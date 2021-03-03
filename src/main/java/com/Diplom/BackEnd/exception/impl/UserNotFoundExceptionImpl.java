package com.Diplom.BackEnd.exception.impl;

import com.Diplom.BackEnd.dto.ErrorMessageDTO;
import com.Diplom.BackEnd.exception.MyException;
import org.springframework.http.HttpStatus;

public class UserNotFoundExceptionImpl extends MyException {

    public UserNotFoundExceptionImpl(String message) {
        super(HttpStatus.NOT_FOUND, new ErrorMessageDTO(message));
    }

    public UserNotFoundExceptionImpl() {
        super(HttpStatus.NOT_FOUND, new ErrorMessageDTO("Пользователь с такими данными не найден"));
    }
}
