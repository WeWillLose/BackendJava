package com.Diplom.BackEnd.controller;

import com.Diplom.BackEnd.exception.MyException;
import com.Diplom.BackEnd.exception.impl.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.convert.ConversionFailedException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MissingPathVariableException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

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

    @ExceptionHandler(value = MethodArgumentTypeMismatchException.class)
    public ResponseEntity<?> errorHandlerConvert(HttpServletRequest req, MethodArgumentTypeMismatchException e){
        e.printStackTrace();
        return new BadRequestImpl("Ошибка в переменной пути").getResponseEntity();
    }
//    @ExceptionHandler(value = NullPointerExceptionImpl.class)
//    public ResponseEntity<?> errorHandlerNullPointer(HttpServletRequest req, NullPointerExceptionImpl e){
//        e.printStackTrace();
//        return new ServerErrorImpl().getResponseEntity();
//    }
//    @ExceptionHandler(value = MyException.class)
//    public ResponseEntity<?> errorHandlerMyException(HttpServletRequest req, MyException e){
//        e.printStackTrace();
//        new ValidationErrorImpl("Handler My Exception");
//        return e.getResponseEntity();
//    }
//    @ExceptionHandler(value = Exception.class)
//    public ResponseEntity<?> errorHandlerException(HttpServletRequest req, Exception e){
//        e.printStackTrace();
//        return new ServerErrorImpl().getResponseEntity();
//    }
}