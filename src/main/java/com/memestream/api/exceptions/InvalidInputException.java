package com.memestream.api.exceptions;

import com.memestream.api.model.SimpleResponse;
import org.springframework.http.HttpStatus;

public class InvalidInputException extends SimpleException {

    public InvalidInputException(SimpleResponse response) {
        super(HttpStatus.BAD_REQUEST, response);
    }
}
