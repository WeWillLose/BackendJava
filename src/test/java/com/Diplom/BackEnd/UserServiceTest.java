package com.Diplom.BackEnd;

import com.Diplom.BackEnd.dto.LoginDTO;
import com.Diplom.BackEnd.model.User;
import com.Diplom.BackEnd.repo.UserRepo;
import com.Diplom.BackEnd.service.AuthService;
import com.Diplom.BackEnd.service.imp.AuthServiceImpl;
import com.Diplom.BackEnd.service.imp.UserServiceImpl;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import static org.assertj.core.api.Assertions.*;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class UserServiceTest {

    @Autowired
    private MockMvc mockMvc;

    @Mock
    UserRepo userRepo;

    @InjectMocks
    private UserServiceImpl userService;

    @Test
    public void when_logIn_authService_should_return_user(){
//        when(userRepo.authenticateUser(any(LoginDTO.class))).thenReturn(new User());
//
//        LoginDTO loginDTO = new LoginDTO("admin", "admin");
//        User user = authService.authenticateUser(loginDTO);
//
//        assertThat(user.getUsername()).isSameAs(loginDTO.getUsername());
    }
}
