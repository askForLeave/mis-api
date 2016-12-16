package cn.edu.tju.model;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Column;

@Entity
public class Staff {

    @Id
    private String id;

    private String name;
    private int sex;    // 1 for man, 2 for woman

    private int annualTotal;
    private int annualLeft;

    private String department;
    private String managerId;
    private String managerName;

    /**
     * @Column(columnDefinition = "TEXT") -> text in mysql
     * @Column(columnDefinition = "LONG VARCHAR") -> mediumtext in mysql
     *
     * leaveDetail is a JsonString of an int[400], array[0] will be the year,
     * and array[i] will be mark of the i-th day of the year.
     *
     * */
    @Column(length = 100000)      // varchar(100000) in my sql, array[4000]
    private String leaveDetail;

    protected Staff() {}

    public Staff(String id, String name, int sex, int annualTotal, int annualLeft, String department, String managerId, String managerName, String leaveDetail) {
        this.id = id;
        this.name = name;
        this.sex = sex;
        this.annualTotal = annualTotal;
        this.annualLeft = annualLeft;
        this.department = department;
        this.managerId = managerId;
        this.managerName = managerName;
        this.leaveDetail = leaveDetail;
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

    public int getSex() {
        return sex;
    }

    public void setSex(int sex) {
        this.sex = sex;
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

    public String getLeaveDetail() {
        return leaveDetail;
    }

    public void setLeaveDetail(String leaveDetail) {
        this.leaveDetail = leaveDetail;
    }

    public String getManagerName() {
        return managerName;
    }

    public void setManagerName(String managerName) {
        this.managerName = managerName;
    }

}
