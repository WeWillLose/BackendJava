package com.Diplom.BackEnd.exception;

import com.Diplom.BackEnd.dto.ErrorMessageDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import javax.persistence.Transient;

public abstract class MyException extends RuntimeException{
    @Transient
    private final HttpStatus status;

    @Transient
    private final ErrorMessageDTO messageDTO;

    public ResponseEntity<?> getResponseEntity() {
        return ResponseEntity.status(this.status).body(messageDTO);
    }

    public MyException(HttpStatus status, ErrorMessageDTO messageDTO) {
        this.messageDTO = messageDTO;
        this.status = status;
    }
}
