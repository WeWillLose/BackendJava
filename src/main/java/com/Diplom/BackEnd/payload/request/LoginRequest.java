package com.Diplom.BackEnd.payload.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data

@AllArgsConstructor
public class LoginRequest {
    private String username;

    private String password;

}