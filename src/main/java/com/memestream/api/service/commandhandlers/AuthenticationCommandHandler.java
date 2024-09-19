package com.memestream.api.service.commandhandlers;

import com.memestream.api.model.SimpleResponse;
import com.memestream.api.model.AuthRequest;
import com.memestream.api.service.JwtUtil;
import com.memestream.api.service.Validator;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationCommandHandler implements Command<AuthRequest, SimpleResponse> {

    private final Validator validator;

    private final AuthenticationManager authenticationManager;

    private final JwtUtil jwtUtil;

    @Override
    public ResponseEntity<SimpleResponse> execute(AuthRequest input) {
        validator.validateChefAuthRequest(input);

        var authenticationToken = new UsernamePasswordAuthenticationToken(input.getName(), input.getPassword());
        var authenticated = authenticationManager.authenticate(authenticationToken);
        SecurityContextHolder.getContext().setAuthentication(authenticated);

        var token = jwtUtil.generateToken(input.getName());

        return ResponseEntity.ok(new SimpleResponse(token));
    }
}
