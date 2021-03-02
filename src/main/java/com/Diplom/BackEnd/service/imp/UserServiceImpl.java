package com.Diplom.BackEnd.service.imp;

import com.Diplom.BackEnd.model.ERole;
import com.Diplom.BackEnd.model.Role;
import com.Diplom.BackEnd.model.User;
import com.Diplom.BackEnd.repo.RoleRepo;
import com.Diplom.BackEnd.repo.UserRepo;
import com.Diplom.BackEnd.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
@Service
@Slf4j
public class UserServiceImpl implements UserService{
    private final UserRepo userRepo;
    private final RoleRepo roleRepo;

    public UserServiceImpl(UserRepo userRepo, RoleRepo roleRepo) {
        this.userRepo = userRepo;
        this.roleRepo = roleRepo;
    }

    @Override
    public User register(User user) {
        Role roleUser =  roleRepo.findByName(ERole.ROLE_TEACHER);
        user.setRoles(Set.of(roleUser));

        User savedUser  = userRepo.save(user);

        log.info("In register - user {} successful registered", savedUser);
        return savedUser;
    }

    @Override
    public List<User> getAll() {
        List<User> users = userRepo.findAll();

        return users;
    }

    @Override
    public User findById(Long id) {
        User user = userRepo.findById(id).orElse(null);
        if (user != null){
            log.info("In findById - user {} found by id {}",user,id);
        }else {
            log.info("In findById - no user found by id {}",id);
        }

        return user;
    }

    @Override
    public User findByUsername(String username) {
        User user = userRepo.findByUsername(username);

        log.info("In findByUsername - user {} found by username {}",user,username);
        return user;
    }

    @Override
    public void delete(Long id) {
        userRepo.deleteById(id);

        log.info("In delete - user wos deleted by id {}",id);
    }


}
