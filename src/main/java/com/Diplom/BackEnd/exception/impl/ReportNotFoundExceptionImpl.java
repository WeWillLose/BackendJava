package com.Diplom.BackEnd.exception.impl;

import com.Diplom.BackEnd.dto.ErrorMessageDTO;
import com.Diplom.BackEnd.exception.MyException;
import org.springframework.http.HttpStatus;

public class ReportNotFoundExceptionImpl extends MyException {
    public ReportNotFoundExceptionImpl() {
        super(HttpStatus.NOT_FOUND, new ErrorMessageDTO("Отчет с таким id не найден"));
    }
    public ReportNotFoundExceptionImpl(String message) {
        super(HttpStatus.NOT_FOUND, new ErrorMessageDTO(message));
    }
}
