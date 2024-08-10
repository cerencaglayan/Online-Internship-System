package com.g7.config;

import com.g7.exception.BadRequestException;
import com.g7.payload.response.Response;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class ExceptionAdvice {
    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<Response<Object>> restException(BadRequestException e){
        return Response.badRequest(e.getMessage());
    }
}
