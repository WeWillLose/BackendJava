package com.Diplom.BackEnd.service;

import com.Diplom.BackEnd.dto.UserDTO;
import com.Diplom.BackEnd.model.Chairman_Slaves;
import com.Diplom.BackEnd.model.User;

public interface MapperToUserDTOService {

    UserDTO mapToUserDto(User user, Chairman_Slaves chairman_slaves) throws NullPointerException;

    UserDTO mapToUserDto(User user) throws NullPointerException;


}
