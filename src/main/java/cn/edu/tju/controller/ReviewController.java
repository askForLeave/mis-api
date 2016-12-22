package cn.edu.tju.controller;

import cn.edu.tju.dao.LeaveAppRepo;
import cn.edu.tju.dao.StaffRepo;
import cn.edu.tju.dto.ErrorReporter;
import cn.edu.tju.dto.ResponseData;
import cn.edu.tju.dto.ResponseLeaveApplication;
import cn.edu.tju.dto.ResponseListData;
import cn.edu.tju.model.LeaveApplication;
import cn.edu.tju.model.Staff;
import cn.edu.tju.model.User;
import cn.edu.tju.service.LoginService;
import com.google.gson.Gson;
import org.joda.time.Days;
import org.joda.time.LocalDate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@RestController
public class ReviewController {

    @Autowired
    protected LoginService loginService;

    @Autowired
    protected HttpSession httpSession;

    @Autowired
    protected LeaveAppRepo leaveAppRepo;

    @Autowired
    protected StaffRepo staffRepo;

    @RequestMapping("/leave/review/todoList")
    public ErrorReporter todoList (String username, int page, int pageSize) {

        if ( !loginService.isLogin()) {
            return new ErrorReporter(4, "not login");
        }

        User curUser = (User) httpSession.getAttribute("user");
        Staff curStaff = staffRepo.findOne(curUser.getId());

        int total = leaveAppRepo.countByManagerIdAndStatus(curStaff.getId(), 2);

        Pageable pageable = new PageRequest(page - 1, pageSize);
        List<LeaveApplication> las = leaveAppRepo.findByManagerIdAndStatusOrderByApplyTimeDesc(curStaff.getId(), 2, pageable);
        List<ResponseLeaveApplication> list = new ArrayList<>();
        for (LeaveApplication e:las) {
            list.add(new ResponseLeaveApplication(e));
        }
        ResponseListData data = new ResponseListData(page, pageSize, total, curStaff.getId(), list);
        return new ErrorReporter(0, "success", data);
    }

    @RequestMapping("/leave/review/doneList")
    public ErrorReporter doneList(String username, int page, int pageSize) {

        if ( !loginService.isLogin()) {
            return new ErrorReporter(4, "not login");
        }

        User curUser = (User) httpSession.getAttribute("user");
        Staff curStaff = staffRepo.findOne(curUser.getId());

        int total = leaveAppRepo.countByManagerIdAndStatusIn(curStaff.getId(), Arrays.asList(3,4));

        Pageable pageable = new PageRequest(page - 1, pageSize);
        List<LeaveApplication> las = leaveAppRepo.findByManagerIdAndStatusInOrderByApplyTimeDesc(curStaff.getId(), Arrays.asList(3,4), pageable);
        List<ResponseLeaveApplication> list = new ArrayList<>();
        for (LeaveApplication e: las) {
            list.add(new ResponseLeaveApplication(e));
        }

        ResponseListData data = new ResponseListData(page, pageSize, total, curStaff.getId(), list);
        return new ErrorReporter(0, "success", data);
    }

    @RequestMapping("/leave/review/action")
    public ErrorReporter action(int id, int status, String reviewReason) {

        if ( !loginService.isLogin()) {
            return new ErrorReporter(4, "not login");
        }

        User curUser = (User) httpSession.getAttribute("user");
        Staff curStaff = staffRepo.findOne(curUser.getId());

        LeaveApplication la;
        if (leaveAppRepo.exists(id)) {
            la = leaveAppRepo.findOne(id);
        } else {
            return new ErrorReporter(19, "application id don't exist");
        }

        if ( !la.getManagerId().equals(curStaff.getId())) {
            return new ErrorReporter(23, "no authority for this leave application");
        }

        if (la.getStatus() != 2) {
            return new ErrorReporter(24, "could not review this leave application, has reviewed or hasn't publish");
        }

        if (status != 3 && status != 4) {
            return new ErrorReporter(25, "wrong status");
        }

        Staff applicantStaff = staffRepo.findOne(la.getApplicantId());
        Gson gson = new Gson();
        int[] leaveDetail = gson.fromJson(applicantStaff.getLeaveDetail(), int[].class);
        LocalDate initialDay = new LocalDate(2016,1,1);
        LocalDate startDay = new LocalDate(la.getStartTime()*1000L);
        LocalDate endDay = new LocalDate(la.getEndTime()*1000L);
        int startDayIndex = Days.daysBetween(initialDay, startDay).getDays();
        int endDayIndex = Days.daysBetween(initialDay, endDay).getDays();

        if (status == 4) {  // not approved
            for (int i = startDayIndex; i <= endDayIndex; i++) {
                if(leaveDetail[i] >= 100) {
                    leaveDetail[i] -= 100;
                }
            }
            if (la.getType() == 1) {
                int annualLeft = applicantStaff.getAnnualLeft();
                for (int i = startDayIndex; i <= endDayIndex; i++) {
                    if(leaveDetail[i] == 0) {
                        annualLeft ++;
                    }
                }
                applicantStaff.setAnnualLeft(annualLeft);
            }
        } else {    // status == 3, approved
            if (Arrays.asList(1,2,3,4,5,6,7).contains(la.getType())) {
                for (int i = startDayIndex; i <= endDayIndex; i++) {
                    if (leaveDetail[i] == 100) {
                        leaveDetail[i] = la.getType();
                    }
                }
            } else if (la.getType() == 10) {
                for (int i = startDayIndex; i <= endDayIndex; i++) {
                    if (leaveDetail[i] == 109 || leaveDetail[i] == 108) {
                        leaveDetail[i] -= 90;
                        // 18, 19 in leave detail means work at holiday or weekends
                    }
                }
            } else {
                return new ErrorReporter(13, "unknown type");
            }
        }
        applicantStaff.setLeaveDetail(gson.toJson(leaveDetail));
        staffRepo.save(applicantStaff);

        la.setStatus(status);
        la.setReviewReason(reviewReason);
        int curTime = (int) (System.currentTimeMillis() / 1000L);
        la.setReviewTime(curTime);
        leaveAppRepo.save(la);

        return new ErrorReporter(0, "success");
    }
}
