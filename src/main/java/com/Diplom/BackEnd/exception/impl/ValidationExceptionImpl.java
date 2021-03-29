package com.Diplom.BackEnd.exception.impl;

import com.Diplom.BackEnd.dto.ErrorMessageDTO;
import com.Diplom.BackEnd.exception.MyException;
import org.springframework.http.HttpStatus;

public class ValidationExceptionImpl extends MyException {
    public ValidationExceptionImpl(String message) {
        super(HttpStatus.BAD_REQUEST, new ErrorMessageDTO(message));
    }
    public ValidationExceptionImpl() {
        super(HttpStatus.BAD_REQUEST, new ErrorMessageDTO("Данные не валидны"));
    }
}
