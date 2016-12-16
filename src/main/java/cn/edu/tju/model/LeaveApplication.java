package cn.edu.tju.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class LeaveApplication {

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private int id;

    private String applicantId;
    private String applicantName;
    private long startTime;
    private long endTime;
    private long applyTime;
    private String applyReason;
    private int type;
    private int status;

    private String applicantDepartment;
    private String managerId;
    private String managerName;
    private long reviewTime;
    private String reviewReason;

    protected LeaveApplication () {}

    public LeaveApplication(String applicantId, String applicantName, long startTime, long endTime, long applyTime, String applyReason, int type, int status, String applicantDepartment, String managerId, String managerName, long reviewTime, String reviewReason) {
        this.applicantId = applicantId;
        this.applicantName = applicantName;
        this.startTime = startTime;
        this.endTime = endTime;
        this.applyTime = applyTime;
        this.applyReason = applyReason;
        this.type = type;
        this.status = status;
        this.applicantDepartment = applicantDepartment;
        this.managerId = managerId;
        this.managerName = managerName;
        this.reviewTime = reviewTime;
        this.reviewReason = reviewReason;
    }

    public int getId() {
        return id;
    }

    public String getApplicantId() {
        return applicantId;
    }

    public void setApplicantId(String applicantId) {
        this.applicantId = applicantId;
    }

    public String getApplicantName() {
        return applicantName;
    }

    public void setApplicantName(String applicantName) {
        this.applicantName = applicantName;
    }

    public long getStartTime() {
        return startTime;
    }

    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }

    public long getEndTime() {
        return endTime;
    }

    public void setEndTime(long endTime) {
        this.endTime = endTime;
    }

    public long getApplyTime() {
        return applyTime;
    }

    public void setApplyTime(long applyTime) {
        this.applyTime = applyTime;
    }

    public String getApplyReason() {
        return applyReason;
    }

    public void setApplyReason(String applyReason) {
        this.applyReason = applyReason;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getApplicantDepartment() {
        return applicantDepartment;
    }

    public void setApplicantDepartment(String applicantDepartment) {
        this.applicantDepartment = applicantDepartment;
    }

    public String getManagerId() {
        return managerId;
    }

    public void setManagerId(String managerId) {
        this.managerId = managerId;
    }

    public String getManagerName() {
        return managerName;
    }

    public void setManagerName(String managerName) {
        this.managerName = managerName;
    }

    public long getReviewTime() {
        return reviewTime;
    }

    public void setReviewTime(long reviewTime) {
        this.reviewTime = reviewTime;
    }

    public String getReviewReason() {
        return reviewReason;
    }

    public void setReviewReason(String reviewReason) {
        this.reviewReason = reviewReason;
    }


}
