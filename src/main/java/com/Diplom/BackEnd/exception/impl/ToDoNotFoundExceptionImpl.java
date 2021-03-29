package com.Diplom.BackEnd.exception.impl;

import com.Diplom.BackEnd.dto.ErrorMessageDTO;
import com.Diplom.BackEnd.exception.MyException;
import org.springframework.http.HttpStatus;

public class ToDoNotFoundExceptionImpl extends MyException {
    public ToDoNotFoundExceptionImpl(String message) {
        super(HttpStatus.NOT_FOUND, new ErrorMessageDTO(message));
    }
    public ToDoNotFoundExceptionImpl() {
        super(HttpStatus.NOT_FOUND, new ErrorMessageDTO("Заметка не найдена"));
    }

    public ToDoNotFoundExceptionImpl(Long id) {
        super(HttpStatus.NOT_FOUND, new ErrorMessageDTO(String.format("Заметка с id - %d не найдена",id)));
    }
}
