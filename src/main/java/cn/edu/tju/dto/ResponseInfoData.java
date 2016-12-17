package cn.edu.tju.dto;


public class ResponseInfoData extends ResponseData{
    private String username;
    private String name;
    private String managerId;
    private String manager;
    private String department;
    private int annualTotal;
    private int annualLeft;


    public ResponseInfoData() {}

    public ResponseInfoData(String username, String name, String managerId, String manager, String department, int annualTotal, int annualLeft) {
        this.username = username;
        this.name = name;
        this.managerId = managerId;
        this.manager = manager;
        this.department = department;
        this.annualTotal = annualTotal;
        this.annualLeft = annualLeft;
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
