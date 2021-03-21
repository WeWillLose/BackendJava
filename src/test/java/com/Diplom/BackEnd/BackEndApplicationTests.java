package com.Diplom.BackEnd;

import com.Diplom.BackEnd.model.ERole;
import com.Diplom.BackEnd.model.User;
import com.Diplom.BackEnd.repo.RoleRepo;
import com.Diplom.BackEnd.repo.UserRepo;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;


import java.io.IOException;

@SpringBootTest
class BackEndApplicationTests {
	@Autowired
	UserRepo userRepo;


	@Autowired
	RoleRepo roleRepo;

	void createUsers(){
		User user1 = new User();
		user1.setUsername("user1");
		user1.setPassword("p1");
		user1.setFirstName("f1");
		user1.setLastName("l1");
		user1.setPatronymic("patr1");
		User user2 = new User();
		user2.setUsername("user2");
		user2.setPassword("p2");
		user2.setFirstName("f2");
		user2.setLastName("l2");
		user2.setPatronymic("patr2");
		User user3 = new User();
		user3.setUsername("user3");
		user3.setPassword("p3");
		user3.setFirstName("f3");
		user3.setLastName("l3");
		user3.setPatronymic("patr3");
		System.out.println(userRepo.save(user1));
		System.out.println(userRepo.save(user2));
		System.out.println(userRepo.save(user3));
		userRepo.flush();
	}

//	void setSlaves(){
//		User user1 = userRepo.findByUsername("user1");
//		User user2 = userRepo.findByUsername("user2");
//		Chairman_Slaves chairman_slaves = new Chairman_Slaves();
//		chairman_slaves.setChairman(user1);
//		chairman_slaves.setSlaves(Collections.singleton(user2));
//		Chairman_Slaves save = chairman_slavesRepo.save(chairman_slaves);
//		System.out.println(save.getChairman().getUsername());
//		save.getSlaves().forEach(t-> System.out.println(t.getUsername()));
//	}
//
//	void test(){
//		User user1 = userRepo.findByUsername("user1");
//		User user2 = userRepo.findByUsername("user2");
//		Chairman_Slaves bySlavesContaining = chairman_slavesRepo.findBySlavesContains(user2);
//		System.out.println(bySlavesContaining.getChairman().getUsername());
//	}

	void testError(){
		roleRepo.findByName(ERole.ROLE_TEACHER);
	}

	void testtest() throws JsonProcessingException {
		User user1 = userRepo.findByUsername("user1");
		User user2 = userRepo.findByUsername("user2");
		user1.setChairman(user2);
		System.out.println(userRepo.save(user1));
		userRepo.flush();
	}

	@Test
	void contextLoads() throws IOException {
		createUsers();
		testtest();
	}

}
