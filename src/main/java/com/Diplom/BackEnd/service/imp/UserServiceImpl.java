package com.Diplom.BackEnd.service.imp;

import com.Diplom.BackEnd.dto.RoleDTO;
import com.Diplom.BackEnd.dto.UserDTO;
import com.Diplom.BackEnd.exception.MyException;
import com.Diplom.BackEnd.exception.Runtime.NullPointerExceptionImpl;
import com.Diplom.BackEnd.exception.impl.*;
import com.Diplom.BackEnd.model.*;
import com.Diplom.BackEnd.repo.RoleRepo;
import com.Diplom.BackEnd.repo.UserRepo;
import com.Diplom.BackEnd.service.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import static org.springframework.util.StringUtils.capitalize;

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
    private UserValidationService userValidationService;

    @Autowired
    private CanEditService canEditService;

    @Autowired
    private ChairmanService chairmanService;

    @Override
    public User findById(Long id) throws UserNotFoundExceptionImpl {
        if (id == null) {
            throw new NullPointerExceptionImpl("IN findById id must not be null");
        }
        User user = userRepo.findById(id).orElse(null);
        log.info("IN findById by {} found {}", id, user);
        return user;
    }

    @Override
    public List<User> findAll() throws MyException {
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
    public User findByUsername(String username) throws MyException {
        if (username == null) {
            throw new NullPointerExceptionImpl("username myst not be null or empty");
        }
        if (username.isBlank()) {
            throw new ValidationExceptionImpl("Логин должен быть не пустой");
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
        if (user == null) {
            throw new UserNotFoundExceptionImpl(id);
        }
        if (user.getRoles().contains(new Role(ERole.ROLE_ADMIN))) {
            throw new ValidationExceptionImpl("Админа нельзя удалить");
        }
        if (user.getId() == null) {
            throw new ValidationExceptionImpl("id должен быть не пустым");
        }
        if (!canEditService.canEdit(user)) {
            throw new ForbiddenExceptionImpl();
        }

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
            throw new UserNotFoundExceptionImpl(sourceUserId);
        }

        if (changedUser == null) {
            throw new NullPointerExceptionImpl("changedUser must not be null");
        }

        if (!canEditService.canEdit(user)) {
            throw new ForbiddenExceptionImpl();
        }
        if (changedUser.getUsername() != null && !changedUser.getUsername().isBlank()) {
            if(!userValidationService.validateUserUsername(changedUser.getUsername())){
                throw new ValidationExceptionImpl("username не прошел валидацию");
            }
            user.setUsername(changedUser.getUsername());
        }
        if (changedUser.getFirstName() != null && !changedUser.getFirstName().isBlank()) {
            if (!userValidationService.validateUserFirstName(changedUser.getFirstName())) {
                throw new ValidationExceptionImpl("Имя не прошло валидацию");
            }
            user.setFirstName(changedUser.getFirstName());
        }
        if (changedUser.getLastName() != null && !changedUser.getLastName().isBlank()) {
            if (!userValidationService.validateUserLastName(changedUser.getLastName())) {
                throw new ValidationExceptionImpl("Фамилия не прошло валидацию");
            }
            user.setLastName(changedUser.getLastName());
        }
        if (changedUser.getPatronymic() != null && !changedUser.getPatronymic().isBlank()) {
            if (!userValidationService.validateUserPatronymic(changedUser.getPatronymic())) {
                throw new ValidationExceptionImpl("Отчество не прошло валидацию");
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
            throw new UserNotFoundExceptionImpl(userId);
        }

        if (!userValidationService.validateUserPassword(password)) {
            throw new ValidationExceptionImpl("Пароль не прошел валидацию");
        }

        if (!canEditService.canEdit(user)) {
            throw new ForbiddenExceptionImpl();
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
            throw new NullPointerExceptionImpl("username must not be null");
        }
        return userRepo.existsByUsername(username);
    }

    @Override
    public User setRoles(Long id, List<RoleDTO> roles) {

        User user = findById(id);

        if (user == null) {
            throw new UserNotFoundExceptionImpl(id);
        }
        if (roles == null) {
            throw new NullPointerExceptionImpl("roles is null");
        }
        if (!canEditService.canEditOnlyAdmin((User) SecurityContextHolder.getContext().getAuthentication().getPrincipal())) {
            throw new ForbiddenExceptionImpl();
        }
        if (roles.contains(new RoleDTO(ERole.ROLE_ADMIN))) {
            throw new ValidationExceptionImpl("Нельзя назначать админа");
        }
        for (Role role : user.getRoles()) {
            if (role.getName().equals(ERole.ROLE_ADMIN)) {
                throw new ValidationExceptionImpl("Нельзя изменять роли админа");
            }
        }
        HashSet<Role> user_roles = new HashSet<>();
        roles.forEach(t -> {
                    try {
                        user_roles.add(roleRepo.findByName(ERole.valueOf(t.getName())));
                    } catch (IllegalArgumentException e) {
                        throw new BadRequestExceptionImpl(String.format("Роль - %s не существует", t.getName()));
                    }

                }
        );
        user.setRoles(user_roles);
        return userRepo.save(user);
    }

    @Override
    public User setChairman(Long followerId, Long chairmanId) {
        return chairmanService.setChairman(followerId, chairmanId);
    }

    @Override
    public User setChairman(Long slaveId, UserDTO chairmanDTO) {
        return chairmanService.setChairman(slaveId,chairmanDTO.getId());
    }

    @Override
    public List<User> findChairmans() {
        return chairmanService.findChairmans();
    }

    @Override
    public String getShortFIO(User user) {
        if(user == null){
            throw  new NullPointerExceptionImpl("IN getFIO user is null");
        }
        StringBuilder fio = new StringBuilder();
        if(user.getLastName() !=null && !user.getLastName().isBlank()){
            fio.append(capitalize(user.getLastName()));
        }
        if(user.getFirstName() !=null && !user.getFirstName().isBlank()){
            fio.append(String.format(" %.1s.",user.getFirstName()));
        }
        if(user.getPatronymic() !=null && !user.getPatronymic().isBlank()){
            fio.append(String.format(" %.1s.",user.getPatronymic()));
        }

        return fio.toString();
    }

    @Override
    public String getFIO(User user) {
        if(user == null){
            throw  new NullPointerExceptionImpl("IN getFIO user is null");
        }
        StringBuilder fio = new StringBuilder();
        if(user.getLastName() !=null && !user.getLastName().isBlank()){
            fio.append(capitalize(user.getLastName()));
        }
        if(user.getFirstName() !=null && !user.getFirstName().isBlank()){
            fio.append(String.format(" %s.",capitalize(user.getFirstName())));
        }
        if(user.getPatronymic() !=null && !user.getPatronymic().isBlank()){
            fio.append(String.format(" %s.",capitalize(user.getPatronymic())));
        }

        return fio.toString();
    }


}
