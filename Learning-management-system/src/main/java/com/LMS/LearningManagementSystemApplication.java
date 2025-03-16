	package com.LMS;

	import com.LMS.dto.Gender;
	import com.LMS.entity.Role;
	import com.LMS.entity.User;
	import com.LMS.repository.RoleRepo;
	import com.LMS.repository.UserRepo;
	import org.springframework.beans.factory.annotation.Autowired;
	import org.springframework.boot.CommandLineRunner;
	import org.springframework.boot.SpringApplication;
	import org.springframework.boot.autoconfigure.SpringBootApplication;

	import java.util.ArrayList;
	import java.util.List;

	@SpringBootApplication
	public class LearningManagementSystemApplication implements CommandLineRunner {
		@Autowired
		private UserRepo userRepo;

		@Autowired
		private RoleRepo roleRepo;

		public static void main(String[] args) {
			SpringApplication.run(LearningManagementSystemApplication.class, args);
		}


		@Override
		public void run(String... args) throws Exception {
			Role admin = roleRepo.findByName("ROLE_ADMIN").orElse(null);
			Role student = roleRepo.findByName("ROLE_STUDENT").orElse(null);
			Role teacher = roleRepo.findByName("ROLE_TEACHER").orElse(null);

			if (admin == null) {
				admin = new Role();
				admin.setName("ROLE_ADMIN");
				roleRepo.save(admin);
			}

			if (student == null) {
				student = new Role();
				student.setName("ROLE_STUDENT");
				roleRepo.save(student);
			}

			if (teacher == null) {
				teacher = new Role();
				teacher.setName("ROLE_TEACHER");
				roleRepo.save(teacher);
			}

			User user = userRepo.findByEmail("yash32860@gmail.com").orElse(null);
			if (user == null) {
				user = new User();
				user.setFullName("Yash Sharma");
				user.setGender(Gender.MALE);
				user.setEmail("yash32860@gmail.com");
				user.setPassword("yash");
				user.setMobile_no("9131747285");
				user.setEnrollment_no("MCA1234CS123");

				List<Role> roles = new ArrayList<>();
				roles.add(admin);
				roles.add(student);
				roles.add(teacher);
				user.setRoles(List.of(student));
				userRepo.save(user);
				System.out.println("User is created successfully");
			} else {
				System.out.println("User already exists");
			}
		}



	}
