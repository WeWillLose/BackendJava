package com.Diplom.BackEnd.controller;

import com.Diplom.BackEnd.payload.request.LoginRequest;
import com.Diplom.BackEnd.payload.request.SignupRequest;
import com.Diplom.BackEnd.payload.response.JwtResponse;
import com.Diplom.BackEnd.payload.response.MessageResponse;
import com.Diplom.BackEnd.payload.response.UserResponse;
import com.Diplom.BackEnd.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
@RestController
@RequestMapping("auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    @PostMapping("login")
    public ResponseEntity<?> authenticateUser(@RequestBody LoginRequest loginRequest) {

        UserResponse response = authService.authenticateUser(loginRequest);
        if (response == null){
            return ResponseEntity.badRequest().body("Пользователь не найден");
        }

        return ResponseEntity.ok(response);
    }

    @PostMapping("registration")
    public ResponseEntity<?> registerUser(@RequestBody SignupRequest signUpRequest) {
        try {
            UserResponse response = authService.registerUser(signUpRequest);
            return ResponseEntity.ok(response);
        }catch (Exception e){
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse(e.getMessage()));
        }
    }

}
