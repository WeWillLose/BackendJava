package com.Diplom.BackEnd.dto;

import com.Diplom.BackEnd.model.ERole;
import com.Diplom.BackEnd.model.Role;
import com.Diplom.BackEnd.model.User;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.mapping.Collection;

import java.beans.Transient;
import java.util.Collections;
import java.util.HashSet;
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


    public boolean isIs_chairman() {
        if (roles == null || roles.isEmpty())
            return false;
//        return roles.contains(new RoleDTO(ERole.ROLE_CHAIRMAN));
        return false;
    }

    public UserDTO(Long id, String username, String firstName, String lastName, String patronymic, Set<Role> roles) {
        this.id = id;
        this.username = username;
        this.firstName = firstName;
        this.lastName = lastName;
        this.patronymic = patronymic;
        this.roles = roles.stream().map(t -> new RoleDTO(t.getName().name())).collect(Collectors.toSet());
    }

    public UserDTO(User user) {
        this.id = user.getId();
        this.username = user.getUsername();
        this.firstName = user.getFirstName();
        this.lastName = user.getLastName();
        this.patronymic = user.getPatronymic();
        this.roles = user.getRoles().stream().map(t -> new RoleDTO(t.getName().name())).collect(Collectors.toSet());

    }

}
