package com.Diplom.BackEnd.config;

import com.Diplom.BackEnd.repo.RoleRepo;
import com.Diplom.BackEnd.repo.UserRepo;
import com.Diplom.BackEnd.service.CanEditService;
import com.Diplom.BackEnd.service.UserMapperService;
import com.Diplom.BackEnd.service.ValidateUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.mockito.Mockito.mock;

@Configuration
public class UserserviceConfig {

    @Bean
    public RoleRepo roleRepo(){
        return mock(RoleRepo.class);
    }
    @Bean
    public UserRepo userRepo(){
        return mock(UserRepo.class);
    }
    @Bean
    public CanEditService canEditService(){
        return mock(CanEditService.class);
    }

    @Autowired
    private UserMapperService userMapperService;

    @Autowired
    private PasswordEncoder encoder;

    @Autowired
    private ValidateUserService validateUserService;
}
