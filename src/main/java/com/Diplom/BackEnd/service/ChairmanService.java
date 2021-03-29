package com.Diplom.BackEnd.service;

import com.Diplom.BackEnd.dto.UserDTO;
import com.Diplom.BackEnd.exception.Runtime.NullPointerExceptionImpl;
import com.Diplom.BackEnd.exception.impl.UserNotFoundExceptionImpl;
import com.Diplom.BackEnd.exception.impl.ValidationExceptionImpl;
import com.Diplom.BackEnd.model.ERole;
import com.Diplom.BackEnd.model.User;

import java.util.List;

public interface ChairmanService {
    
    User setChairman(Long followerId, Long chairmanId);

    User setChairman(Long slaveId, UserDTO chairmanDTO);

    List<User> findChairmans() ;
}
