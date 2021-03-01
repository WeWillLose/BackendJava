package com.Diplom.BackEnd.payload.response;

import com.Diplom.BackEnd.model.Role;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Set;
import java.util.stream.Collectors;

@Data
@AllArgsConstructor
public class RoleResponse {
    private String name;
}
