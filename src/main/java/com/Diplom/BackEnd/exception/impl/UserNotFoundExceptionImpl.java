package com.Diplom.BackEnd.exception.impl;

import com.Diplom.BackEnd.dto.ErrorMessageDTO;
import com.Diplom.BackEnd.exception.MyException;
import org.apache.logging.log4j.message.StringFormattedMessage;
import org.springframework.http.HttpStatus;

public class UserNotFoundExceptionImpl extends MyException {

    public UserNotFoundExceptionImpl(String message) {
        super(HttpStatus.NOT_FOUND, new ErrorMessageDTO(message));
    }

    public UserNotFoundExceptionImpl() {
        super(HttpStatus.NOT_FOUND, new ErrorMessageDTO("Пользователь с такими данными не найден"));
    }
    public UserNotFoundExceptionImpl(Long id) {
        super(HttpStatus.NOT_FOUND, new ErrorMessageDTO(String.format("Пользователь с id: %s не найден",id)));
    }
}
