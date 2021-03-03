package com.Diplom.BackEnd.controller;

import com.Diplom.BackEnd.dto.UserDTO;
import com.Diplom.BackEnd.exception.MyException;
import com.Diplom.BackEnd.exception.impl.ServerErrorImpl;
import com.Diplom.BackEnd.model.Chairman_Slaves;
import com.Diplom.BackEnd.model.User;
import com.Diplom.BackEnd.service.Chairman_slavesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

// Not working
@RestController
@RequestMapping("api/chairman_slaves")
public class Chairman_slavesController {
    @Autowired
    Chairman_slavesService chairman_slavesService;
    @PutMapping("setChairman/{id}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<?> updateChairman_slavesRelation(@PathVariable(name = "id") User user, @RequestBody UserDTO userDTO){
        try{
            Chairman_Slaves chairman_slaves = chairman_slavesService.setChairman(user, userDTO);
            return ResponseEntity.ok().body(chairman_slaves);
        }catch (MyException e){
            return e.getResponseEntity();
        }catch (Exception e){
            e.printStackTrace();
            return new ServerErrorImpl().getResponseEntity();
        }
    }
    @PutMapping("setSlaves/{id}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<?> updateChairman_slavesRelation(@PathVariable(name = "id") User user, @RequestBody Set<UserDTO> userDTO){
        try{
            Chairman_Slaves chairman_slaves = chairman_slavesService.setSlaves(user, userDTO);
            return ResponseEntity.ok().body(chairman_slaves);
        }catch (MyException e){
            return e.getResponseEntity();
        }catch (Exception e){
            e.printStackTrace();
            return new ServerErrorImpl().getResponseEntity();
        }
    }
    @PutMapping("addSlaves/{id}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<?> addChairman_slavesRelation(@PathVariable(name = "id") User user, @RequestBody Set<UserDTO> slaves){
        try{
            Chairman_Slaves chairman_slaves = chairman_slavesService.addSlave(user, slaves);
            return ResponseEntity.ok().body(chairman_slaves);
        }catch (MyException e){
            return e.getResponseEntity();
        }catch (Exception e){
            e.printStackTrace();
            return new ServerErrorImpl().getResponseEntity();
        }
    }
}
