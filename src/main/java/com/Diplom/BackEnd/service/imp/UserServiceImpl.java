package com.Diplom.BackEnd.service.imp;

import com.Diplom.BackEnd.dto.UserDTO;
import com.Diplom.BackEnd.exception.MyException;
import com.Diplom.BackEnd.exception.impl.*;
import com.Diplom.BackEnd.model.*;
import com.Diplom.BackEnd.repo.RoleRepo;
import com.Diplom.BackEnd.repo.UserRepo;
import com.Diplom.BackEnd.service.CanEditService;
import com.Diplom.BackEnd.service.UserDTOMapperService;
import com.Diplom.BackEnd.service.UserService;
import com.Diplom.BackEnd.service.ValidateUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class UserServiceImpl implements UserService{
    @Autowired
    private UserRepo userRepo;
    @Autowired
    private RoleRepo roleRepo;
    @Autowired
    private UserDTOMapperService userDTOMapperService;

    @Autowired
    private PasswordEncoder encoder;
    @Autowired
    private ValidateUserService validateUserService;

    @Autowired
    private CanEditService canEditService;

    @Override
    public List<User> getAll() throws MyException {
        List<User> users = userRepo.findAllByRolesNotContains(roleRepo.findByName(ERole.ROLE_ADMIN));
        return  users;
    }

    @Override
    public User findById(Long id) throws MyException{
        if(id == null){
            throw new NullPointerExceptionImpl("id must not be null");
        }
        User user = userRepo.findById(id).orElse(null);
        log.info("IN findById by {} found {}",id,user);
        return user;
    }

    @Override
    public User findByUsername(String username) throws MyException {
        if(username == null){
            throw new NullPointerExceptionImpl("username myst not be null or empty");
        }
        if(username.isBlank()){
            throw new ValidationErrorImpl("Логин должен быть не пустой");
        }
        User user = userRepo.findByUsername(username);
        if(user == null){
            throw new UserNotFoundExceptionImpl();
        }
        return user;
    }

    @Override
    public void delete(Long id) throws MyException {
        if(id == null){
            throw new NullPointerExceptionImpl("id must not be null");
        }
        User user = this.findById(id);
        if(user.getRoles().contains(new Role(ERole.ROLE_ADMIN))){
            throw new BadRequestImpl("Админа нельзя удалить");
        }
        if(user.getId() == null){
            throw new ValidationErrorImpl("id должен быть не пустым");
        }
        if(!this.existsById(user.getId())){
            throw new UserNotFoundExceptionImpl();
        }
        if(!canEditService.canEdit(user)){
            throw new ForbiddenErrorImpl();
        }
        userRepo.delete(user);
        log.info("In delete - user wos deleted by user {}",user);
    }


    @Override
    public User updateUserInfo(Long sourceUserId, UserDTO changedUserDTO) throws MyException{
        return updateUserInfo(sourceUserId, userDTOMapperService.mapToUser(changedUserDTO));
    }
    
    @Override
    public User updateUserInfo(Long sourceUserId, User changedUser) throws MyException{
        if(sourceUserId == null){
            throw new NullPointerExceptionImpl("sourceUser must not be null");
        }
        User user = this.findById(sourceUserId);

        if(user == null){
            throw new UserNotFoundExceptionImpl();
        }

        if(changedUser == null){
            throw new NullPointerExceptionImpl("changedUser must not be null");
        }

        if(!canEditService.canEdit(user)){
            throw new ForbiddenErrorImpl();
        }
        if(changedUser.getUsername() != null && !changedUser.getUsername().isBlank()){
            user.setUsername(changedUser.getUsername());
        }
        if(changedUser.getFirstName() != null && !changedUser.getFirstName().isBlank()){
            if(!validateUserService.validateUserFirstName(changedUser.getFirstName())){
                throw new ValidationErrorImpl("Имя не прошло валидацию");
            }
            user.setFirstName(changedUser.getFirstName());
        }
        if(changedUser.getLastName() != null && !changedUser.getLastName().isBlank()){
            if(!validateUserService.validateUserLastName(changedUser.getLastName())){
                throw new ValidationErrorImpl("Фамилия не прошло валидацию");
            }
            user.setLastName(changedUser.getLastName());
        }
        if(changedUser.getPatronymic() != null && !changedUser.getPatronymic().isBlank() ){
            if(!validateUserService.validateUserPatronymic(changedUser.getPatronymic())){
                throw new ValidationErrorImpl("Отчество не прошло валидацию");
            }
            user.setPatronymic(changedUser.getPatronymic());
        }
        return userRepo.save(user);
    }

    @Override
    public User setPassword(Long userId, String password) throws MyException {
        if(userId== null){
            throw new NullPointerExceptionImpl("user must not be null");
        }
        User user = findById(userId);
        if(user == null){
            throw new UserNotFoundExceptionImpl();
        }

        if(!validateUserService.validateUserPassword(password)){
            throw new ValidationErrorImpl("Пароль не прошел валидацию");
        }

        if(!canEditService.canEdit(user)){
            throw new ForbiddenErrorImpl();
        }
        user.setPassword(encoder.encode(password));
        return userRepo.save(user);
    }

    @Override
    public boolean existsById(Long id) throws NullPointerExceptionImpl {
        if(id == null){
            throw new NullPointerExceptionImpl("id must not be null");
        }
        return userRepo.existsById(id);
    }

    @Override
    public boolean existsByUsername(String username) throws NullPointerExceptionImpl {
        if(username == null){
            throw new NullPointerExceptionImpl("id must not be null");
        }
        return userRepo.existsByUsername(username);
    }


}
