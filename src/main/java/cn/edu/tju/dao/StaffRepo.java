package cn.edu.tju.dao;

import cn.edu.tju.model.Staff;
import org.springframework.data.repository.CrudRepository;


public interface StaffRepo extends CrudRepository<Staff, String> {
}
