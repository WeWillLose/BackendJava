package com.Diplom.BackEnd.dto;

import com.Diplom.BackEnd.model.ERole;
import com.Diplom.BackEnd.model.Role;
import com.Diplom.BackEnd.model.User;
import com.fasterxml.jackson.annotation.JsonView;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.mapping.Collection;

import java.beans.Transient;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Data
@NoArgsConstructor
public class UserDTO {

    private Long id;

    private String username;

    private String firstName;

    private String lastName;

    private String patronymic;

    private Set<RoleDTO> roles = new HashSet<>();

    private boolean is_chairman;

    private UserDTO chairman;


    public boolean isIs_chairman() {
        if (roles == null || roles.isEmpty())
            return false;
        return roles.contains(new RoleDTO(ERole.ROLE_CHAIRMAN));
    }
}
