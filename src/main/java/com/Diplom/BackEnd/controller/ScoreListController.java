package com.Diplom.BackEnd.controller;

import com.Diplom.BackEnd.dto.InputStreamResourceDTO;
import com.Diplom.BackEnd.exception.MyException;
import com.Diplom.BackEnd.exception.Runtime.NullPointerExceptionImpl;
import com.Diplom.BackEnd.exception.impl.ServerExceptionImpl;
import com.Diplom.BackEnd.service.imp.ScoreListServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/api/score")
public class ScoreListController {
    @Autowired
    private ScoreListServiceImpl scoreListService;
    @GetMapping("/{id}")
    public ResponseEntity<?> getScoreList(@PathVariable Long id){
        try{
            InputStreamResourceDTO inputStreamResource = scoreListService.getScoreList(id);
            HttpHeaders headers = new HttpHeaders();
            String format = String.format("attachment; filename=%s", inputStreamResource.getFileName());
            headers.add(HttpHeaders.CONTENT_DISPOSITION,format );
            return ResponseEntity.ok()
                    .headers(headers)
                    .contentType(MediaType.APPLICATION_OCTET_STREAM)
                    .body(inputStreamResource.getInputStreamResource());
        }catch (MyException e){
            return e.getResponseEntity();
        }catch (Exception e){
            return new ServerExceptionImpl().getResponseEntity();
        }
    }
}
