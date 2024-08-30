package com.amirali.memestream.exceptions;

import com.amirali.memestream.model.SimpleResponse;
import org.springframework.http.HttpStatus;

public class ItemAlreadyExistsException extends SimpleException {

    public ItemAlreadyExistsException(SimpleResponse response) {
        super(HttpStatus.CONFLICT, response);
    }
}
