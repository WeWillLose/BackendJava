package com.Diplom.BackEnd.payload.request;

import lombok.AllArgsConstructor;
import lombok.Data;
@Data
@AllArgsConstructor
public class SignupRequest {
    private String username;
    private String password;
    private String firstName;
    private String lastName;
    private String patronymic;
}
