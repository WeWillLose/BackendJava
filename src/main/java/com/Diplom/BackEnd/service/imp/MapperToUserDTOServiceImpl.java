package com.Diplom.BackEnd.service.imp;

import com.Diplom.BackEnd.dto.UserDTO;
import com.Diplom.BackEnd.model.Chairman_Slaves;
import com.Diplom.BackEnd.model.User;
import com.Diplom.BackEnd.service.MapperToUserDTOService;
import com.sun.istack.NotNull;
import org.springframework.stereotype.Service;

@Service
public class MapperToUserDTOServiceImpl implements MapperToUserDTOService {

    @Override
    public UserDTO mapToUserDto(User user, Chairman_Slaves chairman_slaves) throws NullPointerException{
        if(user == null){
            throw new NullPointerException("user must not be null");
        }
        if(chairman_slaves == null){
            throw new NullPointerException("chairman_slaves must not be null");
        }
        if(chairman_slaves.getChairman() == null){
            throw new NullPointerException("chairman_slaves.getChairman() must not be null");
        }
        if(chairman_slaves.getSlaves() == null){
            throw new NullPointerException("chairman_slaves.getSlaves() must not be null");
        }
        return new UserDTO(user,chairman_slaves.getChairman(),chairman_slaves.getSlaves());

    }

    @Override
    public UserDTO mapToUserDto(User user) throws NullPointerException{
        if(user == null){
            throw new NullPointerException("user must not be null");
        }
        return new UserDTO(user);
    }
}
