package com.Diplom.BackEnd.controller;

import com.Diplom.BackEnd.exception.MyException;
import com.Diplom.BackEnd.exception.impl.ServerErrorImpl;
import com.Diplom.BackEnd.service.imp.ScoreListServiceImpl;
import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping("/api/score")
public class ScoreListController {
    @Autowired
    private ScoreListServiceImpl scoreListService;
    @GetMapping("/{id}")
    public ResponseEntity<?> getScoreList(@PathVariable Long id){
        try{
            scoreListService.getScoreList(id);
        }catch (MyException e){
            return e.getResponseEntity();
        }catch (Exception e){
            e.printStackTrace();
            return  new ServerErrorImpl().getResponseEntity();
        }

        return ResponseEntity.ok().body("");
    }
}
