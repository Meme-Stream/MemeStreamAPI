package com.amirali.memestream.exceptions;

import com.amirali.memestream.model.SimpleResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ExceptionConfiguration {

    @ExceptionHandler(SimpleException.class)
    public ResponseEntity<SimpleResponse> handleSimpleException(SimpleException exception) {
        return ResponseEntity.status(exception.getStatus()).body(exception.getResponse());
    }
}
