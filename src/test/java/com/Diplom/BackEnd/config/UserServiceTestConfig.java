package com.Diplom.BackEnd.config;

import com.Diplom.BackEnd.model.ERole;
import com.Diplom.BackEnd.model.Role;
import com.Diplom.BackEnd.model.User;
import com.Diplom.BackEnd.repo.UserRepo;
import com.Diplom.BackEnd.service.UserService;
import com.Diplom.BackEnd.service.imp.UserServiceImpl;
import org.mockito.Mockito;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Set;

import static org.mockito.Mockito.when;


public class UserServiceTestConfig {
    @Bean
    public UserService userService() {
        return new UserServiceImpl();
    }

    @Bean
    public UserRepo userRepo() {
        return Mockito.mock(UserRepo.class);
    }
}
