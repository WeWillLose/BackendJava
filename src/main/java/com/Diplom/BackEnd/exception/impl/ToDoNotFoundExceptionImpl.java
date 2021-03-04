package com.Diplom.BackEnd.exception.impl;

import com.Diplom.BackEnd.dto.ErrorMessageDTO;
import com.Diplom.BackEnd.exception.MyException;
import org.springframework.http.HttpStatus;

public class ToDoNotFoundExceptionImpl extends MyException {
    public ToDoNotFoundExceptionImpl(String message) {
        super(HttpStatus.NOT_FOUND, new ErrorMessageDTO(message));
    }
    public ToDoNotFoundExceptionImpl() {
        super(HttpStatus.NOT_FOUND, new ErrorMessageDTO("Данные не валидны"));
    }
}
