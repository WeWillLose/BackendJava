package com.Diplom.BackEnd.exception.impl;

import com.Diplom.BackEnd.dto.ErrorMessageDTO;
import com.Diplom.BackEnd.exception.MyException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class ForbiddenExceptionImpl extends MyException {

    public ForbiddenExceptionImpl() {
        super(HttpStatus.FORBIDDEN, new ErrorMessageDTO("Недостаточно прав"));
    }

    public ForbiddenExceptionImpl(String message) {
        super(HttpStatus.FORBIDDEN,new ErrorMessageDTO(message));
    }
}
