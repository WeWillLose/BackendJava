package com.Diplom.BackEnd.controller;

import com.Diplom.BackEnd.dto.LoginDTO;
import com.Diplom.BackEnd.dto.SignupDTO;
import com.Diplom.BackEnd.dto.UserDTO;
import com.Diplom.BackEnd.exception.MyException;
import com.Diplom.BackEnd.exception.impl.ServerExceptionImpl;
import com.Diplom.BackEnd.model.User;
import com.Diplom.BackEnd.service.AuthService;
import com.Diplom.BackEnd.service.UserMapperService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/auth")
@Slf4j
public class AuthController {

    @Autowired
    private AuthService authService;
    @Autowired
    private UserMapperService userMapperService;

    @PostMapping("login")
    public ResponseEntity<?> authenticateUser(@RequestBody LoginDTO loginDTO) {
        try{
            UserDTO userDTO = userMapperService.mapToUserDto(authService.authenticateUser(loginDTO));
            return ResponseEntity.ok().body(userDTO);
        }catch (MyException e){
            return e.getResponseEntity();
        }catch (Exception e){
            e.printStackTrace();
            return new ServerExceptionImpl().getResponseEntity();
        }

    }

    @PostMapping("registration")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<?> registerUser(@RequestBody SignupDTO signUpDTO) {
        try{
            return ResponseEntity.ok().body(userMapperService.mapToUserDto(authService.registerUser(signUpDTO)));
        }catch (MyException e){
            return e.getResponseEntity();
        }catch (Exception e){
            e.printStackTrace();
            return new ServerExceptionImpl().getResponseEntity();
        }
    }
    @GetMapping("isLogged ")
    public ResponseEntity<?> getCurrentUser(@AuthenticationPrincipal User user) {
        try{
            return ResponseEntity.ok().body(user != null);
        }catch (MyException e){
            return e.getResponseEntity();
        }catch (Exception e){
            e.printStackTrace();
            return new ServerExceptionImpl().getResponseEntity();
        }
    }

}
