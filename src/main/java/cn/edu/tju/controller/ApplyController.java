package cn.edu.tju.controller;

import cn.edu.tju.dao.LeaveAppRepo;
import cn.edu.tju.dao.StaffRepo;
import cn.edu.tju.dao.UserRepo;
import cn.edu.tju.dto.ErrorReporter;
import cn.edu.tju.dto.ResponseData;
import cn.edu.tju.dto.ResponseLeaveApplication;
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
import org.springframework.web.bind.annotation.Mapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@RestController
public class ApplyController {

    @Autowired
    private HttpSession httpSession;

    @Autowired
    private LoginService loginService;

    @Autowired
    private LeaveAppRepo leaveAppRepo;

    @Autowired
    private StaffRepo staffRepo;


    @RequestMapping("/leave/apply/add")
    public ErrorReporter add(String username, int startTime, int endTime, int type, String reason, int submitStatus) {

        if ( !loginService.isLogin()) {
            return new ErrorReporter(-1, "not login");
        }

        if (startTime > endTime) {
            return new ErrorReporter(-1, "invalid start time and end time");
        }

        if (submitStatus != 1 && submitStatus != 2) {
            return new ErrorReporter(-1, "invalid submit status");
        }

        User curUser = ((User)httpSession.getAttribute("user"));
        if ( !curUser.getId().equals(username)) {
            return new ErrorReporter(-1, "should only apply leave for yourself");
        }

        Staff curStaff = staffRepo.findOne( curUser.getId() );

        if (submitStatus == 2) {
            Gson gson = new Gson();
            int[] leaveDetail =  gson.fromJson(curStaff.getLeaveDetail(), int[].class);
            LocalDate initialDay = new LocalDate(2016,1,1);
            LocalDate startDay = new LocalDate(startTime*1000L);
            LocalDate endDay = new LocalDate(endTime*1000L);
            int startDayIndex = Days.daysBetween(initialDay, startDay).getDays();
            int endDayIndex = Days.daysBetween(initialDay, endDay).getDays();

            for (int i = startDayIndex; i <= endDayIndex; i++) {
                if ( leaveDetail[i] != 0 && leaveDetail[i] != 9) {
                    return new ErrorReporter(-1, "invalid period for leave application, please check your start time and end time");
                }
            }

            if (type == 1) {
                int annualLeft = curStaff.getAnnualLeft();
                for (int i = startDayIndex; i <= endDayIndex; i++) {
                    if (leaveDetail[i] == 0) {
                        annualLeft --;
                    }
                }
                if (annualLeft < 0){
                    return new ErrorReporter(-1, "your left annual leave is not enough");
                }
                for (int i = startDayIndex; i <= endDayIndex; i++) {
                    if (leaveDetail[i] == 0) {
                        leaveDetail[i] += 100;
                    }
                }
                curStaff.setAnnualLeft(annualLeft);
            } else {
                for (int i = startDayIndex; i <= endDayIndex; i++) {
                    if (leaveDetail[i] == 0) {
                        leaveDetail[i] += 100;
                    }
                }
            }

            curStaff.setLeaveDetail(gson.toJson(leaveDetail));
            staffRepo.save(curStaff);
        }

        int curTime = (int) (System.currentTimeMillis() / 1000L);
        LeaveApplication la = leaveAppRepo.save(new LeaveApplication(username , curStaff.getName() , startTime , endTime , curTime , reason, type, submitStatus, ""+ curStaff.getDepartment(), curStaff.getManagerId(), curStaff.getManagerName(), 0 , ""));

        return new ErrorReporter(0, "success");
    }

    @RequestMapping("/leave/apply/modify")
    public ErrorReporter modify(String username, int startTime, int endTime, int type, String reason, int submitStatus, int id) {

        if ( !loginService.isLogin()) {
            return new ErrorReporter(-1, "not login");
        }

        if (startTime > endTime) {
            return new ErrorReporter(-1, "invalid start time and end time");
        }

        if (submitStatus != 1 && submitStatus != 2) {
            return new ErrorReporter(-1, "invalid submit status");
        }

        User curUser = ((User)httpSession.getAttribute("user"));
        Staff curStaff = staffRepo.findOne( curUser.getId() );
        if ( !curUser.getId().equals(username)) {
            return new ErrorReporter(-1, "should only modify leave applications for yourself");
        }

        LeaveApplication la;
        if (leaveAppRepo.exists(id)){
            la = leaveAppRepo.findOne(id);
        } else {
            return new ErrorReporter(-1, "application not exist");
        }

        if (la.getStatus() != 1 || !la.getApplicantName().equals(curStaff.getId()) ) {
            return new ErrorReporter(-1, "can not modify");
        }

        if (submitStatus == 2) {    // then change the leave details of the staff
            Gson gson = new Gson();
            int[] leaveDetail =  gson.fromJson(curStaff.getLeaveDetail(), int[].class);

            LocalDate initialDay = new LocalDate(2016,1,1);
            LocalDate startDay = new LocalDate(startTime*1000L);
            LocalDate endDay = new LocalDate(endTime*1000L);
            int startDayIndex = Days.daysBetween(initialDay, startDay).getDays();
            int endDayIndex = Days.daysBetween(initialDay, endDay).getDays();

            for (int i = startDayIndex; i <= endDayIndex; i++) {
                if ( leaveDetail[i] != 0 && leaveDetail[i] != 8 && leaveDetail[i] != 9) {
                    return new ErrorReporter(-1, "invalid period for leave application, please check your start time and end time");
                }
            }

            if (type == 1) {
                int annualLeft = curStaff.getAnnualLeft();
                for (int i = startDayIndex; i <= endDayIndex; i++) {
                    if (leaveDetail[i] == 0) {
                        annualLeft --;
                    }
                }
                if (annualLeft < 0){
                    return new ErrorReporter(-1, "your left annual leave is not enough");
                }
                for (int i = startDayIndex; i <= endDayIndex; i++) {
                    if (leaveDetail[i] == 0) {
                        leaveDetail[i] += 100;
                    }
                }
                curStaff.setAnnualLeft(annualLeft);
            } else {
                for (int i = startDayIndex; i <= endDayIndex; i++) {
                    if (leaveDetail[i] == 0) {
                        leaveDetail[i] += 100;
                    }
                }
            }

            curStaff.setLeaveDetail(gson.toJson(leaveDetail));
            staffRepo.save(curStaff);
        }

        la.setStartTime(startTime);
        la.setEndTime(endTime);
        la.setType(type);
        la.setApplyReason(reason);
        la.setStatus(submitStatus);

        int curTime = (int) (System.currentTimeMillis() / 1000L);
        la.setApplyTime(curTime);

        la.setApplicantName(curStaff.getName());
        la.setManagerId(curStaff.getManagerId());
        la.setManagerName(curStaff.getManagerName());
        la = leaveAppRepo.save(la);

        return new ErrorReporter(0, "success");
    }

    @RequestMapping("/leave/apply/info")
    public ErrorReporter info(String username) {

        if ( !loginService.isLogin()) {
            return new ErrorReporter(-1, "not login");
        }

        User curUser = ((User)httpSession.getAttribute("user"));
        Staff curStaff = staffRepo.findOne( curUser.getId() );
        ResponseData rd = new ResponseData(curStaff.getId(), curStaff.getName(), curStaff.getManagerId(), curStaff.getManagerName(), curStaff.getDepartment(), curStaff.getAnnualTotal(), curStaff.getAnnualLeft());

        return new ErrorReporter(0, "success", rd);
    }

    @RequestMapping("/leave/apply/delete")
    public ErrorReporter info(int id) {

        if ( !loginService.isLogin()) {
            return new ErrorReporter(-1, "not login");
        }

        User curUser = ((User)httpSession.getAttribute("user"));
        Staff curStaff = staffRepo.findOne( curUser.getId() );

        LeaveApplication la;
        if (leaveAppRepo.exists(id)){
            la = leaveAppRepo.findOne(id);
        } else {
            return new ErrorReporter(-1, "application not exist");
        }

        if (la.getStatus() != 1 || !la.getApplicantName().equals(curStaff.getId()) ) {
            return new ErrorReporter(-1, "can not delete");
        }

        leaveAppRepo.delete(la);

        return new ErrorReporter(0, "success");
    }

    @RequestMapping("/leave/apply/draftList")
    public ErrorReporter draftList(String username, int page, int pageSize) {

        if ( !loginService.isLogin()) {
            return new ErrorReporter(-1, "not login");
        }

        User curUser = ((User)httpSession.getAttribute("user"));
        Staff curStaff = staffRepo.findOne( curUser.getId() );

        int total = leaveAppRepo.countByApplicantIdAndStatus(curStaff.getId(), 1);

        Pageable pageable = new PageRequest(page - 1, pageSize);
        List<LeaveApplication> las = leaveAppRepo.findByApplicantIdAndStatusOrderByIdDesc(curStaff.getId(), 1, pageable);

        // parse to format for transfer, that is caused by not strictly follow the agreement with front side when develop.
        List<ResponseLeaveApplication> list = new ArrayList<>();
        for (LeaveApplication e : las){
            list.add(new ResponseLeaveApplication(e));
        }

        ResponseData responseData = new ResponseData(page, pageSize, total, curStaff.getId(), list);

        return new ErrorReporter(0, "success", responseData);
    }

    @RequestMapping("/leave/apply/publishList")
    public ErrorReporter publishList(String username, int page, int pageSize) {

        if ( !loginService.isLogin()) {
            return new ErrorReporter(-1, "not login");
        }

        User curUser = ((User)httpSession.getAttribute("user"));
        Staff curStaff = staffRepo.findOne( curUser.getId() );

        int total = leaveAppRepo.countByApplicantIdAndStatusIn(curStaff.getId(), Arrays.asList(2,3,4));

        Pageable pageable = new PageRequest(page - 1, pageSize);
        List<LeaveApplication> las = leaveAppRepo.findByApplicantIdAndStatusInOrderByIdDesc(curStaff.getId(), Arrays.asList(2,3,4), pageable);

        // parse to format for transfer, that is caused by not strictly follow the agreement with front side when develop.
        List<ResponseLeaveApplication> list = new ArrayList<>();
        for (LeaveApplication e : las){
            list.add(new ResponseLeaveApplication(e));
        }

        ResponseData responseData = new ResponseData(page, pageSize, total, curStaff.getId(), list);

        return new ErrorReporter(0, "success", responseData);
    }
}
