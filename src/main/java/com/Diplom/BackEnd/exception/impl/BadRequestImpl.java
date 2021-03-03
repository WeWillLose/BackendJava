package com.Diplom.BackEnd.exception.impl;

import com.Diplom.BackEnd.dto.ErrorMessageDTO;
import com.Diplom.BackEnd.exception.MyException;
import org.springframework.http.HttpStatus;

public class BadRequestImpl extends MyException {
    public BadRequestImpl(String message) {
        super(HttpStatus.BAD_REQUEST,  new ErrorMessageDTO(message));
    }
    public BadRequestImpl() {
        super(HttpStatus.BAD_REQUEST, new ErrorMessageDTO("Некоректные данные в запросе"));
    }
}
