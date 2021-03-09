package com.Diplom.BackEnd.controller;

import com.Diplom.BackEnd.dto.LoginDTO;
import com.Diplom.BackEnd.dto.SignupDTO;
import com.Diplom.BackEnd.dto.UserDTO;
import com.Diplom.BackEnd.exception.MyException;
import com.Diplom.BackEnd.exception.impl.ServerErrorImpl;
import com.Diplom.BackEnd.model.User;
import com.Diplom.BackEnd.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    @PostMapping("login")
    public ResponseEntity<?> authenticateUser(@RequestBody LoginDTO loginDTO) {
        try{
            UserDTO userDTO = authService.authenticateUser(loginDTO);
            return ResponseEntity.ok().body(userDTO);
        }catch (MyException e){
            return e.getResponseEntity();
        }catch (Exception e){
            e.printStackTrace();
            return new ServerErrorImpl().getResponseEntity();
        }

    }

    @PostMapping("registration")
    public ResponseEntity<?> registerUser(@RequestBody SignupDTO signUpDTO) {
        try{
            UserDTO userDTO = authService.registerUser(signUpDTO);
            return ResponseEntity.ok().body(userDTO);
        }catch (MyException e){
            return e.getResponseEntity();
        }catch (Exception e){
            e.printStackTrace();
            return new ServerErrorImpl().getResponseEntity();
        }
    }
    @GetMapping("current")
    public ResponseEntity<?> registerUser(@AuthenticationPrincipal User user) {
        try{
            return ResponseEntity.ok().body(user);
        }catch (MyException e){
            return e.getResponseEntity();
        }catch (Exception e){
            e.printStackTrace();
            return new ServerErrorImpl().getResponseEntity();
        }
    }

}
