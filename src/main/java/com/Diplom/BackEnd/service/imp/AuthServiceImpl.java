package com.Diplom.BackEnd.service.imp;

import com.Diplom.BackEnd.exception.MyException;
import com.Diplom.BackEnd.exception.impl.*;
import com.Diplom.BackEnd.model.Chairman_Slaves;
import com.Diplom.BackEnd.model.ERole;
import com.Diplom.BackEnd.model.Role;
import com.Diplom.BackEnd.model.User;
import com.Diplom.BackEnd.dto.LoginDTO;
import com.Diplom.BackEnd.dto.SignupDTO;
import com.Diplom.BackEnd.dto.UserDTO;
import com.Diplom.BackEnd.repo.Chairman_slavesRepo;
import com.Diplom.BackEnd.repo.RoleRepo;
import com.Diplom.BackEnd.repo.UserRepo;
import com.Diplom.BackEnd.service.AuthService;
import com.Diplom.BackEnd.service.Chairman_slavesService;
import com.Diplom.BackEnd.service.MapperToUserDTOService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;
@Service
@Slf4j
public class AuthServiceImpl implements AuthService {
    @Autowired
    AuthenticationManager authenticationManager;
    @Autowired
    UserRepo userRepository;
    @Autowired
    RoleRepo roleRepository;
    @Autowired
    Chairman_slavesRepo chairman_slavesRepo;
    @Autowired
    PasswordEncoder encoder;
    @Autowired
    MapperToUserDTOService mapperToUserDTOService;
    @Autowired
    ValidateUserServiceImpl validateUserService;


    public UserDTO authenticateUser(LoginDTO loginDTO) throws MyException {
        if(loginDTO == null){
            throw new NullPointerExceptionImpl("loginDTO must not be null");
        }
        try{
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginDTO.getUsername(), loginDTO.getPassword()));
            SecurityContextHolder.getContext().setAuthentication(authentication);
            User user = (User) authentication.getPrincipal();
            log.info("IN authenticateUser user with username: {}, password : {} wos authenticated",
                    loginDTO.getUsername(), loginDTO.getPassword());
            return mapperToUserDTOService.mapToUserDto(user);
        }catch (AuthenticationException e){
            log.error("IN authenticateUser auth for user with login: {}, and password: {} failed", loginDTO.getUsername(), loginDTO.getPassword());
            throw  new UserNotFoundExceptionImpl();
        }

    }

    @Override
    public UserDTO registerUser(SignupDTO signUpDTO)  throws MyException{
        log.info("IN registerUser signUpDTO: {}",signUpDTO);
        if(signUpDTO == null){
            throw new NullPointerExceptionImpl("signUpDTO must not be null");
        }
        if(signUpDTO.getUsername() == null){
            throw new ValidationErrorImpl("Логин должен быть не пустым");
        }
        if(signUpDTO.getPassword() == null){
            throw new ValidationErrorImpl("Пароль должен быть не пустым");
        }
        if(!validateUserService.validateUserPassword(signUpDTO.getPassword())){
            throw new ValidationErrorImpl("Пароль не прошел валидацию");
        }
        if(!validateUserService.validateUserUsername(signUpDTO.getUsername())){
            throw new ValidationErrorImpl("Логин не прошел валидацию");
        }
        if (userRepository.existsByUsername(signUpDTO.getUsername())) {
            log.error("IN registerUser user with username: {} already exists",signUpDTO.getUsername());
            throw new  UserAlreadyExistsExceptionImpl();

        }
        Set<Role> roles = new HashSet<>();
        Role userRole = roleRepository.findByName(ERole.ROLE_TEACHER);
        if (userRole == null){
            log.error("IN registerUser role {} not found",ERole.ROLE_TEACHER);
            throw new ServerErrorImpl();
        }
        roles.add(userRole);

        // Create new user's account
        User user = new User(
                signUpDTO.getUsername(),
                encoder.encode(signUpDTO.getPassword()),
                signUpDTO.getFirstName(),
                signUpDTO.getLastName(),
                signUpDTO.getPatronymic(),
                roles
        );
        User saved_user = userRepository.save(user);
        if(saved_user == null){
            log.error("IN registerUser user: {}. return from db null:{}",user,saved_user);
            throw new ServerErrorImpl();
        }
        log.info("IN registerUser user: {} wos created",saved_user);
        return mapperToUserDTOService.mapToUserDto(saved_user);
    }
}
