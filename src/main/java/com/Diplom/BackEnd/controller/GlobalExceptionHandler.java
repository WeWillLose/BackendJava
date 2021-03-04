package com.Diplom.BackEnd.controller;

import com.Diplom.BackEnd.dto.MethodNotAllowedExceptionImpl;
import com.Diplom.BackEnd.exception.impl.BadRequestImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.core.convert.ConversionException;
import org.springframework.core.convert.ConversionFailedException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MissingPathVariableException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.servlet.http.HttpServletRequest;

@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(value = {MissingPathVariableException.class, ConversionFailedException.class})
    public ResponseEntity<?> errorHandlerPathVar(HttpServletRequest req, Exception e){
        log.error(e.getMessage());
        return new BadRequestImpl("Ошибка в переменной пути").getResponseEntity();
    }

    @ExceptionHandler(value = {HttpRequestMethodNotSupportedException.class})
    public ResponseEntity<?> errorHandlerMethodNotSupported(HttpServletRequest req, Exception e){
        log.error(e.getMessage());
        return new MethodNotAllowedExceptionImpl().getResponseEntity();
    }

//    @ExceptionHandler(value = MissingPathVariableException.class)
//    public ResponseEntity<?> errorHandlerConvert(HttpServletRequest req, Exception e){
//        e.printStackTrace();
//        return new BadRequestImpl("Ошибка в переменной пути").getResponseEntity();
//    }
}