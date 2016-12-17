package cn.edu.tju.dao;

import cn.edu.tju.model.LeaveApplication;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;


public interface LeaveAppRepo extends CrudRepository<LeaveApplication, Integer>, PagingAndSortingRepository<LeaveApplication, Integer> {

    public List<LeaveApplication> findByApplicantIdAndStatusOrderByApplyTimeDesc(String applicantId, int status, Pageable pageable);

    public List<LeaveApplication> findByApplicantIdAndStatusInOrderByApplyTimeDesc(String applicantId, List<Integer> statusList, Pageable pageable);

    public int countByApplicantIdAndStatus(String applicantId, int status);

    public int countByApplicantIdAndStatusIn(String applicantId, List<Integer> statusList);

    public List<LeaveApplication> findByManagerIdAndStatusOrderByApplyTimeDesc(String managerId, int status, Pageable pageable);

    public List<LeaveApplication> findByManagerIdAndStatusInOrderByApplyTimeDesc(String managerId, List<Integer> stl, Pageable pageable);

    public int countByManagerIdAndStatus(String managerId, int status);

    public int countByManagerIdAndStatusIn(String mansgerId, List<Integer> status);

}
