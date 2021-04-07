package com.Diplom.BackEnd.config;

import com.Diplom.BackEnd.model.ERole;
import com.Diplom.BackEnd.model.Role;
import com.Diplom.BackEnd.model.User;
import com.Diplom.BackEnd.repo.UserRepo;
import com.Diplom.BackEnd.service.UserService;
import com.Diplom.BackEnd.service.imp.UserServiceImpl;
import org.mockito.Mockito;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.Set;

import static org.mockito.Mockito.when;


@TestConfiguration
public class SpringSecurityWebAuxTestConfig {
}