package com.Diplom.BackEnd.exception.impl;

import com.Diplom.BackEnd.dto.ErrorMessageDTO;
import com.Diplom.BackEnd.exception.MyException;
import org.springframework.http.HttpStatus;

public class ReportNotFoundExceptionImpl extends MyException {
    public ReportNotFoundExceptionImpl() {
        super(HttpStatus.NOT_FOUND, new ErrorMessageDTO("Отчет не найден"));
    }
    public ReportNotFoundExceptionImpl(Long id) {
        super(HttpStatus.NOT_FOUND, new ErrorMessageDTO(String.format("Отчет с id - %d не найден",id)));
    }
    public ReportNotFoundExceptionImpl(String message) {
        super(HttpStatus.NOT_FOUND, new ErrorMessageDTO(message));
    }
}
