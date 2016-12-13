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

        User curUser = ((User)httpSession.getAttribute("user"));
        if ( !curUser.getId().equals(username)) {
            return new ErrorReporter(-1, "should only apply leave for yourself");
        }

        int curTime = (int) (System.currentTimeMillis() / 1000L);
        Staff curStaff = staffRepo.findOne( curUser.getId() );
        LeaveApplication la = leaveAppRepo.save(new LeaveApplication(username , curStaff.getName() , startTime , endTime , curTime , reason, type, submitStatus, ""+ curStaff.getDepartment(), curStaff.getManagerId(), curStaff.getManagerName(), 0 , ""));

        return new ErrorReporter(0, "success");
    }

    @RequestMapping("/leave/apply/modify")
    public ErrorReporter modify(String username, int startTime, int endTime, int type, String reason, int submitStatus, int id) {

        if ( !loginService.isLogin()) {
            return new ErrorReporter(-1, "not login");
        }

        User curUser = ((User)httpSession.getAttribute("user"));
        if ( !curUser.getId().equals(username)) {
            return new ErrorReporter(-1, "should only modify leave applications for yourself");
        }

        Staff curStaff = staffRepo.findOne( curUser.getId() );
        int curTime = (int) (System.currentTimeMillis() / 1000L);

        LeaveApplication la = leaveAppRepo.findOne(id);
        la.setStartTime(startTime);
        la.setEndTime(endTime);
        la.setType(type);
        la.setApplyReason(reason);
        la.setStatus(submitStatus);

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

        int[] values = {2,3,4};
        int total = 0;
        for (int i : values) {
            total += leaveAppRepo.countByApplicantIdAndStatus(curStaff.getId(), i);
        }

        Pageable pageable = new PageRequest(page - 1, pageSize);
        List<LeaveApplication> las = leaveAppRepo.findByApplicantIdAndStatusInOrderByIdDesc(curStaff.getId(), Arrays.asList(2,3,4), pageable);
//        for (int i : values) {
//            las.addAll( leaveAppRepo.findByApplicantIdAndStatusOrderByIdDesc(curStaff.getId(), i, pageable) );
//        }

        // parse to format for transfer, that is caused by not strictly follow the agreement with front side when develop.
        List<ResponseLeaveApplication> list = new ArrayList<>();
        for (LeaveApplication e : las){
            list.add(new ResponseLeaveApplication(e));
        }

        ResponseData responseData = new ResponseData(page, pageSize, total, curStaff.getId(), list);

        return new ErrorReporter(0, "success", responseData);
    }
}
