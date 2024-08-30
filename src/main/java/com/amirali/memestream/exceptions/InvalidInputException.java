package com.amirali.memestream.exceptions;

import com.amirali.memestream.model.SimpleResponse;
import org.springframework.http.HttpStatus;

public class InvalidInputException extends SimpleException {

    public InvalidInputException(SimpleResponse response) {
        super(HttpStatus.BAD_REQUEST, response);
    }
}
