package com.Diplom.BackEnd.service.imp;

import com.Diplom.BackEnd.dto.RoleDTO;
import com.Diplom.BackEnd.dto.UserDTO;
import com.Diplom.BackEnd.exception.MyException;
import com.Diplom.BackEnd.exception.impl.*;
import com.Diplom.BackEnd.model.*;
import com.Diplom.BackEnd.repo.RoleRepo;
import com.Diplom.BackEnd.repo.UserRepo;
import com.Diplom.BackEnd.service.CanEditService;
import com.Diplom.BackEnd.service.UserMapperService;
import com.Diplom.BackEnd.service.UserService;
import com.Diplom.BackEnd.service.ValidateUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;

@Service
@Slf4j
public class UserServiceImpl implements UserService {
    @Autowired
    private UserRepo userRepo;
    @Autowired
    private RoleRepo roleRepo;
    @Autowired
    private UserMapperService userMapperService;

    @Autowired
    private PasswordEncoder encoder;
    @Autowired
    private ValidateUserService validateUserService;

    @Autowired
    private CanEditService canEditService;

    @Override
    public List<User> getAll() throws MyException {
        List<User> users = userRepo.findAllByRolesNotContains(roleRepo.findByName(ERole.ROLE_ADMIN));
        return users;
    }

    @Override
    public List<User> findFollowers(Long id) throws MyException {
        if (id == null) {
            throw new NullPointerExceptionImpl("id must not be null");
        }
        if (!existsById(id)) {
            throw new UserNotFoundExceptionImpl();
        }
        List<User> followers = userRepo.findAllByChairmanId(id);
        log.info("IN findFollowers by {} found {}", id, followers);
        return followers;
    }

    @Override
    public User findById(Long id) throws MyException {
        if (id == null) {
            throw new NullPointerExceptionImpl("id must not be null");
        }
        User user = userRepo.findById(id).orElse(null);
        log.info("IN findById by {} found {}", id, user);
        return user;
    }

    @Override
    public User findByUsername(String username) throws MyException {
        if (username == null) {
            throw new NullPointerExceptionImpl("username myst not be null or empty");
        }
        if (username.isBlank()) {
            throw new ValidationErrorImpl("Логин должен быть не пустой");
        }
        User user = userRepo.findByUsername(username);
        if (user == null) {
            throw new UserNotFoundExceptionImpl();
        }
        return user;
    }

    @Override
    public void delete(Long id) throws MyException {
        if (id == null) {
            throw new NullPointerExceptionImpl("id must not be null");
        }
        User user = this.findById(id);
        if (user.getRoles().contains(new Role(ERole.ROLE_ADMIN))) {
            throw new BadRequestImpl("Админа нельзя удалить");
        }
        if (user.getId() == null) {
            throw new ValidationErrorImpl("id должен быть не пустым");
        }
        if (!this.existsById(user.getId())) {
            throw new UserNotFoundExceptionImpl();
        }
        if (!canEditService.canEdit(user)) {
            throw new ForbiddenErrorImpl();
        }
//        List<User> allByChairmanId = userRepo.findAllByChairmanId(user.getId());
//        allByChairmanId.forEach(t->{
//            t.setChairman(null);
//        });

        userRepo.delete(user);
        log.info("In delete - user wos deleted by user {}", user);
    }


    @Override
    public User updateUserInfo(Long sourceUserId, UserDTO changedUserDTO) throws MyException {
        return updateUserInfo(sourceUserId, userMapperService.mapToUser(changedUserDTO));
    }

    @Override
    public User updateUserInfo(Long sourceUserId, User changedUser) throws MyException {
        if (sourceUserId == null) {
            throw new NullPointerExceptionImpl("sourceUser must not be null");
        }
        User user = this.findById(sourceUserId);

        if (user == null) {
            throw new UserNotFoundExceptionImpl();
        }

        if (changedUser == null) {
            throw new NullPointerExceptionImpl("changedUser must not be null");
        }

        if (!canEditService.canEdit(user)) {
            throw new ForbiddenErrorImpl();
        }
        if (changedUser.getUsername() != null && !changedUser.getUsername().isBlank()) {
            user.setUsername(changedUser.getUsername());
        }
        if (changedUser.getFirstName() != null && !changedUser.getFirstName().isBlank()) {
            if (!validateUserService.validateUserFirstName(changedUser.getFirstName())) {
                throw new ValidationErrorImpl("Имя не прошло валидацию");
            }
            user.setFirstName(changedUser.getFirstName());
        }
        if (changedUser.getLastName() != null && !changedUser.getLastName().isBlank()) {
            if (!validateUserService.validateUserLastName(changedUser.getLastName())) {
                throw new ValidationErrorImpl("Фамилия не прошло валидацию");
            }
            user.setLastName(changedUser.getLastName());
        }
        if (changedUser.getPatronymic() != null && !changedUser.getPatronymic().isBlank()) {
            if (!validateUserService.validateUserPatronymic(changedUser.getPatronymic())) {
                throw new ValidationErrorImpl("Отчество не прошло валидацию");
            }
            user.setPatronymic(changedUser.getPatronymic());
        }
        return userRepo.save(user);
    }

    @Override
    public User setPassword(Long userId, String password) throws MyException {
        if (userId == null) {
            throw new NullPointerExceptionImpl("user must not be null");
        }
        User user = findById(userId);
        if (user == null) {
            throw new UserNotFoundExceptionImpl();
        }

        if (!validateUserService.validateUserPassword(password)) {
            throw new ValidationErrorImpl("Пароль не прошел валидацию");
        }

        if (!canEditService.canEdit(user)) {
            throw new ForbiddenErrorImpl();
        }
        user.setPassword(encoder.encode(password));
        return userRepo.save(user);
    }

    @Override
    public boolean existsById(Long id) throws NullPointerExceptionImpl {
        if (id == null) {
            throw new NullPointerExceptionImpl("id must not be null");
        }
        return userRepo.existsById(id);
    }

    @Override
    public boolean existsByUsername(String username) throws NullPointerExceptionImpl {
        if (username == null) {
            throw new NullPointerExceptionImpl("id must not be null");
        }
        return userRepo.existsByUsername(username);
    }

    @Override
    public User setRoles(Long id, List<RoleDTO> roles) {

        User user = findById(id);
        if (user == null) {
            throw new UserNotFoundExceptionImpl();
        }
        if (roles == null) {
            return user;
        }
        if (!canEditService.canEditOnlyAdmin()) {
            throw new ForbiddenErrorImpl();
        }
        if (roles.contains(new RoleDTO(ERole.ROLE_ADMIN))) {
            throw new ValidationErrorImpl("Нельзя назначать админа");
        }
        for (Role role : user.getRoles()) {
            if (role.getName().equals(ERole.ROLE_ADMIN)) {
                throw new ValidationErrorImpl("Нельзя изменять роли админа");
            }
        }
        HashSet<Role> user_roles = new HashSet<>();
        roles.forEach(t -> {
                    try {
                        user_roles.add(roleRepo.findByName(ERole.valueOf(t.getName())));
                    } catch (IllegalArgumentException e) {
                        throw new BadRequestImpl(String.format("Роль - %s не существует", t.getName()));
                    }

                }
        );
        user.setRoles(user_roles);
        return userRepo.save(user);
    }

    @Override
    public User setChairman(Long followerId, Long chairmanId) {
        if (followerId == null) {
            throw new NullPointerExceptionImpl("IN setChairman followerId is null");
        }
        if (chairmanId == null) {
            throw new NullPointerExceptionImpl("IN setChairman chairmanId is null");
        }
        User follower = userRepo.findById(followerId).orElse(null);
        if (follower == null) {
            throw new UserNotFoundExceptionImpl(followerId);
        }
        User chairman = userRepo.findById(chairmanId).orElse(null);
        if (chairman == null) {
            throw new UserNotFoundExceptionImpl(chairmanId);
        }
        follower.setChairman(chairman);
        return userRepo.save(follower);
    }

    @Override
    public User setChairman(Long slaveId, UserDTO chairmanDTO) {
        if(chairmanDTO == null){
            throw new NullPointerExceptionImpl("IN setChairman chairmanDTO is null");
        }
        return setChairman(slaveId,chairmanDTO.getId());
    }

    @Override
    public List<User> findChairmans() {
        return userRepo.findAllByRolesContains(roleRepo.findByName(ERole.ROLE_CHAIRMAN));
    }


}
