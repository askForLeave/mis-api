package cn.edu.tju.dao;

import cn.edu.tju.model.LeaveApplication;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;


public interface LeaveAppRepo extends CrudRepository<LeaveApplication, Integer>, PagingAndSortingRepository<LeaveApplication, Integer> {
    public List<LeaveApplication> findByApplicantIdAndStatusOrderByIdDesc(String applicantId, int status, Pageable pageable);

    public int countByApplicantIdAndStatus(String applicantId, int status);
}
