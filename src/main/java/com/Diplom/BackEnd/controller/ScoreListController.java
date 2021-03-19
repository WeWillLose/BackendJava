package com.Diplom.BackEnd.controller;

import com.Diplom.BackEnd.exception.MyException;
import com.Diplom.BackEnd.exception.impl.NullPointerExceptionImpl;
import com.Diplom.BackEnd.exception.impl.ServerErrorImpl;
import com.Diplom.BackEnd.service.imp.ScoreListServiceImpl;
import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.UUID;

@RestController
@RequestMapping("/api/score")
public class ScoreListController {
    @Autowired
    private ScoreListServiceImpl scoreListService;
    @GetMapping("/{id}")
    public ResponseEntity<?> getScoreList(@PathVariable Long id){
        try{
            InputStreamResource inputStreamResource = scoreListService.getScoreList(id);
            HttpHeaders headers = new HttpHeaders();

            String format = String.format("attachment; filename=%s.docx", UUID.randomUUID().toString());
            headers.add(HttpHeaders.CONTENT_DISPOSITION,format );
            return ResponseEntity.ok()
                    .headers(headers)
                    .contentType(MediaType.APPLICATION_OCTET_STREAM)
                    .body(inputStreamResource);
        }catch (NullPointerExceptionImpl e){
            return new ServerErrorImpl().getResponseEntity();
        }catch (MyException e){
            return e.getResponseEntity();
        }catch (Exception e){
            return new ServerErrorImpl().getResponseEntity();
        }
    }
}
