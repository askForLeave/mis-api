package cn.edu.tju.controller;

import cn.edu.tju.dao.StaffRepo;
import cn.edu.tju.dto.ErrorReporter;
import cn.edu.tju.dto.LeaveReporter;
import cn.edu.tju.dto.ResponseData;
import cn.edu.tju.model.Staff;
import com.google.gson.Gson;
import org.joda.time.Days;
import org.joda.time.LocalDate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ReportController {

    @Autowired
    StaffRepo staffRepo;

    @RequestMapping("/leave/report/toFinancial")
    public ErrorReporter toFinancialSys(String staffId, int startTime, int endTime) {

        Staff curStaff = staffRepo.findOne(staffId);
        Gson gson = new Gson();
        int[] leaveDetail =  gson.fromJson(curStaff.getLeaveDetail(), int[].class);
        LocalDate initialDay = new LocalDate(2016,1,1);
        LocalDate startDay = new LocalDate(startTime*1000L);
        LocalDate endDay = new LocalDate(endTime*1000L);
        int startDayIndex = Days.daysBetween(initialDay, startDay).getDays();
        int endDayIndex = Days.daysBetween(initialDay, endDay).getDays();

        int total = 0;

        int normalWorkAndRest = 0;

        int annualLeave = 0;
        int marriageLeave = 0;
        int maternityPaternityLeave = 0;
        int sickLeave = 0;
        int bereavementLeave = 0;
        int officialLeave = 0;
        int matterLeave = 0;

        int overtimeInHoliday = 0;
        int overtimeInWeekends = 0;

        int other = 0;

        for (int i = startDayIndex; i <= endDayIndex; i++) {
            total ++;
            switch (leaveDetail[i]) {
                case 0: normalWorkAndRest ++; break;
                case 1: annualLeave ++; break;
                case 2: marriageLeave ++; break;
                case 3: maternityPaternityLeave ++; break;
                case 4: sickLeave ++; break;
                case 5: bereavementLeave ++; break;
                case 6: officialLeave ++; break;
                case 7: matterLeave ++; break;
                case 8: normalWorkAndRest ++; break;
                case 9: normalWorkAndRest ++; break;

                case 18: overtimeInHoliday ++; break;
                case 19: overtimeInWeekends ++; break;

                default: other ++; break;
            }
        }

        ResponseData data =  new LeaveReporter(total, normalWorkAndRest, annualLeave, marriageLeave, maternityPaternityLeave, sickLeave,
                bereavementLeave, officialLeave, matterLeave, overtimeInHoliday, overtimeInWeekends, other);
        return new ErrorReporter(0,"success", data);
    }

}
