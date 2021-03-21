package com.Diplom.BackEnd.controller;

import com.Diplom.BackEnd.dto.PasswordResetDTO;
import com.Diplom.BackEnd.dto.RoleDTO;
import com.Diplom.BackEnd.dto.UserDTO;
import com.Diplom.BackEnd.exception.MyException;
import com.Diplom.BackEnd.exception.impl.NullPointerExceptionImpl;
import com.Diplom.BackEnd.exception.impl.ServerErrorImpl;
import com.Diplom.BackEnd.service.UserDTOMapperService;
import com.Diplom.BackEnd.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/user")
@Slf4j
public class UserController {

    @Autowired
    private UserService userService;
    @Autowired
    private UserDTOMapperService userDTOMapperService;

    @GetMapping("info/{id}")
    public ResponseEntity<?> getUserInfo(@PathVariable(name = "id") Long id){
        try{
            UserDTO byId = userDTOMapperService.mapToUserDto(userService.findById(id));
            return ResponseEntity.ok().body(byId);
        }catch (NullPointerExceptionImpl e){
            return new ServerErrorImpl().getResponseEntity();
        }catch (MyException e){
            return e.getResponseEntity();
        }catch (Exception e){
            log.error("IN getUserInfo",e);
            e.printStackTrace();
            return new ServerErrorImpl().getResponseEntity();
        }
    }
    @GetMapping("info/all")
    public ResponseEntity<?> getAllUserInfo(){
        try{
            List<UserDTO> byId =  userDTOMapperService.mapToUserDto(userService.getAll());
            return ResponseEntity.ok().body(byId);
        }catch (NullPointerExceptionImpl e){
            return new ServerErrorImpl().getResponseEntity();
        }catch (MyException e){
            return e.getResponseEntity();
        }catch (Exception e){
            log.error("IN getAllUserInfo",e);
            e.printStackTrace();
            return new ServerErrorImpl().getResponseEntity();
        }
    }

    @PutMapping("info/{id}")
    public ResponseEntity<?> updateUserInfo(@PathVariable(name="id") Long id,@RequestBody UserDTO userDTO){
        try{
            UserDTO byId = userDTOMapperService.mapToUserDto(userService.updateUserInfo(id,userDTO));
            return ResponseEntity.ok().body(byId);
        }catch (NullPointerExceptionImpl e){
            return new ServerErrorImpl().getResponseEntity();
        }catch (MyException e){
            return e.getResponseEntity();
        }catch (Exception e){
            log.error("IN updateUserInfo",e);
            e.printStackTrace();
            return new ServerErrorImpl().getResponseEntity();
        }
    }

    @PutMapping("followers/setChairmanFor/{id}")
    public ResponseEntity<?> setChairmanForFollower(@PathVariable(name="id") Long followerId,@RequestBody UserDTO chairmanDTO){
        try{
            UserDTO byId = userDTOMapperService.mapToUserDto(userService.setChairman(followerId,chairmanDTO));
            return ResponseEntity.ok().body(byId);
        }catch (NullPointerExceptionImpl e){
            return new ServerErrorImpl().getResponseEntity();
        }catch (MyException e){
            return e.getResponseEntity();
        }catch (Exception e){
            log.error("IN updateUserInfo",e);
            e.printStackTrace();
            return new ServerErrorImpl().getResponseEntity();
        }
    }


    @GetMapping("followers/{id}")
    public ResponseEntity<?> findFollowersByChairmanId(@PathVariable(name="id") Long id){
        try{
            List<UserDTO> followers = userDTOMapperService.mapToUserDto(userService.findFollowers(id));
            return ResponseEntity.ok().body(followers);
        }catch (NullPointerExceptionImpl e){
            return new ServerErrorImpl().getResponseEntity();
        }catch (MyException e){
            return e.getResponseEntity();
        }catch (Exception e){
            log.error("IN updateUserInfo",e);
            e.printStackTrace();
            return new ServerErrorImpl().getResponseEntity();
        }
    }

    @PutMapping("password/{id}")
    public ResponseEntity<?> resetUserPassword(@PathVariable(name="id") Long id,@RequestBody PasswordResetDTO passwordDTO){
        try{
            UserDTO byId =  userDTOMapperService.mapToUserDto(userService.setPassword(id,passwordDTO.getPassword()));
            return ResponseEntity.ok().body(byId);
        }catch (NullPointerExceptionImpl e){
            return new ServerErrorImpl().getResponseEntity();
        }catch (MyException e){
            return e.getResponseEntity();
        }catch (Exception e){
            log.error("IN updateUser",e);
            e.printStackTrace();
            return new ServerErrorImpl().getResponseEntity();
        }
    }
    @PutMapping("roles/{id}")
    public ResponseEntity<?> setRoles(@PathVariable(name="id") Long id,@RequestBody List<RoleDTO> roles){
        try{
            UserDTO byId =  userDTOMapperService.mapToUserDto(userService.setRoles(id,roles));
            return ResponseEntity.ok().body(byId);
        }catch (NullPointerExceptionImpl e){
            return new ServerErrorImpl().getResponseEntity();
        }catch (MyException e){
            return e.getResponseEntity();
        }catch (Exception e){
            log.error("IN updateUser",e);
            e.printStackTrace();
            return new ServerErrorImpl().getResponseEntity();
        }
    }

    @DeleteMapping("{id}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<?> deleteUser(@PathVariable(name = "id") Long id){
        try{
            userService.delete(id);
            return ResponseEntity.ok().build();
        }catch (NullPointerExceptionImpl e){
            return new ServerErrorImpl().getResponseEntity();
        }catch (MyException e){
            return e.getResponseEntity();
        }catch (Exception e){
            log.error("IN deleteUser",e);
            e.printStackTrace();
            return new ServerErrorImpl().getResponseEntity();
        }
    }
}
