package com.memestream.api.exceptions;

import com.memestream.api.model.ErrorResponse;
import com.memestream.api.model.SimpleResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AccountStatusException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;

@ControllerAdvice
public class ExceptionConfiguration {

    @ExceptionHandler(SimpleException.class)
    public ResponseEntity<SimpleResponse> handleSimpleException(SimpleException exception) {
        return ResponseEntity.status(exception.getStatus()).body(exception.getResponse());
    }

    @ExceptionHandler({InvalidTokenException.class, AuthenticationException.class})
    public ResponseEntity<ErrorResponse> handleAuthExceptions(Exception exception) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(
                new ErrorResponse(exception.getMessage(), HttpStatus.UNAUTHORIZED.value(), LocalDateTime.now())
        );
    }

    @ExceptionHandler({AccountStatusException.class, AccessDeniedException.class})
    public ResponseEntity<ErrorResponse> handleAccountStatusException(AccountStatusException exception) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(
                new ErrorResponse(exception.getMessage(), HttpStatus.FORBIDDEN.value(), LocalDateTime.now())
        );
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleOtherExceptions(Exception exception) {
        exception.printStackTrace();
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                new ErrorResponse("An internal server error occurred", HttpStatus.INTERNAL_SERVER_ERROR.value(), LocalDateTime.now())
        );
    }
}
