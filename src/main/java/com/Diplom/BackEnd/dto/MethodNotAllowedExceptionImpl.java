package com.Diplom.BackEnd.dto;

import com.Diplom.BackEnd.exception.MyException;
import org.springframework.http.HttpStatus;

public class MethodNotAllowedExceptionImpl extends MyException {

    public MethodNotAllowedExceptionImpl(String message) {
        super(HttpStatus.METHOD_NOT_ALLOWED, new ErrorMessageDTO(message));
    }
    public MethodNotAllowedExceptionImpl() {
        super(HttpStatus.METHOD_NOT_ALLOWED, new ErrorMessageDTO("Данный тип запроса не поддерживается"));
    }
}
