package com.Diplom.BackEnd.service;

import com.Diplom.BackEnd.dto.RoleDTO;
import com.Diplom.BackEnd.dto.UserDTO;
import com.Diplom.BackEnd.exception.MyException;
import com.Diplom.BackEnd.model.User;

import java.util.List;

public interface UserService {
    List<User> findAll() throws MyException;
    User findById(Long id) throws MyException;
    User findByUsername(String username) throws MyException;
    void delete(Long id) throws MyException;
    User updateUserInfo(Long sourceUserId,User changedUser) throws MyException;
    User updateUserInfo(Long sourceUserId,UserDTO changedUser) throws MyException;
    User setPassword(Long userId,String password) throws MyException;
    boolean existsById(Long id) throws NullPointerException;
    boolean existsByUsername(String username) throws NullPointerException;
    List<User> findFollowers(Long id) throws MyException;

    User setRoles(Long id, List<RoleDTO> roles);

    User setChairman(Long slaveId, Long chairmanId);

    User setChairman(Long slaveId, UserDTO chairmanDTO);

    List<User> findChairmans();

    String getShortFIO(User user);

    String getFIO(User user);
}
