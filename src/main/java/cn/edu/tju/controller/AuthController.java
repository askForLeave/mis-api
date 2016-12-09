package cn.edu.tju.controller;

import cn.edu.tju.dto.ErrorReporter;
import cn.edu.tju.dto.ResponseData;
import cn.edu.tju.model.LeaveApplication;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
public class AuthController {

    @RequestMapping(value = "/",method = RequestMethod.POST)
    public ErrorReporter doLogin(String username, String password){

        LeaveApplication la = new LeaveApplication("Jacky" , "Jacky" , 123456789 , 234567890 , 1234567890 , "be ill" , 1 , 2 , "Alice" , "Alice" , 123456789 , "approved");
        List<LeaveApplication> list = new ArrayList<LeaveApplication>();
        list.add(la);
        list.add(la);

        ResponseData data = new ResponseData(1,2,10, "Jacky", list);
        return new ErrorReporter(0, "success", data);
//        return data;
    }
}
