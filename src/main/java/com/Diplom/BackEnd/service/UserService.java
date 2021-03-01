package com.Diplom.BackEnd.service;

import com.Diplom.BackEnd.model.User;


import java.util.List;
public interface UserService {
    User register(User user);
    List<User> getAll();
    User findById(Long id);
    User findByUsername(String username);
    void delete(Long id);


}
