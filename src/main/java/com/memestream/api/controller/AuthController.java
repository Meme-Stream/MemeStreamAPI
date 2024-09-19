package com.memestream.api.controller;

import com.memestream.api.model.AuthRequest;
import com.memestream.api.model.Chef;
import com.memestream.api.model.SimpleResponse;
import com.memestream.api.service.commandhandlers.AuthenticationCommandHandler;
import com.memestream.api.service.commandhandlers.CreateChefCommandHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final CreateChefCommandHandler createChefCommandHandler;

    private final AuthenticationCommandHandler authenticationCommandHandler;

    @PostMapping("/createChef")
    public ResponseEntity<SimpleResponse> createChef(@RequestBody Chef chef) {
        return createChefCommandHandler.execute(chef);
    }

    @PostMapping
    public ResponseEntity<SimpleResponse> auth(@RequestBody AuthRequest authRequest) {
        return authenticationCommandHandler.execute(authRequest);
    }
}
