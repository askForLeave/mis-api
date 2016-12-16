package cn.edu.tju.service;

import cn.edu.tju.dao.UserRepo;
import cn.edu.tju.model.User;
import cn.edu.tju.dto.ErrorReporter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;
import java.util.Collection;
import java.util.Iterator;

@Service
public class LoginService {

    @Autowired
    protected UserRepo userRepo;

    @Autowired
    protected HttpSession httpSession;

    public ErrorReporter login(String username, String password){
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        if (userRepo.exists(username)) {
            User user = userRepo.findOne(username);
            if (passwordEncoder.matches(password, user.getPassword())) {
                httpSession.setAttribute("user", user);
                return new ErrorReporter(0, "success");
            }else {
                return new ErrorReporter(1, "password error");
            }
        } else {
            return new ErrorReporter(2, "no account");
        }
    }

    public ErrorReporter reg(String username, String password){
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        password = passwordEncoder.encode(password);
        if ( !userRepo.exists(username) ) {
            try {
                User user = new User(username,password);
                userRepo.save(user);
                return new ErrorReporter(0, "success");
            } catch (Exception ex) {
                ex.printStackTrace();
                return new ErrorReporter(-1, "unknown error");
            }
        } else {
            return new ErrorReporter(3, "duplication error");
        }
    }

    public boolean isLogin(){
        if (httpSession.getAttribute("user")!=null){
            return true;
        }else {
            return false;
        }
    }

    public ErrorReporter logout() {
        if (httpSession.getAttribute("user") == null){
            return  new ErrorReporter(-1, "not login");
        }
        httpSession.setAttribute("user", null);
        return new ErrorReporter(0, "logout success");
    }

    public HttpSession getHttpSession() {
        return httpSession;
    }

    public void setHttpSession(HttpSession httpSession) {
        this.httpSession = httpSession;
    }
}
