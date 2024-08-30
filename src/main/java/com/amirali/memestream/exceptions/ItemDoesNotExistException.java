package com.amirali.memestream.exceptions;

import com.amirali.memestream.model.SimpleResponse;
import org.springframework.http.HttpStatus;

public class ItemDoesNotExistException extends SimpleException {
    public ItemDoesNotExistException(SimpleResponse response) {
        super(HttpStatus.NOT_FOUND, response);
    }
}
