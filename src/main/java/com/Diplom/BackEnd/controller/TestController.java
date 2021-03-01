package com.Diplom.BackEnd.controller;

import com.Diplom.BackEnd.model.Chairman_Slaves;
import com.Diplom.BackEnd.model.User;
import com.Diplom.BackEnd.payload.response.UserResponse;
import com.Diplom.BackEnd.repo.Chairman_slavesRepo;
import com.Diplom.BackEnd.repo.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("test")
public class TestController {
    @Autowired
    UserRepo userRepo;

    @Autowired
    Chairman_slavesRepo chairman_slavesRepo;

    @GetMapping("admin")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> getAdmin(){
        return ResponseEntity.ok("Admin ok");
    }

    @GetMapping("test/{id}")
    public ResponseEntity<?> getTest(@PathVariable Long id){
        User byId = userRepo.findById(id).orElseThrow(()-> new RuntimeException());
        Chairman_Slaves byChairman_username = chairman_slavesRepo.findByChairman_Username(byId.getUsername());
        if (byChairman_username!=null){
            return ResponseEntity.ok(new UserResponse(byId,byChairman_username.getChairman(),byChairman_username.getSlaves()));
        }
        return ResponseEntity.ok(new UserResponse(byId));

    }

}
