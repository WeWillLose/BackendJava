package com.Diplom.BackEnd.service.imp;

import com.Diplom.BackEnd.exception.MyException;
import com.Diplom.BackEnd.exception.impl.ServerErrorImpl;
import com.Diplom.BackEnd.exception.impl.UserAlreadyExistsExceptionImpl;
import com.Diplom.BackEnd.exception.impl.UserNotFoundExceptionImpl;
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
    Chairman_slavesService chairman_slavesService;


    public UserDTO authenticateUser(LoginDTO loginDTO) throws MyException {
        try{
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginDTO.getUsername(), loginDTO.getPassword()));

            SecurityContextHolder.getContext().setAuthentication(authentication);

            User user = (User) authentication.getPrincipal();
            Chairman_Slaves chairman_slaves = chairman_slavesService.getChairman_slavesBySlave(user);
            log.info("IN authenticateUser user with username: {}, password : {} wos authenticated",
                    loginDTO.getUsername(), loginDTO.getPassword());
            if(chairman_slaves !=null){
                return mapperToUserDTOService.mapToUserDto(user,chairman_slaves);
            }
            return mapperToUserDTOService.mapToUserDto(user);
        }catch (AuthenticationException e){
            log.error("IN authenticateUser auth for user with login: {}, and password: {} failed", loginDTO.getUsername(), loginDTO.getPassword());
            throw  new UserNotFoundExceptionImpl();
        }

    }

    @Override
    public UserDTO registerUser(SignupDTO signUpDTO)  throws MyException{
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
