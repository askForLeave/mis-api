package cn.edu.tju.dao;

import cn.edu.tju.model.LeaveApplication;
import org.springframework.data.repository.CrudRepository;


public interface LeaveAppRepo extends CrudRepository<LeaveApplication, Integer> {
}
