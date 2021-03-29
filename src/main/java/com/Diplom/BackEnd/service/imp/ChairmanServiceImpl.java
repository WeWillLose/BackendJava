package com.Diplom.BackEnd.service.imp;

import com.Diplom.BackEnd.dto.UserDTO;
import com.Diplom.BackEnd.exception.Runtime.NullPointerExceptionImpl;
import com.Diplom.BackEnd.exception.impl.UserNotFoundExceptionImpl;
import com.Diplom.BackEnd.exception.impl.ValidationExceptionImpl;
import com.Diplom.BackEnd.model.ERole;
import com.Diplom.BackEnd.model.User;
import com.Diplom.BackEnd.repo.RoleRepo;
import com.Diplom.BackEnd.repo.UserRepo;
import com.Diplom.BackEnd.service.ChairmanService;
import com.Diplom.BackEnd.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class ChairmanServiceImpl implements ChairmanService {
    @Autowired
    private UserService userService;
    @Autowired
    private UserRepo userRepo;
    @Autowired
    private RoleRepo roleRepo;
    @Override
    public User setChairman(Long followerId, Long chairmanId) {
        if (followerId == null) {
            throw new NullPointerExceptionImpl("IN setChairman followerId is null");
        }
        if (chairmanId == null) {
            throw new NullPointerExceptionImpl("IN setChairman chairmanId is null");
        }
        User follower = userService.findById(followerId);
        if (follower == null) {
            throw new UserNotFoundExceptionImpl(followerId);
        }
        User chairman = userService.findById(chairmanId);
        if (chairman == null) {
            throw new UserNotFoundExceptionImpl(chairmanId);
        }
        if(!chairman.getRoles().contains(roleRepo.findByName(ERole.ROLE_CHAIRMAN))){
            throw new ValidationExceptionImpl("У пользователя нет роли председатель");
        }
        follower.setChairman(chairman);
        log.info("IN setChairman follower: {}, chairman: {}",follower,chairman);
        return userRepo.save(follower);
    }

    @Override
    public User setChairman(Long slaveId, UserDTO chairmanDTO) {
        return setChairman(slaveId,chairmanDTO.getId());
    }

    @Override
    public List<User> findChairmans() {
        return userRepo.findAllByRolesContains(roleRepo.findByName(ERole.ROLE_CHAIRMAN));
    }
}
