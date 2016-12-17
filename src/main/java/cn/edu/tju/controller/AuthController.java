package cn.edu.tju.controller;

import cn.edu.tju.dto.ErrorReporter;
import cn.edu.tju.service.LoginService;
import cn.edu.tju.model.User;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class AuthController {

    @Autowired
    protected LoginService loginService;

    @RequestMapping(value = "/",method = RequestMethod.POST)
    public ErrorReporter index(String username, String password){

        String msg;
        if(loginService.isLogin()) {
            msg = "hello " + ((User)loginService.getHttpSession().getAttribute("user")).getId();
        } else {
            msg = "not log in";
        }

        return new ErrorReporter(0, "index page: " + msg);
    }

    @RequestMapping(value = "/leave/auth/login",method = RequestMethod.POST)
    public ErrorReporter doLogin(String username, String password){
        return loginService.login(username, password);
    }

    @RequestMapping(value = "/leave/auth/reg",method = RequestMethod.POST)
    public ErrorReporter doReg(String username, String password){
        return loginService.reg(username, password);
    }

    @RequestMapping(value = "/leave/auth/logout",method = RequestMethod.POST)
    public ErrorReporter doLogout(String username){
        return loginService.logout();
    }

}
