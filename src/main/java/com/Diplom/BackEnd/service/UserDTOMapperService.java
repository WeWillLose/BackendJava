package com.Diplom.BackEnd.service;

import com.Diplom.BackEnd.dto.UserDTO;
import com.Diplom.BackEnd.model.User;

import java.util.List;

public interface UserDTOMapperService {

//    UserDTO mapToUserDto(User user, Chairman_Slaves chairman_slaves);

    UserDTO mapToUserDto(User user);

    List<UserDTO> mapToUserDto(List<User> user);

    User mapToUser(UserDTO userDTO);


}
