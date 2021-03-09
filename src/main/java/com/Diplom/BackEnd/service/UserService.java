package com.Diplom.BackEnd.service;

import com.Diplom.BackEnd.dto.UserDTO;
import com.Diplom.BackEnd.exception.MyException;
import com.Diplom.BackEnd.model.User;

import java.util.List;

public interface UserService {
    List<User> getAll() throws MyException;
    User findById(Long id) throws MyException;
    User findByUsername(String username) throws MyException;
    void delete(Long id) throws MyException;
    User updateUserInfo(Long sourceUserId,User changedUser) throws MyException;
    User updateUserInfo(Long sourceUserId,UserDTO changedUser) throws MyException;
    User setPassword(Long userId,String password) throws MyException;
    boolean existsById(Long id) throws NullPointerException;
    boolean existsByUsername(String username) throws NullPointerException;

}
