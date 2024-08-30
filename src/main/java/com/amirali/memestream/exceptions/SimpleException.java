package com.amirali.memestream.exceptions;

import com.amirali.memestream.model.SimpleResponse;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class SimpleException extends RuntimeException {

    private final HttpStatus status;
    private final SimpleResponse response;

    public SimpleException(HttpStatus status, SimpleResponse response) {
        super(response.getResponse());

        this.response = response;
        this.status = status;
    }
}
