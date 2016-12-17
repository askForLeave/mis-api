package cn.edu.tju.dto;

/**
 * Created by yongheng on 2016/12/16.
 */
public class LeaveReporter extends ResponseData{

    private int total;

    private int normalWorkAndRest;

    private int annualLeave;
    private int marriageLeave;
    private int maternityPaternityLeave;
    private int sickLeave;
    private int bereavementLeave;
    private int officialLeave;
    private int matterLeave;

    private int overtimeInHoliday;
    private int overtimeInWeekends;

    private int other;

    public LeaveReporter(int total, int normalWorkAndRest, int annualLeave, int marriageLeave, int maternityPaternityLeave, int sickLeave, int bereavementLeave, int officialLeave, int matterLeave, int overtimeInHoliday, int overtimeInWeekends, int other) {
        this.total = total;
        this.normalWorkAndRest = normalWorkAndRest;
        this.annualLeave = annualLeave;
        this.marriageLeave = marriageLeave;
        this.maternityPaternityLeave = maternityPaternityLeave;
        this.sickLeave = sickLeave;
        this.bereavementLeave = bereavementLeave;
        this.officialLeave = officialLeave;
        this.matterLeave = matterLeave;
        this.overtimeInHoliday = overtimeInHoliday;
        this.overtimeInWeekends = overtimeInWeekends;
        this.other = other;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public int getAnnualLeave() {
        return annualLeave;
    }

    public void setAnnualLeave(int annualLeave) {
        this.annualLeave = annualLeave;
    }

    public int getMarriageLeave() {
        return marriageLeave;
    }

    public void setMarriageLeave(int marriageLeave) {
        this.marriageLeave = marriageLeave;
    }

    public int getMaternityPaternityLeave() {
        return maternityPaternityLeave;
    }

    public void setMaternityPaternityLeave(int maternityPaternityLeave) {
        this.maternityPaternityLeave = maternityPaternityLeave;
    }

    public int getSickLeave() {
        return sickLeave;
    }

    public void setSickLeave(int sickLeave) {
        this.sickLeave = sickLeave;
    }

    public int getBereavementLeave() {
        return bereavementLeave;
    }

    public void setBereavementLeave(int bereavementLeave) {
        this.bereavementLeave = bereavementLeave;
    }

    public int getOfficialLeave() {
        return officialLeave;
    }

    public void setOfficialLeave(int officialLeave) {
        this.officialLeave = officialLeave;
    }

    public int getMatterLeave() {
        return matterLeave;
    }

    public void setMatterLeave(int matterLeave) {
        this.matterLeave = matterLeave;
    }

    public int getOvertimeInHoliday() {
        return overtimeInHoliday;
    }

    public void setOvertimeInHoliday(int overtimeInHoliday) {
        this.overtimeInHoliday = overtimeInHoliday;
    }

    public int getOvertimeInWeekends() {
        return overtimeInWeekends;
    }

    public void setOvertimeInWeekends(int overtimeInWeekends) {
        this.overtimeInWeekends = overtimeInWeekends;
    }

    public int getOther() {
        return other;
    }

    public void setOther(int other) {
        this.other = other;
    }

    public int getNormalWorkAndRest() {
        return normalWorkAndRest;
    }

    public void setNormalWorkAndRest(int normalWorkAndRest) {
        this.normalWorkAndRest = normalWorkAndRest;
    }
}
