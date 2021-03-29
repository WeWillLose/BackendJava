package com.Diplom.BackEnd.exception.impl;

import com.Diplom.BackEnd.dto.ErrorMessageDTO;
import com.Diplom.BackEnd.exception.MyException;
import org.springframework.http.HttpStatus;

public class BadRequestExceptionImpl extends MyException {
    public BadRequestExceptionImpl(String message) {
        super(HttpStatus.BAD_REQUEST,  new ErrorMessageDTO(message));
    }
    public BadRequestExceptionImpl() {
        super(HttpStatus.BAD_REQUEST, new ErrorMessageDTO("Некоректные данные в запросе"));
    }
}
