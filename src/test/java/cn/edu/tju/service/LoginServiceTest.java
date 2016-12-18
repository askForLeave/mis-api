package cn.edu.tju.service;

import cn.edu.tju.dao.UserRepo;
import cn.edu.tju.dto.ErrorReporter;
import cn.edu.tju.dto.ResponseNameData;
import cn.edu.tju.model.User;
import com.google.gson.Gson;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import javax.servlet.http.HttpSession;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class LoginServiceTest {
    private LoginService loginService;
    private Gson gson;
    private ErrorReporter success = new ErrorReporter(0, "success");
    private ErrorReporter success2;
    private ErrorReporter passworderror = new ErrorReporter(1, "password error");
    private ErrorReporter noaccount = new ErrorReporter(2, "no account");
    private ErrorReporter duplicationerror = new ErrorReporter(3, "duplication error");
    private ErrorReporter notlogin = new ErrorReporter(4, "not login");

    @Before
    public void setUp() throws Exception {
        loginService = new LoginService();
        gson =  new Gson();
    }

    @After
    public void tearDown() throws Exception {

    }

    @Test
    public void loginTest1() throws Exception {
        loginService.userRepo = mock(UserRepo.class);
        when(loginService.userRepo.exists("test")).thenReturn(false);
        ErrorReporter actualReporter = loginService.login("test","test");
        String expected = gson.toJson(noaccount);
        String actual = gson.toJson(actualReporter);
        assertEquals(expected,actual);
    }

    @Test
    public void loginTest2() throws Exception {
        loginService.userRepo = mock(UserRepo.class);
        when(loginService.userRepo.exists("test")).thenReturn(true);
        User user = new User("test","error");
        when(loginService.userRepo.findOne("test")).thenReturn(user);
        ErrorReporter actualReporter = loginService.login("test","test");
        String expected = gson.toJson(passworderror);
        String actual = gson.toJson(actualReporter);
        assertEquals(expected,actual);
    }

    @Test
    public void loginTest3() throws Exception {
        loginService.userRepo = mock(UserRepo.class);
        when(loginService.userRepo.exists("test")).thenReturn(true);
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        User user = new User("test",passwordEncoder.encode("test"));
        when(loginService.userRepo.findOne("test")).thenReturn(user);
        loginService.httpSession = mock(HttpSession.class);
        success2 = new ErrorReporter(0, "success");
        success2.setData(new ResponseNameData("test"));
        ErrorReporter actualReporter = loginService.login("test","test");
        String expected = gson.toJson(success2);
        String actual = gson.toJson(actualReporter);
        assertEquals(expected,actual);
    }

    @Test
    public void regTest1() throws Exception {
        loginService.userRepo = mock(UserRepo.class);
        when(loginService.userRepo.exists("test")).thenReturn(true);
        ErrorReporter actualReporter = loginService.reg("test","test");
        String expected = gson.toJson(duplicationerror);
        String actual = gson.toJson(actualReporter);
        assertEquals(expected,actual);
    }

    @Test
    public void regTest2() throws Exception {
        loginService.userRepo = mock(UserRepo.class);
        when(loginService.userRepo.exists("test")).thenReturn(false);
        ErrorReporter actualReporter = loginService.reg("test","test");
        String expected = gson.toJson(success);
        String actual = gson.toJson(actualReporter);
        assertEquals(expected,actual);
    }


    @Test
    public void isLoginTest1() throws Exception {
        loginService.httpSession = mock(HttpSession.class);
        when(loginService.httpSession.getAttribute("user")).thenReturn(null);
        assertEquals(false,loginService.isLogin());
    }

    @Test
    public void isLoginTest2() throws Exception {
        loginService.httpSession = mock(HttpSession.class);
        when(loginService.httpSession.getAttribute("user")).thenReturn("test");
        assertEquals(true,loginService.isLogin());
    }

    @Test
    public void logoutTest1() throws Exception {
        loginService.httpSession = mock(HttpSession.class);
        when(loginService.httpSession.getAttribute("user")).thenReturn(null);
        ErrorReporter actualReporter = loginService.logout();
        String expected = gson.toJson(notlogin);
        String actual = gson.toJson(actualReporter);
        assertEquals(expected,actual);
    }

    @Test
    public void logoutTest2() throws Exception {
        loginService.httpSession = mock(HttpSession.class);
        when(loginService.httpSession.getAttribute("user")).thenReturn("test");
        ErrorReporter actualReporter = loginService.logout();
        String expected = gson.toJson(success);
        String actual = gson.toJson(actualReporter);
        assertEquals(expected,actual);
    }

}