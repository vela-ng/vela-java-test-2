package com.unbabel.challenge.exception;

import com.unbabel.challenge.dto.ErrorDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

@ControllerAdvice
public class GlobalExceptionHandler {

    private Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(value = {RuntimeException.class})
    public ResponseEntity<?> generalExceptionHandler(RuntimeException ex, WebRequest req){
        log.error(ex.getMessage());

        ErrorDto error = new ErrorDto("An Error Occurred while processing the request. \nPlease try checking the input supplied");

        return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
