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
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import javax.servlet.http.HttpSession;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockingDetails;
import static org.mockito.Mockito.when;


public class ReviewControllerTest {
    private ReviewController reviewController;
    private Gson gson;
    private ErrorReporter nologin = new ErrorReporter(4, "not login");
    private ErrorReporter unknowntype = new ErrorReporter(13, "unknown type");
    private ErrorReporter noexist = new ErrorReporter(19, "application id don't exist");
    private ErrorReporter noauthority = new ErrorReporter(23, "no authority for this leave application");
    private ErrorReporter nopublish = new ErrorReporter(24, "could not review this leave application, has reviewed or hasn't publish");
    private ErrorReporter wrongstatus = new ErrorReporter(25, "wrong status");
    private ErrorReporter success = new ErrorReporter(0, "success");
    private ErrorReporter success2;
    @Before
    public void setUp() throws Exception {
        reviewController = new ReviewController();
        gson = new Gson();
    }

    @After
    public void tearDown() throws Exception {

    }

    @Test
    public void todoListTest1() throws Exception {
        reviewController.loginService = mock(LoginService.class);
        when(reviewController.loginService.isLogin()).thenReturn(false);
        ErrorReporter actualReporter = reviewController.todoList("test",1,1);
        String expected = gson.toJson(nologin);
        String actual = gson.toJson(actualReporter);
        assertEquals(expected,actual);
    }

    @Test
    public void todoListTest2() throws Exception {
        reviewController.loginService = mock(LoginService.class);
        when(reviewController.loginService.isLogin()).thenReturn(true);
        reviewController.httpSession = mock(HttpSession.class);
        User user = new User("test","test");
        when((reviewController.httpSession.getAttribute("user"))).thenReturn(user);
        reviewController.staffRepo = mock(StaffRepo.class);
        int leaveditail[] = new int [400];
        for(int i=0;i<400;i++)leaveditail[i]=1;
        Staff staff = new Staff("test","test",1,20,20,"test","testM","testM",gson.toJson(leaveditail));
        when(reviewController.staffRepo.findOne(user.getId())).thenReturn(staff);
        reviewController.leaveAppRepo = mock(LeaveAppRepo.class);
        when(reviewController.leaveAppRepo.countByManagerIdAndStatus("test",2)).thenReturn(100);
        Pageable pageable = new PageRequest(0, 1);
        List<LeaveApplication> las = new ArrayList<>();
        int curTime = (int) (System.currentTimeMillis() / 1000L);
        LeaveApplication leaveApplication = new LeaveApplication("test" , ""+staff.getName() , 1456761600 , 1457625600 , curTime , "", 1, 1, ""+ staff.getDepartment(), staff.getManagerId(), staff.getManagerName(), 0 , "");
        las.add(leaveApplication);
        when(reviewController.leaveAppRepo.findByManagerIdAndStatusOrderByApplyTimeDesc(staff.getId(), 2, pageable)).thenReturn(las);
        List<ResponseLeaveApplication> list = new ArrayList<>();
        for (LeaveApplication e:las) {
            list.add(new ResponseLeaveApplication(e));
        }
        ResponseListData data = new ResponseListData(1, 1, 100, staff.getId(), list);
        success2 = new ErrorReporter(0, "success");
        success2.setData(data);
        ErrorReporter actualReporter = reviewController.todoList("test",1,1);
        String expected = gson.toJson(success2);
        String actual = gson.toJson(actualReporter);
        assertEquals(expected,actual);
    }

    @Test
    public void doneListTest1() throws Exception {
        reviewController.loginService = mock(LoginService.class);
        when(reviewController.loginService.isLogin()).thenReturn(false);
        ErrorReporter actualReporter = reviewController.doneList("test",1,1);
        String expected = gson.toJson(nologin);
        String actual = gson.toJson(actualReporter);
        assertEquals(expected,actual);
    }

    @Test
    public void doneListTest2() throws Exception {
        reviewController.loginService = mock(LoginService.class);
        when(reviewController.loginService.isLogin()).thenReturn(true);
        reviewController.httpSession = mock(HttpSession.class);
        User user = new User("test","test");
        when((reviewController.httpSession.getAttribute("user"))).thenReturn(user);
        reviewController.staffRepo = mock(StaffRepo.class);
        int leaveditail[] = new int [400];
        for(int i=0;i<400;i++)leaveditail[i]=1;
        Staff staff = new Staff("test","test",1,20,20,"test","testM","testM",gson.toJson(leaveditail));
        when(reviewController.staffRepo.findOne(user.getId())).thenReturn(staff);
        reviewController.leaveAppRepo = mock(LeaveAppRepo.class);
        when(reviewController.leaveAppRepo.countByManagerIdAndStatusIn("test",Arrays.asList(3,4))).thenReturn(100);
        Pageable pageable = new PageRequest(0, 1);
        List<LeaveApplication> las = new ArrayList<>();
        int curTime = (int) (System.currentTimeMillis() / 1000L);
        LeaveApplication leaveApplication = new LeaveApplication("test" , ""+staff.getName() , 1456761600 , 1457625600 , curTime , "", 1, 1, ""+ staff.getDepartment(), staff.getManagerId(), staff.getManagerName(), 0 , "");
        las.add(leaveApplication);
        when(reviewController.leaveAppRepo.findByManagerIdAndStatusInOrderByApplyTimeDesc(staff.getId(), Arrays.asList(3,4), pageable)).thenReturn(las);
        List<ResponseLeaveApplication> list = new ArrayList<>();
        for (LeaveApplication e:las) {
            list.add(new ResponseLeaveApplication(e));
        }
        ResponseListData data = new ResponseListData(1, 1, 100, staff.getId(), list);
        success2 = new ErrorReporter(0, "success");
        success2.setData(data);
        ErrorReporter actualReporter = reviewController.doneList("test",1,1);
        String expected = gson.toJson(success2);
        String actual = gson.toJson(actualReporter);
        assertEquals(expected,actual);
    }

    @Test
    public void actionTest1() throws Exception {
        reviewController.loginService = mock(LoginService.class);
        when(reviewController.loginService.isLogin()).thenReturn(false);
        ErrorReporter actualReporter = reviewController.action(1,3,"");
        String expected = gson.toJson(nologin);
        String actual = gson.toJson(actualReporter);
        assertEquals(expected,actual);
    }

    @Test
    public void actionTest2() throws Exception {
        reviewController.loginService = mock(LoginService.class);
        when(reviewController.loginService.isLogin()).thenReturn(true);
        reviewController.httpSession = mock(HttpSession.class);
        User user = new User("test","test");
        when((reviewController.httpSession.getAttribute("user"))).thenReturn(user);
        reviewController.staffRepo = mock(StaffRepo.class);
        int leaveditail[] = new int [400];
        for(int i=0;i<400;i++)leaveditail[i]=1;
        Staff staff = new Staff("test","test",1,20,20,"test","testM","testM",gson.toJson(leaveditail));
        when(reviewController.staffRepo.findOne(user.getId())).thenReturn(staff);
        reviewController.leaveAppRepo = mock(LeaveAppRepo.class);
        when(reviewController.leaveAppRepo.exists(1)).thenReturn(false);
        ErrorReporter actualReporter = reviewController.action(1,3,"");
        String expected = gson.toJson(noexist);
        String actual = gson.toJson(actualReporter);
        assertEquals(expected,actual);
    }

    @Test
    public void actionTest3() throws Exception {
        reviewController.loginService = mock(LoginService.class);
        when(reviewController.loginService.isLogin()).thenReturn(true);
        reviewController.httpSession = mock(HttpSession.class);
        User user = new User("test","test");
        when((reviewController.httpSession.getAttribute("user"))).thenReturn(user);
        reviewController.staffRepo = mock(StaffRepo.class);
        int leaveditail[] = new int [400];
        for(int i=0;i<400;i++)leaveditail[i]=1;
        Staff staff = new Staff("test","test",1,20,20,"test","testM","testM",gson.toJson(leaveditail));
        when(reviewController.staffRepo.findOne(user.getId())).thenReturn(staff);
        reviewController.leaveAppRepo = mock(LeaveAppRepo.class);
        when(reviewController.leaveAppRepo.exists(1)).thenReturn(true);
        int curTime = (int) (System.currentTimeMillis() / 1000L);
        LeaveApplication leaveApplication = new LeaveApplication("test" , ""+staff.getName() , 1456761600 , 1457625600 , curTime , "", 1, 1, ""+ staff.getDepartment(), staff.getManagerId(), staff.getManagerName(), 0 , "");
        when(reviewController.leaveAppRepo.findOne(1)).thenReturn(leaveApplication);
        ErrorReporter actualReporter = reviewController.action(1,3,"");
        String expected = gson.toJson(noauthority);
        String actual = gson.toJson(actualReporter);
        assertEquals(expected,actual);
    }

    @Test
    public void actionTest4() throws Exception {
        reviewController.loginService = mock(LoginService.class);
        when(reviewController.loginService.isLogin()).thenReturn(true);
        reviewController.httpSession = mock(HttpSession.class);
        User user = new User("testM","test");
        when((reviewController.httpSession.getAttribute("user"))).thenReturn(user);
        reviewController.staffRepo = mock(StaffRepo.class);
        int leaveditail[] = new int [400];
        for(int i=0;i<400;i++)leaveditail[i]=1;
        Staff staff = new Staff("testM","test",1,20,20,"test","testM","testM",gson.toJson(leaveditail));
        when(reviewController.staffRepo.findOne(user.getId())).thenReturn(staff);
        reviewController.leaveAppRepo = mock(LeaveAppRepo.class);
        when(reviewController.leaveAppRepo.exists(1)).thenReturn(true);
        int curTime = (int) (System.currentTimeMillis() / 1000L);
        LeaveApplication leaveApplication = new LeaveApplication("test" , ""+staff.getName() , 1456761600 , 1457625600 , curTime , "", 1, 1, ""+ staff.getDepartment(), staff.getManagerId(), staff.getManagerName(), 0 , "");
        when(reviewController.leaveAppRepo.findOne(1)).thenReturn(leaveApplication);
        ErrorReporter actualReporter = reviewController.action(1,3,"");
        String expected = gson.toJson(nopublish);
        String actual = gson.toJson(actualReporter);
        assertEquals(expected,actual);
    }

    @Test
    public void actionTest5() throws Exception {
        reviewController.loginService = mock(LoginService.class);
        when(reviewController.loginService.isLogin()).thenReturn(true);
        reviewController.httpSession = mock(HttpSession.class);
        User user = new User("testM","test");
        when((reviewController.httpSession.getAttribute("user"))).thenReturn(user);
        reviewController.staffRepo = mock(StaffRepo.class);
        int leaveditail[] = new int [400];
        for(int i=0;i<400;i++)leaveditail[i]=1;
        Staff staff = new Staff("testM","test",1,20,20,"test","testM","testM",gson.toJson(leaveditail));
        when(reviewController.staffRepo.findOne(user.getId())).thenReturn(staff);
        reviewController.leaveAppRepo = mock(LeaveAppRepo.class);
        when(reviewController.leaveAppRepo.exists(1)).thenReturn(true);
        int curTime = (int) (System.currentTimeMillis() / 1000L);
        LeaveApplication leaveApplication = new LeaveApplication("test" , ""+staff.getName() , 1456761600 , 1457625600 , curTime , "", 1, 2, ""+ staff.getDepartment(), staff.getManagerId(), staff.getManagerName(), 0 , "");
        when(reviewController.leaveAppRepo.findOne(1)).thenReturn(leaveApplication);
        ErrorReporter actualReporter = reviewController.action(1,2,"");
        String expected = gson.toJson(wrongstatus);
        String actual = gson.toJson(actualReporter);
        assertEquals(expected,actual);
    }

    @Test
    public void actionTest6() throws Exception {
        reviewController.loginService = mock(LoginService.class);
        when(reviewController.loginService.isLogin()).thenReturn(true);
        reviewController.httpSession = mock(HttpSession.class);
        User user = new User("testM","test");
        when((reviewController.httpSession.getAttribute("user"))).thenReturn(user);
        reviewController.staffRepo = mock(StaffRepo.class);
        int leaveditail[] = new int [400];
        for(int i=0;i<400;i++)leaveditail[i]=1;
        Staff staff = new Staff("testM","test",1,20,20,"test","testM","testM",gson.toJson(leaveditail));
        when(reviewController.staffRepo.findOne(user.getId())).thenReturn(staff);
        reviewController.leaveAppRepo = mock(LeaveAppRepo.class);
        when(reviewController.leaveAppRepo.exists(1)).thenReturn(true);
        int curTime = (int) (System.currentTimeMillis() / 1000L);
        LeaveApplication leaveApplication = new LeaveApplication("test" , ""+staff.getName() , 1456761600 , 1457625600 , curTime , "", 8, 2, ""+ staff.getDepartment(), staff.getManagerId(), staff.getManagerName(), 0 , "");
        when(reviewController.leaveAppRepo.findOne(1)).thenReturn(leaveApplication);
        Staff staff2 = new Staff("testM","test",1,20,20,"test","testM","testM",gson.toJson(leaveditail));
        when((reviewController.staffRepo.findOne(leaveApplication.getApplicantId()))).thenReturn(staff2);
        ErrorReporter actualReporter = reviewController.action(1,3,"");
        String expected = gson.toJson(unknowntype);
        String actual = gson.toJson(actualReporter);
        assertEquals(expected,actual);
    }

    @Test
    public void actionTest7() throws Exception {
        reviewController.loginService = mock(LoginService.class);
        when(reviewController.loginService.isLogin()).thenReturn(true);
        reviewController.httpSession = mock(HttpSession.class);
        User user = new User("testM","test");
        when((reviewController.httpSession.getAttribute("user"))).thenReturn(user);
        reviewController.staffRepo = mock(StaffRepo.class);
        int leaveditail[] = new int [400];
        for(int i=0;i<400;i++)leaveditail[i]=1;
        Staff staff = new Staff("testM","test",1,20,20,"test","testM","testM",gson.toJson(leaveditail));
        when(reviewController.staffRepo.findOne(user.getId())).thenReturn(staff);
        reviewController.leaveAppRepo = mock(LeaveAppRepo.class);
        when(reviewController.leaveAppRepo.exists(1)).thenReturn(true);
        int curTime = (int) (System.currentTimeMillis() / 1000L);
        LeaveApplication leaveApplication = new LeaveApplication("test" , ""+staff.getName() , 1456761600 , 1457625600 , curTime , "", 1, 2, ""+ staff.getDepartment(), staff.getManagerId(), staff.getManagerName(), 0 , "");
        when(reviewController.leaveAppRepo.findOne(1)).thenReturn(leaveApplication);
        Staff staff2 = new Staff("testM","test",1,20,20,"test","testM","testM",gson.toJson(leaveditail));
        when((reviewController.staffRepo.findOne(leaveApplication.getApplicantId()))).thenReturn(staff2);
        ErrorReporter actualReporter = reviewController.action(1,3,"");
        String expected = gson.toJson(success);
        String actual = gson.toJson(actualReporter);
        assertEquals(expected,actual);
    }

}