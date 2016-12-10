package cn.edu.tju;
import cn.edu.tju.dao.LeaveAppRepo;
import cn.edu.tju.dao.StaffRepo;
import cn.edu.tju.dao.UserRepo;
import cn.edu.tju.model.LeaveApplication;
import cn.edu.tju.model.Staff;
import cn.edu.tju.model.User;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class AskForLeaveApplication {

	public static void main(String[] args) {
		SpringApplication.run(AskForLeaveApplication.class, args);
	}

	@Bean
	public CommandLineRunner demo(UserRepo userRepo, StaffRepo staffRepo, LeaveAppRepo leaveAppRepo) {
		return (args) -> {

//			userRepo.save(new User("Jacky", "123456"));
//			userRepo.save(new User("Alice", "123456"));
//
//			staffRepo.save(new Staff("Jacky", "Jacky", 15, 5, "dev", "Alice"));

			leaveAppRepo.save(new LeaveApplication("Jack" , "Jacky" , 123456789 , 234567890 , 1234567890 , "be ill" , 1 , 2 , "Alice" , "Alice" , 123456789 , "approved"));
			leaveAppRepo.save(new LeaveApplication("Jack" , "Jacky" , 123456789 , 234567890 , 1234567890 , "be ill" , 1 , 2 , "Alice" , "Alice" , 123456789 , "approved"));
			leaveAppRepo.save(new LeaveApplication("Jack" , "Jacky" , 123456789 , 234567890 , 1234567890 , "be ill" , 1 , 2 , "Alice" , "Alice" , 123456789 , "approved"));


		};
	}
}
