package com.Diplom.BackEnd.service;

import com.Diplom.BackEnd.model.User;
import com.Diplom.BackEnd.payload.request.LoginRequest;
import com.Diplom.BackEnd.payload.request.SignupRequest;
import com.Diplom.BackEnd.payload.response.JwtResponse;
import com.Diplom.BackEnd.payload.response.UserResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;

public interface AuthService {
    UserResponse authenticateUser(@RequestBody LoginRequest loginRequest);
    UserResponse registerUser (SignupRequest signupRequest);
}
