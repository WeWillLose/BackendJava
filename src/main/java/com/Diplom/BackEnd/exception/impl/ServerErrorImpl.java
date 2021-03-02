package com.Diplom.BackEnd.exception.impl;

import com.Diplom.BackEnd.dto.ErrorMessageDTO;
import com.Diplom.BackEnd.exception.MyException;
import org.springframework.http.HttpStatus;

public class ServerErrorImpl extends MyException  {
    public ServerErrorImpl() {
        super(HttpStatus.INTERNAL_SERVER_ERROR, new ErrorMessageDTO("Ошибка сервера"));
    }

    public ServerErrorImpl(String message) {
        super(HttpStatus.INTERNAL_SERVER_ERROR,new ErrorMessageDTO(message));
    }


}
