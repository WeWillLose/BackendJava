package com.Diplom.BackEnd.service.imp;

import com.Diplom.BackEnd.exception.Runtime.NullPointerExceptionImpl;
import com.Diplom.BackEnd.model.ERole;
import com.Diplom.BackEnd.model.Report;
import com.Diplom.BackEnd.model.Role;
import com.Diplom.BackEnd.model.User;
import com.Diplom.BackEnd.service.CanEditService;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class CanEditServiceImpl implements CanEditService {

    private boolean isChairman(User user){
        if(user == null || user.getRoles() == null) return  false;
        for (Role role : user.getRoles()) {
            if(role.getName().equals(ERole.ROLE_CHAIRMAN)){
                return true;
            }
        }
        return false;
    }
    private boolean isAdmin(User user){
        if(user == null || user.getRoles() == null) return  false;
        for (Role role : user.getRoles()) {
            if(role.getName().equals(ERole.ROLE_ADMIN)){
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean canEdit(User editor,User owner){
        if(editor == null || owner==null){
            return false;
        }
        if(owner.getId() == null){
            throw new NullPointerExceptionImpl("IN canEdit owner.getId() must not be null");
        }

        if(owner.getId().equals(editor.getId())){
            return true;
        }

        if(editor.getRoles() == null) {
            return false;
        }

        return isAdmin(editor);
    }
    @Override
    public boolean canEditOnlyAdmin(User user){
        if(user == null){
            return false;
        }
        return isAdmin(user);
    }
    @Override
    public boolean canEditReportWithChairman(User editor, Report report){
        if(editor == null || report == null) return false;
        if(isChairman(editor)) return true;
        if(report.getAuthor()!=null){
            return report.getAuthor().getId().equals(editor.getId());
        }
        return isAdmin(editor);
    }
}
