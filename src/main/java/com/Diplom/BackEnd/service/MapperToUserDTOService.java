package com.Diplom.BackEnd.service;

import com.Diplom.BackEnd.dto.UserDTO;
import com.Diplom.BackEnd.model.Chairman_Slaves;
import com.Diplom.BackEnd.model.User;

import javax.persistence.Enumerated;
import java.util.List;
import java.util.stream.Collectors;

public interface MapperToUserDTOService {

    UserDTO mapToUserDto(User user, Chairman_Slaves chairman_slaves);

    UserDTO mapToUserDto(User user);

    List<UserDTO> mapToUserDto(List<User> user);

    User mapToUser(UserDTO userDTO);


}
