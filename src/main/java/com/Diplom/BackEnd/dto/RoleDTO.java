package com.Diplom.BackEnd.dto;

import com.Diplom.BackEnd.model.ERole;
import com.Diplom.BackEnd.model.Role;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RoleDTO {
    private String name;

    public RoleDTO(ERole name) {
        this.name = name.name();
    }

}
