package com.Diplom.BackEnd.exception.impl;

import com.Diplom.BackEnd.dto.ErrorMessageDTO;
import com.Diplom.BackEnd.exception.MyException;
import org.springframework.http.HttpStatus;

public class ForbiddenErrorImpl extends MyException {

    public ForbiddenErrorImpl() {
        super(HttpStatus.FORBIDDEN, new ErrorMessageDTO("У вас нет прав доступа"));
    }

    public ForbiddenErrorImpl(String message) {
        super(HttpStatus.FORBIDDEN,new ErrorMessageDTO(message));
    }

}
