package com.Diplom.BackEnd.payload.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@AllArgsConstructor
public class JwtResponse {
    private String token;
    private Long id;
    private String firstName;
    private String lastName;
    private String patronymic;
    private String username;
    private Set<String> roles;
    private String type = "Bearer";

    public JwtResponse(String token, Long id, String firstName, String lastName, String patronymic, String username, Set<String> roles) {
        this.token = token;
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.patronymic = patronymic;
        this.username = username;
        this.roles = roles;
    }

}

