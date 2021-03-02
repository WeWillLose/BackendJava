package com.Diplom.BackEnd.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
@Data
@AllArgsConstructor
public class SignupDTO {
    private String username;
    private String password;
    private String firstName;
    private String lastName;
    private String patronymic;
}
