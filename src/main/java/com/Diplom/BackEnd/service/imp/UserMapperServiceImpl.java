package com.Diplom.BackEnd.service.imp;

import com.Diplom.BackEnd.dto.RoleDTO;
import com.Diplom.BackEnd.dto.UserDTO;
import com.Diplom.BackEnd.exception.Runtime.NullPointerExceptionImpl;
import com.Diplom.BackEnd.model.ERole;
import com.Diplom.BackEnd.model.Role;
import com.Diplom.BackEnd.model.User;
import com.Diplom.BackEnd.service.UserMapperService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class UserMapperServiceImpl implements UserMapperService {

    @Override
    public UserDTO mapToUserDto(User user) throws NullPointerException{
        if(user == null){
            log.info("IN mapToUserDto user is null");
            return null;
        }
        UserDTO userDTO = new UserDTO();
        userDTO.setUsername(user.getUsername());
        userDTO.setFirstName(user.getFirstName());
        userDTO.setId(user.getId());
        userDTO.setLastName(user.getLastName());
        userDTO.setPatronymic(user.getPatronymic());
        if(user.getRoles()!=null){
            userDTO.setRoles(user.getRoles().stream().map(role-> new RoleDTO(role.getName())).collect(Collectors.toSet()));
        }
        if(user.getChairman()!=null){
            UserDTO chairman = new UserDTO();
            chairman.setFirstName(user.getChairman().getFirstName());
            chairman.setLastName(user.getChairman().getLastName());
            chairman.setPatronymic(user.getChairman().getPatronymic());
            chairman.setUsername(user.getChairman().getUsername());
            chairman.setId(user.getChairman().getId());
            userDTO.setChairman(chairman);
        }
        log.info("IN mapToUserDto mapped {} into {}",user,userDTO);
        return userDTO;
    }

    @Override
    public List<UserDTO> mapToUserDto(List<User> user) {
        if(user == null){
            return null;
        }
        return user.stream().map(this::mapToUserDto).collect(Collectors.toList());
    }

    @Override
    public User mapToUser(UserDTO userDTO) {
        if(userDTO == null){
            log.info("IN mapToUser userDTO is null");
            return null;
        }
        User user = new User();
        user.setUsername(userDTO.getUsername());
        user.setFirstName(userDTO.getFirstName());
        user.setLastName(userDTO.getLastName());
        user.setPatronymic(userDTO.getPatronymic());
        if(userDTO.getRoles()!=null){
            user.setRoles(userDTO.getRoles().stream().map(t-> new Role(ERole.valueOf(t.getName()))).collect(Collectors.toSet()));
        }
        log.info("IN mapToUser mapped {} into {}",userDTO,user);
        return user;
    }
}
