package cn.edu.tju.dto;


import cn.edu.tju.dao.LeaveAppRepo;
import cn.edu.tju.model.LeaveApplication;

import java.util.List;

public class ResponseData {

    private int page;
    private int pageSize;
    private int total;

    private String username;

    private String name;
    private String managerId;
    private String manager;
    private String department;
    private int annualTotal;
    private int annualLeft;


    private List<LeaveApplication> LeaveAppList;

    protected ResponseData() {}

    // for login response
    public ResponseData(String username) {
        this.username = username;
    }

    // for info response
    public ResponseData(String username, String name, String managerId, String manager, String department, int annualTotal, int annualLeft) {
        this.username = username;
        this.name = name;
        this.managerId = managerId;
        this.manager = manager;
        this.department = department;
        this.annualTotal = annualTotal;
        this.annualLeft = annualLeft;
    }

    // for list leave_applications response
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getManagerId() {
        return managerId;
    }

    public void setManagerId(String managerId) {
        this.managerId = managerId;
    }

    public String getManager() {
        return manager;
    }

    public void setManager(String manager) {
        this.manager = manager;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public int getAnnualTotal() {
        return annualTotal;
    }

    public void setAnnualTotal(int annualTotal) {
        this.annualTotal = annualTotal;
    }

    public int getAnnualLeft() {
        return annualLeft;
    }

    public void setAnnualLeft(int annualLeft) {
        this.annualLeft = annualLeft;
    }
}
