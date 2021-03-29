package com.Diplom.BackEnd.exception.impl;

import com.Diplom.BackEnd.dto.ErrorMessageDTO;
import com.Diplom.BackEnd.exception.MyException;
import org.springframework.http.HttpStatus;

public class ServerExceptionImpl extends MyException  {
    public ServerExceptionImpl() {
        super(HttpStatus.INTERNAL_SERVER_ERROR, new ErrorMessageDTO("Ошибка сервера"));
    }

    public ServerExceptionImpl(String message) {
        super(HttpStatus.INTERNAL_SERVER_ERROR,new ErrorMessageDTO(message));
    }


}
