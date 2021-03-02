package com.Diplom.BackEnd.dto;

import com.Diplom.BackEnd.model.ERole;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class RoleDTO {
    private String name;

    public RoleDTO(ERole name) {
        this.name = name.name();
    }
}
