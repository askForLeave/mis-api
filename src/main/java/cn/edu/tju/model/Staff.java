package cn.edu.tju.model;

import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class Staff {

    @Id
    private String id;

    private String name;

    private int annualTotal;
    private int annualLeft;

    private String department;
    private String managerId;

    private String leave_detail;

    protected Staff() {}

    public Staff(String id, String name, int annualTotal, int annualLeft, String department, String managerId) {
        this.id = id;
        this.name = name;
        this.annualTotal = annualTotal;
        this.annualLeft = annualLeft;
        this.department = department;
        this.managerId = managerId;
        this.leave_detail = "0100100010041040";//new char[400].toString();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getManagerId() {
        return managerId;
    }

    public void setManagerId(String managerId) {
        this.managerId = managerId;
    }

    public String getLeave_detail() {
        return leave_detail;
    }

    public void setLeave_detail(String leave_detail) {
        this.leave_detail = leave_detail;
    }
}
