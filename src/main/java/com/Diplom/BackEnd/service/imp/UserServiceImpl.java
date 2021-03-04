package com.Diplom.BackEnd.service.imp;

import com.Diplom.BackEnd.dto.UserDTO;
import com.Diplom.BackEnd.exception.MyException;
import com.Diplom.BackEnd.exception.impl.BadRequestImpl;
import com.Diplom.BackEnd.exception.impl.UserNotFoundExceptionImpl;
import com.Diplom.BackEnd.model.Chairman_Slaves;
import com.Diplom.BackEnd.model.ERole;
import com.Diplom.BackEnd.model.Role;
import com.Diplom.BackEnd.model.User;
import com.Diplom.BackEnd.repo.RoleRepo;
import com.Diplom.BackEnd.repo.UserRepo;
import com.Diplom.BackEnd.service.Chairman_slavesService;
import com.Diplom.BackEnd.service.MapperToUserDTOService;
import com.Diplom.BackEnd.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.mapping.Collection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class UserServiceImpl implements UserService{
    @Autowired
    private UserRepo userRepo;
    @Autowired
    private RoleRepo roleRepo;
    @Autowired
    private MapperToUserDTOService mapperToUserDTOService;
    @Autowired
    private Chairman_slavesService chairman_slavesService;
    @Autowired
    PasswordEncoder encoder;



    @Override
    public List<User> getAll() throws MyException {
        List<User> users = userRepo.findAllByRolesNotContains(roleRepo.findByName(ERole.ROLE_ADMIN));
        return  users;
    }

    @Override
    public User findById(Long id) throws MyException{
        User user = userRepo.findById(id).orElseThrow(UserNotFoundExceptionImpl::new);
        log.info("IN findById by {} found {}",id,user);
        return user;
    }

    @Override
    public User findByUsername(String username) throws MyException {
        User user = userRepo.findByUsername(username);
        if(user == null){
            throw new UserNotFoundExceptionImpl();
        }
        return user;
    }

    @Override
    public void delete(Long id) throws MyException{
        if(id == null){
            throw new NullPointerException("id must not be null");
        }
        User user = userRepo.findById(id).orElseThrow(UserNotFoundExceptionImpl::new);
        delete(user);
    }

    @Override
    public void delete(User user) throws MyException {
        if(user == null){
            throw new NullPointerException("user must not be null");
        }
        if(user.getRoles().contains(new Role(ERole.ROLE_ADMIN))){
            throw new BadRequestImpl("Админа нельзя удалить");
        }
        userRepo.delete(user);
        log.info("In delete - user wos deleted by user {}",user);
    }

    public UserDTO getUserDtoByUserAndFindChairman_slaves(User user) throws NullPointerException{
        if(user == null){
           return null;
        }
        Chairman_Slaves chairman_slaves = chairman_slavesService.getChairman_slavesByUser(user);
        return mapperToUserDTOService.mapToUserDto(user,chairman_slaves);
    }

    @Override
    public List<UserDTO> getUserDtoByUserAndFindChairman_slaves(List<User> user) throws NullPointerException {
        if(user == null){
            return new ArrayList<>();
        }
       return user.stream()
                .map(this::getUserDtoByUserAndFindChairman_slaves)
                .collect(Collectors.toList());
    }

    @Override
    public User updateUserInfo(User sourceUser, User changedUser) throws MyException{
        if(sourceUser == null){
            throw new NullPointerException("sourceUser must not be null");
        }
        if(changedUser == null){
            throw new NullPointerException("changedUser must not be null");
        }
        if(changedUser.getUsername() != null && !changedUser.getUsername().isBlank()){
            sourceUser.setUsername(changedUser.getUsername());
        }
        if(changedUser.getFirstName() != null && !changedUser.getFirstName().isBlank()){
            sourceUser.setFirstName(changedUser.getFirstName());
        }
        if(changedUser.getLastName() != null && !changedUser.getLastName().isBlank()){
            sourceUser.setLastName(changedUser.getLastName());
        }
        if(changedUser.getPatronymic() != null &&!changedUser.getPatronymic().isBlank() ){
            sourceUser.setPatronymic(changedUser.getPatronymic());
        }

        return userRepo.save(sourceUser);
    }

    @Override
    public User updateUserInfo(Long id, User user) throws MyException{
        if(user == null){
            throw new NullPointerException("userDTO must not be null");
        }
        User byId = userRepo.findById(id).orElseThrow(UserNotFoundExceptionImpl::new);

        return updateUserInfo(byId,user);
    }
    @Override
    public User updateUserInfo(Long id, UserDTO userDTO) throws MyException{
        if(userDTO == null){
            throw new NullPointerException("userDTO must not be null");
        }
       return updateUserInfo(id,mapperToUserDTOService.mapToUser(userDTO));
    }




    @Override
    public User setPassword(Long id, String password) throws MyException {
        if(id== null){
            throw new NullPointerException("id must not be null");
        }
        if(password== null){
            throw new NullPointerException("password must not be null");
        }
        User byId = userRepo.findById(id).orElseThrow(UserNotFoundExceptionImpl::new);
        return setPassword(byId,password);
    }

    @Override
    public User setPassword(User user, String password) throws MyException {
        if(user== null){
            throw new NullPointerException("user must not be null");
        }
        if(password== null){
            throw new NullPointerException("password must not be null");
        }
        if(!password.isEmpty()){
            user.setPassword(encoder.encode(password));
        }
        return userRepo.save(user);
    }

    @Override
    public boolean existsById(Long id) throws NullPointerException {
        if(id == null){
            throw new NullPointerException("id must not be null");
        }
        return userRepo.existsById(id);
    }

    @Override
    public boolean existsByUsername(String username) throws NullPointerException {
        if(username == null){
            throw new NullPointerException("id must not be null");
        }
        return userRepo.existsByUsername(username);
    }


}
