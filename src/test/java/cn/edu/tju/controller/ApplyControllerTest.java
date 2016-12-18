package cn.edu.tju.controller;

import cn.edu.tju.dao.LeaveAppRepo;
import cn.edu.tju.dao.StaffRepo;
import cn.edu.tju.dto.*;
import cn.edu.tju.model.LeaveApplication;
import cn.edu.tju.model.Staff;
import cn.edu.tju.model.User;
import cn.edu.tju.service.LoginService;
import com.google.gson.Gson;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import javax.servlet.http.HttpSession;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;


public class ApplyControllerTest {
    private ErrorReporter nologin = new ErrorReporter(4, "not login");
    private ErrorReporter invalidtime = new ErrorReporter(11, "invalid start time and end time");
    private ErrorReporter invalidstatus = new ErrorReporter(12, "invalid submit status");
    private ErrorReporter unknowntype = new ErrorReporter(13, "unknown type");
    private ErrorReporter erroruser = new ErrorReporter(14, "should only apply leave for yourself");
    private ErrorReporter invalidperiod = new ErrorReporter(15, "invalid period for leave application, please check your start time and end time");
    private ErrorReporter noenoughannual = new ErrorReporter(16, "your left annual leave is not enough");
    private ErrorReporter cannotapplyovertime = new ErrorReporter(17, "can not apply overtime");
    private ErrorReporter erroruser2 = new ErrorReporter(18, "should only modify leave applications for yourself");
    private ErrorReporter noexist = new ErrorReporter(19, "application not exist");
    private ErrorReporter nomodify = new ErrorReporter(20, "can not modify");
    private ErrorReporter success = new ErrorReporter(0, "success");
    private ErrorReporter success2;
    private ErrorReporter nodelete = new ErrorReporter(21, "can not delete");
    private ApplyController applyController;
    private Gson gson;
    @Before
    public void setUp() throws Exception {
        applyController = new ApplyController();
        gson =  new Gson();
    }

    @After
    public void tearDown() throws Exception {

    }

    @Test
    public void addTest1() throws Exception {
        applyController.loginService = mock(LoginService.class);
        when(applyController.loginService.isLogin()).thenReturn(false);
        ErrorReporter actualReporter = applyController.add("test",1,2,1,"test",1);
        String expected = gson.toJson(nologin);
        String actual = gson.toJson(actualReporter);
        assertEquals(expected,actual);
    }

    @Test
    public void addTest2() throws Exception {
        applyController.loginService = mock(LoginService.class);
        when(applyController.loginService.isLogin()).thenReturn(true);
        ErrorReporter actualReporter = applyController.add("test",2,1,1,"test",1);
        String expected = gson.toJson(invalidtime);
        String actual = gson.toJson(actualReporter);
        assertEquals(expected,actual);
    }

    @Test
    public void addTest3() throws Exception {
        applyController.loginService = mock(LoginService.class);
        when(applyController.loginService.isLogin()).thenReturn(true);
        ErrorReporter actualReporter = applyController.add("test",1,2,1,"test",3);
        String expected = gson.toJson(invalidstatus);
        String actual = gson.toJson(actualReporter);
        assertEquals(expected,actual);
    }

    @Test
    public void addTest4() throws Exception {
        applyController.loginService = mock(LoginService.class);
        when(applyController.loginService.isLogin()).thenReturn(true);
        ErrorReporter actualReporter = applyController.add("test",1,2,8,"test",1);
        String expected = gson.toJson(unknowntype);
        String actual = gson.toJson(actualReporter);
        assertEquals(expected,actual);
    }

    @Test
    public void addTest5() throws Exception {
        applyController.loginService = mock(LoginService.class);
        when(applyController.loginService.isLogin()).thenReturn(true);
        applyController.httpSession = mock(HttpSession.class);
        User testuser = new User("error","test");
        when(applyController.httpSession.getAttribute("user")).thenReturn(testuser);
        ErrorReporter actualReporter = applyController.add("test",1,2,1,"test",1);
        String expected = gson.toJson(erroruser);
        String actual = gson.toJson(actualReporter);
        assertEquals(expected,actual);
    }

    @Test
    public void addTest6() throws Exception {
        applyController.loginService = mock(LoginService.class);
        when(applyController.loginService.isLogin()).thenReturn(true);
        applyController.httpSession = mock(HttpSession.class);
        User testuser = new User("test","test");
        when(applyController.httpSession.getAttribute("user")).thenReturn(testuser);
        applyController.staffRepo = mock(StaffRepo.class);
        int leaveditail[] = new int [400];
        for(int i=0;i<400;i++)leaveditail[i]=1;
        Staff staff = new Staff("test","test",1,20,20,"test","testM","testM",gson.toJson(leaveditail));
        when(applyController.staffRepo.findOne(testuser.getId())).thenReturn(staff);
        ErrorReporter actualReporter = applyController.add("test",1456761600,1457625600,1,"test",2);
        String expected = gson.toJson(invalidperiod);
        String actual = gson.toJson(actualReporter);
        assertEquals(expected,actual);
    }

    @Test
    public void addTest7() throws Exception {
        applyController.loginService = mock(LoginService.class);
        when(applyController.loginService.isLogin()).thenReturn(true);
        applyController.httpSession = mock(HttpSession.class);
        User testuser = new User("test","test");
        when(applyController.httpSession.getAttribute("user")).thenReturn(testuser);
        applyController.staffRepo = mock(StaffRepo.class);
        int leaveDetail[] = new int [400];
        leaveDetail[0] = 0;
        for (int i = 2; i < leaveDetail.length; i += 7) {
            leaveDetail[i] = 9;
            leaveDetail[i-1] = 9;
        }
        if ((leaveDetail.length - 1) % 7 == 2)	leaveDetail[leaveDetail.length - 1] = 9;
        leaveDetail[5] = 1;
        Staff staff = new Staff("test","test",1,20,0,"test","testM","testM",gson.toJson(leaveDetail));
        when(applyController.staffRepo.findOne(testuser.getId())).thenReturn(staff);
        ErrorReporter actualReporter = applyController.add("test",1456761600,1457625600,1,"test",2);
        String expected = gson.toJson(noenoughannual);
        String actual = gson.toJson(actualReporter);
        assertEquals(expected,actual);
    }

    @Test
    public void addTest8() throws Exception {
        applyController.loginService = mock(LoginService.class);
        when(applyController.loginService.isLogin()).thenReturn(true);
        applyController.httpSession = mock(HttpSession.class);
        User testuser = new User("test","test");
        when(applyController.httpSession.getAttribute("user")).thenReturn(testuser);
        applyController.staffRepo = mock(StaffRepo.class);
        int leaveDetail[] = new int [400];
        leaveDetail[0] = 0;
        for (int i = 2; i < leaveDetail.length; i += 7) {
            leaveDetail[i] = 9;
            leaveDetail[i-1] = 9;
        }
        if ((leaveDetail.length - 1) % 7 == 2)	leaveDetail[leaveDetail.length - 1] = 9;
        leaveDetail[5] = 1;
        Staff staff = new Staff("test","test",1,20,20,"test","testM","testM",gson.toJson(leaveDetail));
        when(applyController.staffRepo.findOne(testuser.getId())).thenReturn(staff);
        ErrorReporter actualReporter = applyController.add("test",1456761600,1456761600,10,"test",2);
        String expected = gson.toJson(cannotapplyovertime);
        String actual = gson.toJson(actualReporter);
        assertEquals(expected,actual);
    }

    @Test
    public void addTest9() throws Exception {
        applyController.loginService = mock(LoginService.class);
        when(applyController.loginService.isLogin()).thenReturn(true);
        applyController.httpSession = mock(HttpSession.class);
        User testuser = new User("test","test");
        when(applyController.httpSession.getAttribute("user")).thenReturn(testuser);
        applyController.staffRepo = mock(StaffRepo.class);
        int leaveDetail[] = new int [400];
        leaveDetail[0] = 0;
        for (int i = 2; i < leaveDetail.length; i += 7) {
            leaveDetail[i] = 9;
            leaveDetail[i-1] = 9;
        }
        if ((leaveDetail.length - 1) % 7 == 2)	leaveDetail[leaveDetail.length - 1] = 9;
        leaveDetail[5] = 1;
        Staff staff = new Staff("test","test",1,20,20,"test","testM","testM",gson.toJson(leaveDetail));
        when(applyController.staffRepo.findOne(testuser.getId())).thenReturn(staff);
        applyController.leaveAppRepo = mock(LeaveAppRepo.class);
        int curTime = (int) (System.currentTimeMillis() / 1000L);
        LeaveApplication leaveApplication = new LeaveApplication("test" , ""+staff.getName() , 1456761600 , 1457625600 , curTime , "", 1, 1, ""+ staff.getDepartment(), staff.getManagerId(), staff.getManagerName(), 0 , "");
        when(applyController.leaveAppRepo.save(leaveApplication)).thenReturn(null);
        ErrorReporter actualReporter = applyController.add("test",1456761600,1457625600,1,"test",2);
        String expected = gson.toJson(success);
        String actual = gson.toJson(actualReporter);
        assertEquals(expected,actual);
    }

    @Test
    public void addTest10() throws Exception {
        applyController.loginService = mock(LoginService.class);
        when(applyController.loginService.isLogin()).thenReturn(true);
        applyController.httpSession = mock(HttpSession.class);
        User testuser = new User("test","test");
        when(applyController.httpSession.getAttribute("user")).thenReturn(testuser);
        applyController.staffRepo = mock(StaffRepo.class);
        int leaveDetail[] = new int [400];
        leaveDetail[0] = 0;
        for (int i = 2; i < leaveDetail.length; i += 7) {
            leaveDetail[i] = 9;
            leaveDetail[i-1] = 9;
        }
        if ((leaveDetail.length - 1) % 7 == 2)	leaveDetail[leaveDetail.length - 1] = 9;
        leaveDetail[5] = 1;
        Staff staff = new Staff("test","test",1,20,20,"test","testM","testM",gson.toJson(leaveDetail));
        when(applyController.staffRepo.findOne(testuser.getId())).thenReturn(staff);
        applyController.leaveAppRepo = mock(LeaveAppRepo.class);
        int curTime = (int) (System.currentTimeMillis() / 1000L);
        LeaveApplication leaveApplication = new LeaveApplication("test" , ""+staff.getName() , 1456761600 , 1457625600 , curTime , "", 1, 1, ""+ staff.getDepartment(), staff.getManagerId(), staff.getManagerName(), 0 , "");
        when(applyController.leaveAppRepo.save(leaveApplication)).thenReturn(null);
        ErrorReporter actualReporter = applyController.add("test",1456761600,1457625600,1,"test",1);
        String expected = gson.toJson(success);
        String actual = gson.toJson(actualReporter);
        assertEquals(expected,actual);
    }

    @Test
    public void modifyTest1() throws Exception {
        applyController.loginService = mock(LoginService.class);
        when(applyController.loginService.isLogin()).thenReturn(false);
        ErrorReporter actualReporter = applyController.modify("test",1,2,1,"test",1,1);
        String expected = gson.toJson(nologin);
        String actual = gson.toJson(actualReporter);
        assertEquals(expected,actual);
    }

    @Test
    public void modifyTest2() throws Exception {
        applyController.loginService = mock(LoginService.class);
        when(applyController.loginService.isLogin()).thenReturn(true);
        ErrorReporter actualReporter = applyController.modify("test",2,1,1,"test",1,1);
        String expected = gson.toJson(invalidtime);
        String actual = gson.toJson(actualReporter);
        assertEquals(expected,actual);
    }

    @Test
    public void modifyTest3() throws Exception {
        applyController.loginService = mock(LoginService.class);
        when(applyController.loginService.isLogin()).thenReturn(true);
        ErrorReporter actualReporter = applyController.modify("test",1,2,1,"test",3,1);
        String expected = gson.toJson(invalidstatus);
        String actual = gson.toJson(actualReporter);
        assertEquals(expected,actual);
    }

    @Test
    public void modifyTest4() throws Exception {
        applyController.loginService = mock(LoginService.class);
        when(applyController.loginService.isLogin()).thenReturn(true);
        ErrorReporter actualReporter = applyController.modify("test",1,2,8,"test",1,1);
        String expected = gson.toJson(unknowntype);
        String actual = gson.toJson(actualReporter);
        assertEquals(expected,actual);
    }

    @Test
    public void modifyTest5() throws Exception {
        applyController.loginService = mock(LoginService.class);
        when(applyController.loginService.isLogin()).thenReturn(true);
        applyController.httpSession = mock(HttpSession.class);
        User testuser = new User("error","test");
        when(applyController.httpSession.getAttribute("user")).thenReturn(testuser);
        applyController.staffRepo = mock(StaffRepo.class);
        int leaveditail[] = new int [400];
        for(int i=0;i<400;i++)leaveditail[i]=1;
        Staff staff = new Staff("test","test",1,20,20,"test","testM","testM",gson.toJson(leaveditail));
        when(applyController.staffRepo.findOne(testuser.getId())).thenReturn(staff);
        ErrorReporter actualReporter = applyController.modify("test",1,2,1,"test",1,1);
        String expected = gson.toJson(erroruser2);
        String actual = gson.toJson(actualReporter);
        assertEquals(expected,actual);
    }

    @Test
    public void modifyTest6() throws Exception {
        applyController.loginService = mock(LoginService.class);
        when(applyController.loginService.isLogin()).thenReturn(true);
        applyController.httpSession = mock(HttpSession.class);
        User testuser = new User("test","test");
        when(applyController.httpSession.getAttribute("user")).thenReturn(testuser);
        applyController.staffRepo = mock(StaffRepo.class);
        int leaveditail[] = new int [400];
        for(int i=0;i<400;i++)leaveditail[i]=1;
        Staff staff = new Staff("test","test",1,20,20,"test","testM","testM",gson.toJson(leaveditail));
        when(applyController.staffRepo.findOne(testuser.getId())).thenReturn(staff);
        applyController.leaveAppRepo = mock(LeaveAppRepo.class);
        when(applyController.leaveAppRepo.exists(1)).thenReturn(false);
        ErrorReporter actualReporter = applyController.modify("test",1,2,1,"test",1,1);
        String expected = gson.toJson(noexist);
        String actual = gson.toJson(actualReporter);
        assertEquals(expected,actual);
    }

    @Test
    public void modifyTest7() throws Exception {
        applyController.loginService = mock(LoginService.class);
        when(applyController.loginService.isLogin()).thenReturn(true);
        applyController.httpSession = mock(HttpSession.class);
        User testuser = new User("test","test");
        when(applyController.httpSession.getAttribute("user")).thenReturn(testuser);
        applyController.staffRepo = mock(StaffRepo.class);
        int leaveditail[] = new int [400];
        for(int i=0;i<400;i++)leaveditail[i]=1;
        Staff staff = new Staff("test","test",1,20,20,"test","testM","testM",gson.toJson(leaveditail));
        when(applyController.staffRepo.findOne(testuser.getId())).thenReturn(staff);
        applyController.leaveAppRepo = mock(LeaveAppRepo.class);
        when(applyController.leaveAppRepo.exists(1)).thenReturn(true);
        int curTime = (int) (System.currentTimeMillis() / 1000L);
        LeaveApplication leaveApplication = new LeaveApplication("test" , ""+staff.getName() , 1456761600 , 1457625600 , curTime , "", 1, 3, ""+ staff.getDepartment(), staff.getManagerId(), staff.getManagerName(), 0 , "");
        when(applyController.leaveAppRepo.findOne(1)).thenReturn(leaveApplication);
        ErrorReporter actualReporter = applyController.modify("test",1,2,1,"test",1,1);
        String expected = gson.toJson(nomodify);
        String actual = gson.toJson(actualReporter);
        assertEquals(expected,actual);
    }

    @Test
    public void modifyTest8() throws Exception {
        applyController.loginService = mock(LoginService.class);
        when(applyController.loginService.isLogin()).thenReturn(true);
        applyController.httpSession = mock(HttpSession.class);
        User testuser = new User("test","test");
        when(applyController.httpSession.getAttribute("user")).thenReturn(testuser);
        applyController.staffRepo = mock(StaffRepo.class);
        int leaveditail[] = new int [400];
        for(int i=0;i<400;i++)leaveditail[i]=1;
        Staff staff = new Staff("test","test",1,20,20,"test","testM","testM",gson.toJson(leaveditail));
        when(applyController.staffRepo.findOne(testuser.getId())).thenReturn(staff);
        applyController.leaveAppRepo = mock(LeaveAppRepo.class);
        when(applyController.leaveAppRepo.exists(1)).thenReturn(true);
        int curTime = (int) (System.currentTimeMillis() / 1000L);
        LeaveApplication leaveApplication = new LeaveApplication("test" , ""+staff.getName() , 1456761600 , 1457625600 , curTime , "", 1, 1, ""+ staff.getDepartment(), staff.getManagerId(), staff.getManagerName(), 0 , "");
        when(applyController.leaveAppRepo.findOne(1)).thenReturn(leaveApplication);
        ErrorReporter actualReporter = applyController.modify("test",1456761600,1457625600,1,"test",2,1);
        String expected = gson.toJson(invalidperiod);
        String actual = gson.toJson(actualReporter);
        assertEquals(expected,actual);
    }

    @Test
    public void modifyTest9() throws Exception {
        applyController.loginService = mock(LoginService.class);
        when(applyController.loginService.isLogin()).thenReturn(true);
        applyController.httpSession = mock(HttpSession.class);
        User testuser = new User("test","test");
        when(applyController.httpSession.getAttribute("user")).thenReturn(testuser);
        applyController.staffRepo = mock(StaffRepo.class);
        int leaveDetail[] = new int [400];
        leaveDetail[0] = 0;
        for (int i = 2; i < leaveDetail.length; i += 7) {
            leaveDetail[i] = 9;
            leaveDetail[i-1] = 9;
        }
        if ((leaveDetail.length - 1) % 7 == 2)	leaveDetail[leaveDetail.length - 1] = 9;
        leaveDetail[5] = 1;
        Staff staff = new Staff("test","test",1,20,20,"test","testM","testM",gson.toJson(leaveDetail));
        when(applyController.staffRepo.findOne(testuser.getId())).thenReturn(staff);
        applyController.leaveAppRepo = mock(LeaveAppRepo.class);
        when(applyController.leaveAppRepo.exists(1)).thenReturn(true);
        int curTime = (int) (System.currentTimeMillis() / 1000L);
        LeaveApplication leaveApplication = new LeaveApplication("test" , ""+staff.getName() , 1456761600 , 1457625600 , curTime , "", 1, 1, ""+ staff.getDepartment(), staff.getManagerId(), staff.getManagerName(), 0 , "");
        when(applyController.leaveAppRepo.findOne(1)).thenReturn(leaveApplication);
        applyController.staffRepo = mock(StaffRepo.class);
        Staff staff2 = new Staff("test","test",1,20,0,"test","testM","testM",gson.toJson(leaveDetail));
        when(applyController.staffRepo.findOne(leaveApplication.getApplicantId())).thenReturn(staff2);
        ErrorReporter actualReporter = applyController.modify("test",1456761600,1457625600,1,"test",2,1);
        String expected = gson.toJson(noenoughannual);
        String actual = gson.toJson(actualReporter);
        assertEquals(expected,actual);
    }

    @Test
    public void modifyTes10() throws Exception {
        applyController.loginService = mock(LoginService.class);
        when(applyController.loginService.isLogin()).thenReturn(true);
        applyController.httpSession = mock(HttpSession.class);
        User testuser = new User("test","test");
        when(applyController.httpSession.getAttribute("user")).thenReturn(testuser);
        applyController.staffRepo = mock(StaffRepo.class);
        int leaveDetail[] = new int [400];
        leaveDetail[0] = 0;
        for (int i = 2; i < leaveDetail.length; i += 7) {
            leaveDetail[i] = 9;
            leaveDetail[i-1] = 9;
        }
        if ((leaveDetail.length - 1) % 7 == 2)	leaveDetail[leaveDetail.length - 1] = 9;
        leaveDetail[5] = 1;
        Staff staff = new Staff("test","test",1,20,20,"test","testM","testM",gson.toJson(leaveDetail));
        when(applyController.staffRepo.findOne(testuser.getId())).thenReturn(staff);
        applyController.leaveAppRepo = mock(LeaveAppRepo.class);
        when(applyController.leaveAppRepo.exists(1)).thenReturn(true);
        int curTime = (int) (System.currentTimeMillis() / 1000L);
        LeaveApplication leaveApplication = new LeaveApplication("test" , ""+staff.getName() , 1456761600 , 1457625600 , curTime , "", 1, 1, ""+ staff.getDepartment(), staff.getManagerId(), staff.getManagerName(), 0 , "");
        when(applyController.leaveAppRepo.findOne(1)).thenReturn(leaveApplication);
        applyController.staffRepo = mock(StaffRepo.class);
        Staff staff2 = new Staff("test","test",1,20,20,"test","testM","testM",gson.toJson(leaveDetail));
        when(applyController.staffRepo.findOne(leaveApplication.getApplicantId())).thenReturn(staff2);
        ErrorReporter actualReporter = applyController.modify("test",1456761600,1456761600,10,"test",2,1);
        String expected = gson.toJson(cannotapplyovertime);
        String actual = gson.toJson(actualReporter);
        assertEquals(expected,actual);
    }

    @Test
    public void modifyTes11() throws Exception {
        applyController.loginService = mock(LoginService.class);
        when(applyController.loginService.isLogin()).thenReturn(true);
        applyController.httpSession = mock(HttpSession.class);
        User testuser = new User("test","test");
        when(applyController.httpSession.getAttribute("user")).thenReturn(testuser);
        applyController.staffRepo = mock(StaffRepo.class);
        int leaveDetail[] = new int [400];
        leaveDetail[0] = 0;
        for (int i = 2; i < leaveDetail.length; i += 7) {
            leaveDetail[i] = 9;
            leaveDetail[i-1] = 9;
        }
        if ((leaveDetail.length - 1) % 7 == 2)	leaveDetail[leaveDetail.length - 1] = 9;
        leaveDetail[5] = 1;
        Staff staff = new Staff("test","test",1,20,20,"test","testM","testM",gson.toJson(leaveDetail));
        when(applyController.staffRepo.findOne(testuser.getId())).thenReturn(staff);
        applyController.leaveAppRepo = mock(LeaveAppRepo.class);
        when(applyController.leaveAppRepo.exists(1)).thenReturn(true);
        int curTime = (int) (System.currentTimeMillis() / 1000L);
        LeaveApplication leaveApplication = new LeaveApplication("test" , ""+staff.getName() , 1456761600 , 1457625600 , curTime , "", 1, 1, ""+ staff.getDepartment(), staff.getManagerId(), staff.getManagerName(), 0 , "");
        when(applyController.leaveAppRepo.findOne(1)).thenReturn(leaveApplication);
        applyController.staffRepo = mock(StaffRepo.class);
        Staff staff2 = new Staff("test","test",1,20,20,"test","testM","testM",gson.toJson(leaveDetail));
        when(applyController.staffRepo.findOne(leaveApplication.getApplicantId())).thenReturn(staff2);
        ErrorReporter actualReporter = applyController.modify("test",1456761600,1457625600,1,"test",2,1);
        String expected = gson.toJson(success);
        String actual = gson.toJson(actualReporter);
        assertEquals(expected,actual);
    }

    @Test
    public void modifyTest12() throws Exception {
        applyController.loginService = mock(LoginService.class);
        when(applyController.loginService.isLogin()).thenReturn(true);
        applyController.httpSession = mock(HttpSession.class);
        User testuser = new User("test","test");
        when(applyController.httpSession.getAttribute("user")).thenReturn(testuser);
        applyController.staffRepo = mock(StaffRepo.class);
        int leaveDetail[] = new int [400];
        leaveDetail[0] = 0;
        for (int i = 2; i < leaveDetail.length; i += 7) {
            leaveDetail[i] = 9;
            leaveDetail[i-1] = 9;
        }
        if ((leaveDetail.length - 1) % 7 == 2)	leaveDetail[leaveDetail.length - 1] = 9;
        leaveDetail[5] = 1;
        Staff staff = new Staff("test","test",1,20,20,"test","testM","testM",gson.toJson(leaveDetail));
        when(applyController.staffRepo.findOne(testuser.getId())).thenReturn(staff);
        applyController.leaveAppRepo = mock(LeaveAppRepo.class);
        when(applyController.leaveAppRepo.exists(1)).thenReturn(true);
        int curTime = (int) (System.currentTimeMillis() / 1000L);
        LeaveApplication leaveApplication = new LeaveApplication("test" , ""+staff.getName() , 1456761600 , 1457625600 , curTime , "", 1, 1, ""+ staff.getDepartment(), staff.getManagerId(), staff.getManagerName(), 0 , "");
        when(applyController.leaveAppRepo.findOne(1)).thenReturn(leaveApplication);
        ErrorReporter actualReporter = applyController.modify("test",1456761600,1457625600,1,"test",1,1);
        String expected = gson.toJson(success);
        String actual = gson.toJson(actualReporter);
        assertEquals(expected,actual);
    }

    @Test
    public void infoTest1() throws Exception {
        applyController.loginService = mock(LoginService.class);
        when(applyController.loginService.isLogin()).thenReturn(false);
        ErrorReporter actualReporter = applyController.info("test");
        String expected = gson.toJson(nologin);
        String actual = gson.toJson(actualReporter);
        assertEquals(expected,actual);
    }

    @Test
    public void infoTest2() throws Exception {
        applyController.loginService = mock(LoginService.class);
        when(applyController.loginService.isLogin()).thenReturn(true);
        applyController.httpSession = mock(HttpSession.class);
        User testuser = new User("test","test");
        when(applyController.httpSession.getAttribute("user")).thenReturn(testuser);
        applyController.staffRepo = mock(StaffRepo.class);
        int leaveditail[] = new int [400];
        for(int i=0;i<400;i++)leaveditail[i]=1;
        Staff staff = new Staff("test","test",1,20,20,"test","testM","testM",gson.toJson(leaveditail));
        when(applyController.staffRepo.findOne(testuser.getId())).thenReturn(staff);
        success2 = new ErrorReporter(0, "success");
        ResponseInfoData responseData = new ResponseInfoData(staff.getId(), staff.getName(), staff.getManagerId(), staff.getManagerName(), staff.getDepartment(), staff.getAnnualTotal(), staff.getAnnualLeft());
        success2.setData(responseData);
        ErrorReporter actualReporter = applyController.info("test");
        String expected = gson.toJson(success2);
        String actual = gson.toJson(actualReporter);
        assertEquals(expected,actual);
    }

    @Test
    public void info1Test1() throws Exception {
        applyController.loginService = mock(LoginService.class);
        when(applyController.loginService.isLogin()).thenReturn(false);
        ErrorReporter actualReporter = applyController.info(1);
        String expected = gson.toJson(nologin);
        String actual = gson.toJson(actualReporter);
        assertEquals(expected,actual);
    }

    @Test
    public void info1Test2() throws Exception {
        applyController.loginService = mock(LoginService.class);
        when(applyController.loginService.isLogin()).thenReturn(true);
        applyController.httpSession = mock(HttpSession.class);
        User testuser = new User("test","test");
        when(applyController.httpSession.getAttribute("user")).thenReturn(testuser);
        applyController.staffRepo = mock(StaffRepo.class);
        int leaveDetail[] = new int [400];
        for(int i=0;i<400;i++)leaveDetail[i]=0;
        Staff staff = new Staff("test","test",1,20,20,"test","testM","testM",gson.toJson(leaveDetail));
        when(applyController.staffRepo.findOne(testuser.getId())).thenReturn(staff);
        applyController.leaveAppRepo = mock(LeaveAppRepo.class);
        when(applyController.leaveAppRepo.exists(1)).thenReturn(false);
        ErrorReporter actualReporter = applyController.info(1);
        String expected = gson.toJson(noexist);
        String actual = gson.toJson(actualReporter);
        assertEquals(expected,actual);
    }

    @Test
    public void info1Test3() throws Exception {
        applyController.loginService = mock(LoginService.class);
        when(applyController.loginService.isLogin()).thenReturn(true);
        applyController.httpSession = mock(HttpSession.class);
        User testuser = new User("test","test");
        when(applyController.httpSession.getAttribute("user")).thenReturn(testuser);
        applyController.staffRepo = mock(StaffRepo.class);
        int leaveDetail[] = new int [400];
        for(int i=0;i<400;i++)leaveDetail[i]=0;
        Staff staff = new Staff("test","test",1,20,20,"test","testM","testM",gson.toJson(leaveDetail));
        when(applyController.staffRepo.findOne(testuser.getId())).thenReturn(staff);
        applyController.leaveAppRepo = mock(LeaveAppRepo.class);
        when(applyController.leaveAppRepo.exists(1)).thenReturn(true);
        int curTime = (int) (System.currentTimeMillis() / 1000L);
        LeaveApplication leaveApplication = new LeaveApplication("test" , ""+staff.getName() , 1456761600 , 1457625600 , curTime , "", 1, 2, ""+ staff.getDepartment(), staff.getManagerId(), staff.getManagerName(), 0 , "");
        when(applyController.leaveAppRepo.findOne(1)).thenReturn(leaveApplication);
        ErrorReporter actualReporter = applyController.info(1);
        String expected = gson.toJson(nodelete);
        String actual = gson.toJson(actualReporter);
        assertEquals(expected,actual);
    }

    @Test
    public void info1Test4() throws Exception {
        applyController.loginService = mock(LoginService.class);
        when(applyController.loginService.isLogin()).thenReturn(true);
        applyController.httpSession = mock(HttpSession.class);
        User testuser = new User("test","test");
        when(applyController.httpSession.getAttribute("user")).thenReturn(testuser);
        applyController.staffRepo = mock(StaffRepo.class);
        int leaveDetail[] = new int [400];
        for(int i=0;i<400;i++)leaveDetail[i]=0;
        Staff staff = new Staff("test","test",1,20,20,"test","testM","testM",gson.toJson(leaveDetail));
        when(applyController.staffRepo.findOne(testuser.getId())).thenReturn(staff);
        applyController.leaveAppRepo = mock(LeaveAppRepo.class);
        when(applyController.leaveAppRepo.exists(1)).thenReturn(true);
        int curTime = (int) (System.currentTimeMillis() / 1000L);
        LeaveApplication leaveApplication = new LeaveApplication("test" , ""+staff.getName() , 1456761600 , 1457625600 , curTime , "", 1, 1, ""+ staff.getDepartment(), staff.getManagerId(), staff.getManagerName(), 0 , "");
        when(applyController.leaveAppRepo.findOne(1)).thenReturn(leaveApplication);
        ErrorReporter actualReporter = applyController.info(1);
        String expected = gson.toJson(success);
        String actual = gson.toJson(actualReporter);
        assertEquals(expected,actual);
    }

    @Test
    public void draftListTest1() throws Exception {
        applyController.loginService = mock(LoginService.class);
        when(applyController.loginService.isLogin()).thenReturn(false);
        ErrorReporter actualReporter = applyController.draftList("test",1,1);
        String expected = gson.toJson(nologin);
        String actual = gson.toJson(actualReporter);
        assertEquals(expected,actual);
    }

    @Test
    public void draftListTest2() throws Exception {
        applyController.loginService = mock(LoginService.class);
        when(applyController.loginService.isLogin()).thenReturn(true);
        applyController.httpSession = mock(HttpSession.class);
        User testuser = new User("test","test");
        when(applyController.httpSession.getAttribute("user")).thenReturn(testuser);
        applyController.staffRepo = mock(StaffRepo.class);
        int leaveDetail[] = new int [400];
        for(int i=0;i<400;i++)leaveDetail[i]=0;
        Staff staff = new Staff("test","test",1,20,20,"test","testM","testM",gson.toJson(leaveDetail));
        when(applyController.staffRepo.findOne(testuser.getId())).thenReturn(staff);
        applyController.leaveAppRepo = mock(LeaveAppRepo.class);
        when(applyController.leaveAppRepo.countByApplicantIdAndStatusInAndTypeIn(staff.getId(), Collections.singletonList(1), Arrays.asList(1,2,3,4,5,6,7))).thenReturn(100);
        Pageable pageable = new PageRequest(0, 1);
        List<LeaveApplication> LeaveApplicationlist = new ArrayList<>();
        int curTime = (int) (System.currentTimeMillis() / 1000L);
        LeaveApplication leaveApplication = new LeaveApplication("test" , ""+staff.getName() , 1456761600 , 1457625600 , curTime , "", 1, 1, ""+ staff.getDepartment(), staff.getManagerId(), staff.getManagerName(), 0 , "");
        LeaveApplicationlist.add(leaveApplication);
        when(applyController.leaveAppRepo.findByApplicantIdAndStatusInAndTypeInOrderByApplyTimeDesc(staff.getId(), Collections.singletonList(1), Arrays.asList(1,2,3,4,5,6,7), pageable)).thenReturn(LeaveApplicationlist);
        List<ResponseLeaveApplication> list = new ArrayList<>();
        for (LeaveApplication e : LeaveApplicationlist){
            list.add(new ResponseLeaveApplication(e));
        }
        ResponseListData responseData = new ResponseListData(1, 1, 100, staff.getId(), list);
        success2 = new ErrorReporter(0, "success");
        success2.setData(responseData);
        ErrorReporter actualReporter = applyController.draftList("test",1,1);
        String expected = gson.toJson(success2);
        String actual = gson.toJson(actualReporter);
        assertEquals(expected,actual);
    }

    @Test
    public void publishListTest1() throws Exception {
        applyController.loginService = mock(LoginService.class);
        when(applyController.loginService.isLogin()).thenReturn(false);
        ErrorReporter actualReporter = applyController.publishList("test",1,1);
        String expected = gson.toJson(nologin);
        String actual = gson.toJson(actualReporter);
        assertEquals(expected,actual);
    }

    @Test
    public void publishListTest2() throws Exception {
        applyController.loginService = mock(LoginService.class);
        when(applyController.loginService.isLogin()).thenReturn(true);
        applyController.httpSession = mock(HttpSession.class);
        User testuser = new User("test","test");
        when(applyController.httpSession.getAttribute("user")).thenReturn(testuser);
        applyController.staffRepo = mock(StaffRepo.class);
        int leaveDetail[] = new int [400];
        for(int i=0;i<400;i++)leaveDetail[i]=0;
        Staff staff = new Staff("test","test",1,20,20,"test","testM","testM",gson.toJson(leaveDetail));
        when(applyController.staffRepo.findOne(testuser.getId())).thenReturn(staff);
        applyController.leaveAppRepo = mock(LeaveAppRepo.class);
        when(applyController.leaveAppRepo.countByApplicantIdAndStatusInAndTypeIn(staff.getId(), Arrays.asList(2,3,4), Arrays.asList(1,2,3,4,5,6,7))).thenReturn(100);
        Pageable pageable = new PageRequest(0, 1);
        List<LeaveApplication> LeaveApplicationlist = new ArrayList<>();
        int curTime = (int) (System.currentTimeMillis() / 1000L);
        LeaveApplication leaveApplication = new LeaveApplication("test" , ""+staff.getName() , 1456761600 , 1457625600 , curTime , "", 1, 1, ""+ staff.getDepartment(), staff.getManagerId(), staff.getManagerName(), 0 , "");
        LeaveApplicationlist.add(leaveApplication);
        when(applyController.leaveAppRepo.findByApplicantIdAndStatusInAndTypeInOrderByApplyTimeDesc(staff.getId(), Arrays.asList(2,3,4), Arrays.asList(1,2,3,4,5,6,7), pageable)).thenReturn(LeaveApplicationlist);
        List<ResponseLeaveApplication> list = new ArrayList<>();
        for (LeaveApplication e : LeaveApplicationlist){
            list.add(new ResponseLeaveApplication(e));
        }
        ResponseListData responseData = new ResponseListData(1, 1, 100, staff.getId(), list);
        success2 = new ErrorReporter(0, "success");
        success2.setData(responseData);
        ErrorReporter actualReporter = applyController.publishList("test",1,1);
        String expected = gson.toJson(success2);
        String actual = gson.toJson(actualReporter);
        assertEquals(expected,actual);
    }

    @Test
    public void overtimeDraftListTest1() throws Exception {
        applyController.loginService = mock(LoginService.class);
        when(applyController.loginService.isLogin()).thenReturn(false);
        ErrorReporter actualReporter = applyController.overtimeDraftList("test",1,1);
        String expected = gson.toJson(nologin);
        String actual = gson.toJson(actualReporter);
        assertEquals(expected,actual);
    }

    @Test
    public void overtimeDraftListTest2() throws Exception {
        applyController.loginService = mock(LoginService.class);
        when(applyController.loginService.isLogin()).thenReturn(true);
        applyController.httpSession = mock(HttpSession.class);
        User testuser = new User("test","test");
        when(applyController.httpSession.getAttribute("user")).thenReturn(testuser);
        applyController.staffRepo = mock(StaffRepo.class);
        int leaveDetail[] = new int [400];
        for(int i=0;i<400;i++)leaveDetail[i]=0;
        Staff staff = new Staff("test","test",1,20,20,"test","testM","testM",gson.toJson(leaveDetail));
        when(applyController.staffRepo.findOne(testuser.getId())).thenReturn(staff);
        applyController.leaveAppRepo = mock(LeaveAppRepo.class);
        when(applyController.leaveAppRepo.countByApplicantIdAndStatusInAndTypeIn(staff.getId(), Collections.singletonList(1), Collections.singletonList(10))).thenReturn(100);
        Pageable pageable = new PageRequest(0, 1);
        List<LeaveApplication> LeaveApplicationlist = new ArrayList<>();
        int curTime = (int) (System.currentTimeMillis() / 1000L);
        LeaveApplication leaveApplication = new LeaveApplication("test" , ""+staff.getName() , 1456761600 , 1457625600 , curTime , "", 1, 1, ""+ staff.getDepartment(), staff.getManagerId(), staff.getManagerName(), 0 , "");
        LeaveApplicationlist.add(leaveApplication);
        when(applyController.leaveAppRepo.findByApplicantIdAndStatusInAndTypeInOrderByApplyTimeDesc(staff.getId(), Collections.singletonList(1), Collections.singletonList(10), pageable)).thenReturn(LeaveApplicationlist);
        List<ResponseLeaveApplication> list = new ArrayList<>();
        for (LeaveApplication e : LeaveApplicationlist){
            list.add(new ResponseLeaveApplication(e));
        }
        ResponseListData responseData = new ResponseListData(1, 1, 100, staff.getId(), list);
        success2 = new ErrorReporter(0, "success");
        success2.setData(responseData);
        ErrorReporter actualReporter = applyController.overtimeDraftList("test",1,1);
        String expected = gson.toJson(success2);
        String actual = gson.toJson(actualReporter);
        assertEquals(expected,actual);
    }

    @Test
    public void overtimePublishListTest1() throws Exception {
        applyController.loginService = mock(LoginService.class);
        when(applyController.loginService.isLogin()).thenReturn(false);
        ErrorReporter actualReporter = applyController.overtimePublishList("test",1,1);
        String expected = gson.toJson(nologin);
        String actual = gson.toJson(actualReporter);
        assertEquals(expected,actual);
    }

    @Test
    public void overtimePublishListTest2() throws Exception {
        applyController.loginService = mock(LoginService.class);
        when(applyController.loginService.isLogin()).thenReturn(true);
        applyController.httpSession = mock(HttpSession.class);
        User testuser = new User("test","test");
        when(applyController.httpSession.getAttribute("user")).thenReturn(testuser);
        applyController.staffRepo = mock(StaffRepo.class);
        int leaveDetail[] = new int [400];
        for(int i=0;i<400;i++)leaveDetail[i]=0;
        Staff staff = new Staff("test","test",1,20,20,"test","testM","testM",gson.toJson(leaveDetail));
        when(applyController.staffRepo.findOne(testuser.getId())).thenReturn(staff);
        applyController.leaveAppRepo = mock(LeaveAppRepo.class);
        when(applyController.leaveAppRepo.countByApplicantIdAndStatusInAndTypeIn(staff.getId(), Arrays.asList(2,3,4), Collections.singletonList(10))).thenReturn(100);
        Pageable pageable = new PageRequest(0, 1);
        List<LeaveApplication> LeaveApplicationlist = new ArrayList<>();
        int curTime = (int) (System.currentTimeMillis() / 1000L);
        LeaveApplication leaveApplication = new LeaveApplication("test" , ""+staff.getName() , 1456761600 , 1457625600 , curTime , "", 1, 1, ""+ staff.getDepartment(), staff.getManagerId(), staff.getManagerName(), 0 , "");
        LeaveApplicationlist.add(leaveApplication);
        when(applyController.leaveAppRepo.findByApplicantIdAndStatusInAndTypeInOrderByApplyTimeDesc(staff.getId(), Arrays.asList(2,3,4), Collections.singletonList(10), pageable)).thenReturn(LeaveApplicationlist);
        List<ResponseLeaveApplication> list = new ArrayList<>();
        for (LeaveApplication e : LeaveApplicationlist){
            list.add(new ResponseLeaveApplication(e));
        }
        ResponseListData responseData = new ResponseListData(1, 1, 100, staff.getId(), list);
        success2 = new ErrorReporter(0, "success");
        success2.setData(responseData);
        ErrorReporter actualReporter = applyController.overtimePublishList("test",1,1);
        String expected = gson.toJson(success2);
        String actual = gson.toJson(actualReporter);
        assertEquals(expected,actual);
    }

}