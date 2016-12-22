package cn.edu.tju;
import cn.edu.tju.dao.LeaveAppRepo;
import cn.edu.tju.dao.StaffRepo;
import cn.edu.tju.dao.UserRepo;
import cn.edu.tju.model.LeaveApplication;
import cn.edu.tju.model.Staff;
import cn.edu.tju.model.User;
import org.joda.time.DateTime;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import com.google.gson.Gson;

@SpringBootApplication
public class AskForLeaveApplication {

	public static void main(String[] args) {
		SpringApplication.run(AskForLeaveApplication.class, args);
	}

	@Bean
	public CommandLineRunner demo(UserRepo userRepo, StaffRepo staffRepo, LeaveAppRepo leaveAppRepo) {
		return (args) -> {

			BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
			String password = passwordEncoder.encode("123456");
			userRepo.save(new User("Jack", password));
			userRepo.save(new User("Alice", password));

			int[] leaveDetail = new int[3660];
			leaveDetail[0] = 0;	// leaveDetail[0] is the day 2016-01-01

			for (int i = 2; i < leaveDetail.length; i += 7) {
				leaveDetail[i] = 9;
				leaveDetail[i-1] = 9;
			}
			if ((leaveDetail.length - 1) % 7 == 2)	leaveDetail[leaveDetail.length - 1] = 9;

			leaveDetail[5] = 1;
			leaveDetail[6] = 1;

			Gson gson = new Gson();
			String leaveDetailJS = gson.toJson(leaveDetail);

			staffRepo.save(new Staff("Jack", "Jack", 1,12, 10, "dev", "Alice", "Alice", "" + leaveDetailJS));
			staffRepo.save(new Staff("Alice", "Alice", 2,12, 10, "dev", "Bob", "Bob", "" + leaveDetailJS));

		};
	}
}
