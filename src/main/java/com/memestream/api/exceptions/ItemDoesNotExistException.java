package com.memestream.api.exceptions;

import com.memestream.api.model.SimpleResponse;
import org.springframework.http.HttpStatus;

public class ItemDoesNotExistException extends SimpleException {
    public ItemDoesNotExistException(SimpleResponse response) {
        super(HttpStatus.NOT_FOUND, response);
    }
}
