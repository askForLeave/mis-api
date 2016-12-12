package cn.edu.tju.controller;

import cn.edu.tju.dao.LeaveAppRepo;
import cn.edu.tju.dao.StaffRepo;
import cn.edu.tju.dao.UserRepo;
import cn.edu.tju.dto.ErrorReporter;
import cn.edu.tju.dto.ResponseData;
import cn.edu.tju.model.LeaveApplication;
import cn.edu.tju.model.Staff;
import cn.edu.tju.model.User;
import cn.edu.tju.service.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.Mapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;

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
        LeaveApplication la = leaveAppRepo.save(new LeaveApplication(username , curStaff.getName() , startTime , endTime , curTime , reason, type, submitStatus, curStaff.getManagerId(), curStaff.getManagerName(), 0 , ""));

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
}
