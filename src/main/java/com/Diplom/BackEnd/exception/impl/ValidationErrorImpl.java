package com.Diplom.BackEnd.exception.impl;

import com.Diplom.BackEnd.dto.ErrorMessageDTO;
import com.Diplom.BackEnd.exception.MyException;
import org.springframework.http.HttpStatus;

public class ValidationErrorImpl extends MyException {
    public ValidationErrorImpl( String message) {
        super(HttpStatus.BAD_REQUEST, new ErrorMessageDTO(message));
    }
    public ValidationErrorImpl() {
        super(HttpStatus.BAD_REQUEST, new ErrorMessageDTO("Данные не валидны"));
    }
}
