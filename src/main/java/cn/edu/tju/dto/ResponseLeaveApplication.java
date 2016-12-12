package cn.edu.tju.dto;


import cn.edu.tju.model.LeaveApplication;

public class ResponseLeaveApplication {

    private int id;         // 申请主键

    private String applyUserName;   // 申请人姓名
    private long applyTime;         // unix时间戳，申请时间
    private long startTime;         // 开始时间
    private long endTime;           // 结束时间
    private String reason;          // 请假原因
    private int type;               // 请假类型

    private int status;             // 审核状态
    private String department;      // 申请人所在部门
    private String reviewer;        // 审核人
    private String reviewReason;    // 审核原因,驳回原因(若驳回)
    private long reviewTime;        // 审核时间

    protected ResponseLeaveApplication() {}

    public ResponseLeaveApplication(LeaveApplication la) {
        this.id = la.getId();
        this.applyUserName = la.getApplicantName();
        this.applyTime = la.getApplyTime();
        this.startTime = la.getStartTime();
        this.endTime = la.getEndTime();
        this.reason = la.getApplyReason();
        this.type = la.getType();
        this.status = la.getStatus();
        this.department = la.getApplicantDepartment();
        this.reviewer = la.getManagerName();
        this.reviewReason = la.getReviewReason();
        this.reviewTime = la.getReviewTime();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getApplyUserName() {
        return applyUserName;
    }

    public void setApplyUserName(String applyUserName) {
        this.applyUserName = applyUserName;
    }

    public long getApplyTime() {
        return applyTime;
    }

    public void setApplyTime(long applyTime) {
        this.applyTime = applyTime;
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

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
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

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getReviewer() {
        return reviewer;
    }

    public void setReviewer(String reviewer) {
        this.reviewer = reviewer;
    }

    public String getReviewReason() {
        return reviewReason;
    }

    public void setReviewReason(String reviewReason) {
        this.reviewReason = reviewReason;
    }

    public long getReviewTime() {
        return reviewTime;
    }

    public void setReviewTime(long reviewTime) {
        this.reviewTime = reviewTime;
    }
}
