package com.webshop.project3;

import java.util.Arrays;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import com.webshop.project3.entity.Role;
import com.webshop.project3.entity.User;
import com.webshop.project3.repository.RoleRepo;
import com.webshop.project3.repository.UserRepo;

import lombok.extern.slf4j.Slf4j;


// khi khoi dong sever se mac dinh tao mot user va mot role (dump data)
@Component
@Slf4j
public class DemoData implements ApplicationRunner {

	@Autowired
	RoleRepo roleRepo;
	
	@Autowired
	UserRepo userRepo;

	@Override
	public void run(ApplicationArguments args) throws Exception {
		// insert data demo into data
		Role role = new Role();
		role.setName("ROLE_ADMIN");
		if (roleRepo.findByName(role.getName()) == null) {
			try {
				roleRepo.save(role);
				log.info("INSERT DUMP");
				User user = new User();
				user.setUsername("sysadmin");
				user.setPassword(new BCryptPasswordEncoder().encode("123456"));
				user.setName("SYS ADMIN");
				user.setEmail("admin@jmaster.io");
				user.setBirthdate(new Date());
				user.setRoles(Arrays.asList(role));
				
				userRepo.save(user);
			} catch (Exception e) {
				//todo
			}
		}
	}
}
