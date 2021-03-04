package com.Diplom.BackEnd.service;

import com.Diplom.BackEnd.dto.UserDTO;
import com.Diplom.BackEnd.exception.MyException;
import com.Diplom.BackEnd.model.User;


import java.util.List;
import java.util.Set;

public interface UserService {
    List<User> getAll() throws MyException;
    User findById(Long id) throws MyException;
    User findByUsername(String username) throws MyException;
    void delete(Long id) throws MyException;
    void delete(User id) throws MyException;
    UserDTO getUserDtoByUserAndFindChairman_slaves(User user) throws NullPointerException;
    List<UserDTO> getUserDtoByUserAndFindChairman_slaves(List<User> user) throws NullPointerException;
    User updateUserInfo(Long id,User user) throws MyException;
    User updateUserInfo(Long id,UserDTO userDTO) throws MyException;
    User updateUserInfo(User sourceUser,User changedUser) throws MyException;
    User setPassword(Long id,String password) throws MyException;
    User setPassword(User user,String password) throws MyException;
    boolean existsById(Long id) throws NullPointerException;
    boolean existsByUsername(String username) throws NullPointerException;

}
