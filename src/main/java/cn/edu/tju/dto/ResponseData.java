package cn.edu.tju.dto;


import cn.edu.tju.model.LeaveApplication;

import java.util.List;

public class ResponseData {



    private String username;






    protected ResponseData() {}

    // for login response
    public ResponseData(String username) {
        this.username = username;
    }



    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }


}
