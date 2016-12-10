package cn.edu.tju.controller;

import cn.edu.tju.dto.ErrorReporter;
import cn.edu.tju.service.LoginService;

import cn.edu.tju.dto.ResponseData;
import cn.edu.tju.model.LeaveApplication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
public class AuthController {

    @RequestMapping(value = "/",method = RequestMethod.POST)
    public ErrorReporter index(String username, String password){

        return new ErrorReporter(0, "success");
    }

    @Autowired
    private LoginService loginService;

    @RequestMapping(value = "/login",method = RequestMethod.POST)
    public ErrorReporter doLogin(String username, String password){
        return loginService.login(username, password);
    }

    @RequestMapping(value = "/reg",method = RequestMethod.POST)
    public ErrorReporter doReg(String username, String password){
        return loginService.reg(username, password);
    }

}
