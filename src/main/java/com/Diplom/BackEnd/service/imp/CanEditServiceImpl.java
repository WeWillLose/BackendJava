package com.Diplom.BackEnd.service.imp;

import com.Diplom.BackEnd.exception.impl.NullPointerExceptionImpl;
import com.Diplom.BackEnd.model.ERole;
import com.Diplom.BackEnd.model.Role;
import com.Diplom.BackEnd.model.ToDo;
import com.Diplom.BackEnd.model.User;
import com.Diplom.BackEnd.service.CanEditService;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import javax.persistence.Transient;

@Service
public class CanEditServiceImpl implements CanEditService {

    public boolean canEdit(User owner){
        User auth = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if(auth == null){
            throw new NullPointerExceptionImpl("IN canEdit auth is null");
        }
        if(owner == null){
            throw new NullPointerExceptionImpl("IN canEdit owner must not be null");
        }
        if(owner.getId() == null){
            throw new NullPointerExceptionImpl("IN canEdit owner.getId() must not be null");
        }

        if(owner.getId().equals(auth.getId())){
            return true;
        }

        if(auth.getRoles() == null) return false;
        for (Role role : auth.getRoles()) {
            if(role.getName().equals(ERole.ROLE_ADMIN)){
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean canCreate() {
        User auth = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if(auth == null){
            throw new NullPointerExceptionImpl("IN canEdit auth is null");
        }

        if(auth.getRoles() == null) return false;

        for (Role role : auth.getRoles()) {
            if(role.getName().equals(ERole.ROLE_ADMIN)){
                return true;
            }
        }
        return false;
    }
}
