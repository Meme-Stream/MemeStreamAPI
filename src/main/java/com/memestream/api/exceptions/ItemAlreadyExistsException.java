package com.memestream.api.exceptions;

import com.memestream.api.model.SimpleResponse;
import org.springframework.http.HttpStatus;

public class ItemAlreadyExistsException extends SimpleException {

    public ItemAlreadyExistsException(SimpleResponse response) {
        super(HttpStatus.CONFLICT, response);
    }
}
