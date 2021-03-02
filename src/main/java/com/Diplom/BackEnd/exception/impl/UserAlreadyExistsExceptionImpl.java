package com.Diplom.BackEnd.exception.impl;

import com.Diplom.BackEnd.dto.ErrorMessageDTO;
import com.Diplom.BackEnd.exception.MyException;
import org.springframework.http.HttpStatus;

public class UserAlreadyExistsExceptionImpl extends MyException {
    public UserAlreadyExistsExceptionImpl() {
        super(HttpStatus.CONFLICT, new ErrorMessageDTO("Пользователь с таким логином уже существует"));
    }

    public UserAlreadyExistsExceptionImpl(String message) {
        super(HttpStatus.CONFLICT,new ErrorMessageDTO(message));
    }
}
