package com.Diplom.BackEnd.service.imp;

import com.Diplom.BackEnd.dto.RoleDTO;
import com.Diplom.BackEnd.dto.UserDTO;
import com.Diplom.BackEnd.exception.impl.NullPointerExceptionImpl;
import com.Diplom.BackEnd.model.ERole;
import com.Diplom.BackEnd.model.Role;
import com.Diplom.BackEnd.model.User;
import com.Diplom.BackEnd.service.UserMapperService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserMapperServiceImpl implements UserMapperService {


//    @Override
//    public UserDTO mapToUserDto(User user, Chairman_Slaves chairman_slaves) {
//        return null;
//    }

    @Override
    public UserDTO mapToUserDto(User user) throws NullPointerException{
        if(user == null){
            return new UserDTO();
        }
        UserDTO userDTO = new UserDTO();
        userDTO.setUsername(user.getUsername());
        userDTO.setFirstName(user.getFirstName());
        userDTO.setId(user.getId());
        userDTO.setLastName(user.getLastName());
        userDTO.setPatronymic(user.getPatronymic());
        if(user.getRoles()!=null){
            userDTO.setRoles(user.getRoles().stream().map(role->{return new RoleDTO(role.getName());}).collect(Collectors.toSet()));
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
        return userDTO;
    }

    @Override
    public List<UserDTO> mapToUserDto(List<User> user) {
        if(user == null){
            throw new NullPointerExceptionImpl("user must not be null");
        }
        return user.stream().map(this::mapToUserDto).collect(Collectors.toList());
    }

    @Override
    public User mapToUser(UserDTO userDTO) {
        if(userDTO == null){
            return new User();
        }
        User user = new User();
        user.setUsername(userDTO.getUsername());
        user.setFirstName(userDTO.getFirstName());
        user.setLastName(userDTO.getLastName());
        user.setPatronymic(userDTO.getPatronymic());
        if(userDTO.getRoles()!=null){
            user.setRoles(userDTO.getRoles().stream().map(t->{
                return new Role(ERole.valueOf(t.getName()));
            }).collect(Collectors.toSet()));
        }

        return user;
    }
}
