package cn.edu.tju.dto;


import cn.edu.tju.dao.LeaveAppRepo;
import cn.edu.tju.model.LeaveApplication;

import java.util.List;

public class ResponseData {

    private int page;
    private int pageSize;
    private int total;
    private String username;

    private List<LeaveApplication> LeaveAppList;

    protected ResponseData() {}

    public ResponseData(String username) {
        this.username = username;
    }

    public ResponseData(int page, int pageSize, int total, List<LeaveApplication> leaveAppList) {
        this.page = page;
        this.pageSize = pageSize;
        this.total = total;
        LeaveAppList = leaveAppList;
    }

    public ResponseData(int page, int pageSize, int total, String username, List<LeaveApplication> leaveAppList) {
        this.page = page;
        this.pageSize = pageSize;
        this.total = total;
        this.username = username;
        LeaveAppList = leaveAppList;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public List<LeaveApplication> getLeaveAppList() {
        return LeaveAppList;
    }

    public void setLeaveAppList(List<LeaveApplication> leaveAppList) {
        LeaveAppList = leaveAppList;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
