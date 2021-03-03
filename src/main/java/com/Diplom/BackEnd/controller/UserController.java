package com.Diplom.BackEnd.controller;

import com.Diplom.BackEnd.dto.PasswordResetDTO;
import com.Diplom.BackEnd.dto.UserDTO;
import com.Diplom.BackEnd.exception.MyException;
import com.Diplom.BackEnd.exception.impl.BadRequestImpl;
import com.Diplom.BackEnd.exception.impl.ServerErrorImpl;
import com.Diplom.BackEnd.model.User;
import com.Diplom.BackEnd.service.MapperToUserDTOService;
import com.Diplom.BackEnd.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.MissingPathVariableException;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping("api/user")
public class UserController {
    @Autowired
    private UserService userService;

    @Autowired
    private MapperToUserDTOService mapperToUserDTOService;

    @GetMapping("info/{id}")
    public ResponseEntity<?> getUserInfo(@PathVariable(name = "id") User user){
        try{
            UserDTO byId = userService.getUserDtoByUserAndFindChairman_slaves(user);
            return ResponseEntity.ok().body(byId);
        }catch (MyException e){
            return e.getResponseEntity();
        }catch (Exception e){
            e.printStackTrace();
            return new ServerErrorImpl().getResponseEntity();
        }
    }
    @GetMapping("all")
    public ResponseEntity<?> getAllUserInfo(){
        try{
            List<UserDTO> byId = userService.getUserDtoByUserAndFindChairman_slaves(userService.getAll());
            return ResponseEntity.ok().body(byId);
        }catch (MyException e){
            return e.getResponseEntity();
        }catch (Exception e){
            e.printStackTrace();
            return new ServerErrorImpl().getResponseEntity();
        }
    }

    @PutMapping("info/{id}")
    @PreAuthorize("#user.id == authentication.principal.id || hasAuthority('ADMIN')")
    public ResponseEntity<?> updateUserInfo(@PathVariable(name="id") User user,@RequestBody UserDTO userDTO){
        try{

            UserDTO byId = userService.getUserDtoByUserAndFindChairman_slaves(userService.updateUserInfo(user.getId(),userDTO));
            return ResponseEntity.ok().body(byId);
        }catch (MyException e){
            return e.getResponseEntity();
        }catch (Exception e){
            e.printStackTrace();
            return new ServerErrorImpl().getResponseEntity();
        }
    }

    @PutMapping("password/{id}")
    @PreAuthorize("#user.id == authentication.principal.id || hasAuthority('ADMIN')")
    public ResponseEntity<?> updateUser(@PathVariable(name="id") User user,@RequestBody PasswordResetDTO password){
        try{
            UserDTO byId = userService.getUserDtoByUserAndFindChairman_slaves(userService.setPassword(user.getId(),password.getPassword()));
            return ResponseEntity.ok().body(byId);
        }catch (MyException e){
            return e.getResponseEntity();
        }catch (Exception e){
            e.printStackTrace();
            return new ServerErrorImpl().getResponseEntity();
        }
    }
}
