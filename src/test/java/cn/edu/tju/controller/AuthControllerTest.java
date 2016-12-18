package cn.edu.tju.controller;

import cn.edu.tju.dto.ErrorReporter;
import cn.edu.tju.model.User;
import cn.edu.tju.service.LoginService;
import com.google.gson.Gson;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import javax.servlet.http.HttpSession;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;


public class AuthControllerTest {
    private AuthController authController;
    private Gson gson;
    private ErrorReporter nologin = new ErrorReporter(100, "index page: not log in");
    private ErrorReporter success = new ErrorReporter(100, "index page: hello test");
    @Before
    public void setUp() throws Exception {
        authController = new AuthController();
        gson = new Gson();
    }

    @After
    public void tearDown() throws Exception {

    }

    @Test
    public void indexTest1() throws Exception {
        authController.loginService = mock(LoginService.class);
        when(authController.loginService.isLogin()).thenReturn(false);
        ErrorReporter actualReporter = authController.index("test","test");
        String expected = gson.toJson(nologin);
        String actual = gson.toJson(actualReporter);
        assertEquals(expected,actual);
    }

    @Test
    public void indexTest2() throws Exception {
        authController.loginService = mock(LoginService.class);
        when(authController.loginService.isLogin()).thenReturn(true);
        HttpSession httpSession = mock(HttpSession.class);
        when(authController.loginService.getHttpSession()).thenReturn(httpSession);
        User user = new User("test","test");
        when(httpSession.getAttribute("user")).thenReturn(user);
        ErrorReporter actualReporter = authController.index("test","test");
        String expected = gson.toJson(success);
        String actual = gson.toJson(actualReporter);
        assertEquals(expected,actual);
    }

}