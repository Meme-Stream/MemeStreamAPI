package com.amirali.memestream.service.commandhandlers;

import org.springframework.http.ResponseEntity;

import java.io.IOException;

public interface Command <I, O> {
    ResponseEntity<O> execute(I input) throws IOException;
}
