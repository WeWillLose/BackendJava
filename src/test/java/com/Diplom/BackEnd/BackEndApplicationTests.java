package com.Diplom.BackEnd;

import com.Diplom.BackEnd.dto.UserDTO;
import com.Diplom.BackEnd.model.Chairman_Slaves;
import com.Diplom.BackEnd.model.ERole;
import com.Diplom.BackEnd.model.User;
import com.Diplom.BackEnd.repo.Chairman_slavesRepo;
import com.Diplom.BackEnd.repo.RoleRepo;
import com.Diplom.BackEnd.repo.UserRepo;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFTable;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;

@SpringBootTest
class BackEndApplicationTests {
	@Autowired
	UserRepo userRepo;

	@Autowired
	Chairman_slavesRepo chairman_slavesRepo;

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
		userRepo.save(user1);
		userRepo.save(user2);
		userRepo.save(user3);
	}

	void setSlaves(){
		User user1 = userRepo.findByUsername("user1");
		User user2 = userRepo.findByUsername("user2");
		Chairman_Slaves chairman_slaves = new Chairman_Slaves();
		chairman_slaves.setChairman(user1);
		chairman_slaves.setSlaves(Collections.singleton(user2));
		Chairman_Slaves save = chairman_slavesRepo.save(chairman_slaves);
		System.out.println(save.getChairman().getUsername());
		save.getSlaves().forEach(t-> System.out.println(t.getUsername()));
	}

	void test(){
		User user1 = userRepo.findByUsername("user1");
		User user2 = userRepo.findByUsername("user2");
		Chairman_Slaves bySlavesContaining = chairman_slavesRepo.findBySlavesContains(user2);
		System.out.println(bySlavesContaining.getChairman().getUsername());
	}

	void testError(){
		roleRepo.findByName(ERole.ROLE_TEACHER);
	}
	void testtest() throws JsonProcessingException {
		ObjectMapper objectMapper = new ObjectMapper();

		HashSet<Object> objects = new HashSet<>();
		UserDTO userDTO = new UserDTO();
		userDTO.setUsername("u1");
		UserDTO userDTO1 = new UserDTO();
		userDTO1.setUsername("u2");
		objects.add(userDTO);
		objects.add(userDTO1);
		System.out.println(objectMapper.writeValueAsString(objects));
	}

	public void generateReport() throws IOException {
		XWPFDocument docx = new XWPFDocument(new FileInputStream("template_report.docx"));
		List<XWPFTable> tables = docx.getTables();
		tables.forEach(table -> {
			System.out.println(table.getRow(0));
		});
	}

	@Test
	void contextLoads() throws IOException {
		generateReport();
	}

}
