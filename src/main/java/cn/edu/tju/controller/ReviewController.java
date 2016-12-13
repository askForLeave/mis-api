package cn.edu.tju.controller;

import cn.edu.tju.dao.LeaveAppRepo;
import cn.edu.tju.dao.StaffRepo;
import cn.edu.tju.dto.ErrorReporter;
import cn.edu.tju.dto.ResponseData;
import cn.edu.tju.dto.ResponseLeaveApplication;
import cn.edu.tju.model.LeaveApplication;
import cn.edu.tju.model.Staff;
import cn.edu.tju.model.User;
import cn.edu.tju.service.LoginService;
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
    LoginService loginService;

    @Autowired
    HttpSession httpSession;

    @Autowired
    LeaveAppRepo leaveAppRepo;

    @Autowired
    StaffRepo staffRepo;

    @RequestMapping("/leave/review/todoList")
    public ErrorReporter todoList (String username, int page, int pageSize) {

        if ( !loginService.isLogin()) {
            return new ErrorReporter(-1, "not login");
        }

        User curUser = (User) httpSession.getAttribute("user");
        Staff curStaff = staffRepo.findOne(curUser.getId());

        int total = leaveAppRepo.countByManagerIdAndStatus(curStaff.getId(), 2);

        Pageable pageable = new PageRequest(page - 1, pageSize);
        List<LeaveApplication> las = leaveAppRepo.findByManagerIdAndStatusOrderByIdDesc(curStaff.getId(), 2, pageable);
        List<ResponseLeaveApplication> list = new ArrayList<>();
        for (LeaveApplication e:las) {
            list.add(new ResponseLeaveApplication(e));
        }
        ResponseData data = new ResponseData(page, pageSize, total, curStaff.getId(), list);
        return new ErrorReporter(0, "success", data);
    }

    @RequestMapping("/leave/review/doneList")
    public ErrorReporter doneList(String username, int page, int pageSize) {

        if ( !loginService.isLogin()) {
            return new ErrorReporter(-1, "not login");
        }

        User curUser = (User) httpSession.getAttribute("user");
        Staff curStaff = staffRepo.findOne(curUser.getId());

        int total = leaveAppRepo.countByManagerIdAndStatusIn(curStaff.getId(), Arrays.asList(3,4));

        Pageable pageable = new PageRequest(page - 1, pageSize);
        List<LeaveApplication> las = leaveAppRepo.findByManagerIdAndStatusInOrderByIdDesc(curStaff.getId(), Arrays.asList(3,4), pageable);
        List<ResponseLeaveApplication> list = new ArrayList<>();
        for (LeaveApplication e: las) {
            list.add(new ResponseLeaveApplication(e));
        }

        ResponseData data = new ResponseData(page, pageSize, total, curStaff.getId(), list);
        return new ErrorReporter(0, "success", data);
    }

    @RequestMapping("/leave/review/action")
    public ErrorReporter action(int id, int status, String reviewReason) {

        if ( !loginService.isLogin()) {
            return new ErrorReporter(-1, "not login");
        }

        User curUser = (User) httpSession.getAttribute("user");
        Staff curStaff = staffRepo.findOne(curUser.getId());

        LeaveApplication la;
        if (leaveAppRepo.exists(id)) {
            la = leaveAppRepo.findOne(id);
        } else {
            return new ErrorReporter(-1, "application id don't exist");
        }

        if ( !la.getManagerId().equals(curStaff.getId())) {
            return new ErrorReporter(-1, "no authority for this leave application");
        }

        if (la.getStatus() != 2) {
            return new ErrorReporter(-1, "could not review this leave application, has reviewed or hasn't publish");
        }

        if (status != 3 && status != 4) {
            return new ErrorReporter(-1, "wrong status");
        }

        la.setStatus(status);
        la.setReviewReason(reviewReason);
        leaveAppRepo.save(la);

        return new ErrorReporter(0, "success");
    }

}
