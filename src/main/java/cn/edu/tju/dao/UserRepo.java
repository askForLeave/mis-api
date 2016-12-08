package cn.edu.tju.dao;

import cn.edu.tju.model.User;
import org.springframework.data.repository.CrudRepository;


public interface UserRepo extends CrudRepository<User, String>{
}
