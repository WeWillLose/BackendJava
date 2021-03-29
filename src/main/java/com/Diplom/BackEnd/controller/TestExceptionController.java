package com.Diplom.BackEnd.controller;

import com.Diplom.BackEnd.exception.Runtime.NullPointerExceptionImpl;
import com.Diplom.BackEnd.exception.impl.ValidationExceptionImpl;
import org.springframework.http.ResponseEntity;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("exception")
public class TestExceptionController {
    @GetMapping("null")
    public ResponseEntity<?> NullPointerException(){
        throw new NullPointerExceptionImpl("null pointer");
    }
    @GetMapping("exception")
    public ResponseEntity<?> Exception() throws Exception {
        throw new Exception("");
    }
    @GetMapping("my")
    public ResponseEntity<?> MyException(){
        throw new ValidationExceptionImpl("my  exception");
    }
    @GetMapping("notsupp")
    public ResponseEntity<?> NotSupported() throws HttpRequestMethodNotSupportedException {
        throw new HttpRequestMethodNotSupportedException("");
    }
}
