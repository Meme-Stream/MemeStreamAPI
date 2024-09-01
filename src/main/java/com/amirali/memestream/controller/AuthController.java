package com.amirali.memestream.controller;

import com.amirali.memestream.model.Chef;
import com.amirali.memestream.model.SimpleResponse;
import com.amirali.memestream.model.AuthRequest;
import com.amirali.memestream.service.commandhandlers.AuthenticationCommandHandler;
import com.amirali.memestream.service.commandhandlers.CreateChefCommandHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
