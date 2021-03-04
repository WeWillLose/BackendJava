package com.Diplom.BackEnd.service.imp;

import com.Diplom.BackEnd.dto.UserDTO;
import com.Diplom.BackEnd.exception.impl.NullPointerExceptionImpl;
import com.Diplom.BackEnd.model.Chairman_Slaves;
import com.Diplom.BackEnd.model.ERole;
import com.Diplom.BackEnd.model.Role;
import com.Diplom.BackEnd.model.User;
import com.Diplom.BackEnd.service.MapperToUserDTOService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class MapperUserDTOServiceImpl implements MapperToUserDTOService {

    @Override
    public UserDTO mapToUserDto(User user, Chairman_Slaves chairman_slaves) throws NullPointerException{
        if(user == null){
            throw new NullPointerExceptionImpl("user must not be null");
        }
        if(chairman_slaves == null){
            throw new NullPointerExceptionImpl("chairman_slaves must not be null");
        }
        return new UserDTO(user,chairman_slaves.getChairman(),chairman_slaves.getSlaves());

    }

    @Override
    public UserDTO mapToUserDto(User user) throws NullPointerException{
        if(user == null){
            throw new NullPointerExceptionImpl("user must not be null");
        }
        return new UserDTO(user);
    }

    @Override
    public List<UserDTO> mapToUserDto(List<User> user) {
        if(user == null){
            throw new NullPointerExceptionImpl("user must not be null");
        }
        return user.stream().map(UserDTO::new).collect(Collectors.toList());
    }

    @Override
    public User mapToUser(UserDTO userDTO) {
        if(userDTO == null){
            throw new NullPointerExceptionImpl("userDTO must not be null");
        }
        User user = new User();
        user.setUsername(userDTO.getUsername());
        user.setFirstName(userDTO.getFirstName());
        user.setLastName(userDTO.getLastName());
        user.setPatronymic(userDTO.getPatronymic());
        user.setRoles(userDTO.getRoles().stream().map(t->{
                return new Role(ERole.valueOf(t.getName()));
        }).collect(Collectors.toSet()));
        return user;
    }
}
