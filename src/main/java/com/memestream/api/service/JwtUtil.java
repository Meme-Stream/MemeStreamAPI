package com.memestream.api.service;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.util.Date;

@Service
public class JwtUtil {

    @Value("${application.password.secret}")
    private String secret;

    private static final long EXPIRATION_5_DAYS = 432_000_000;

    public String generateToken(String subject) {
        return Jwts.builder()
                .signWith(Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8)))
                .subject(subject)
                .expiration(new Date(System.currentTimeMillis() + EXPIRATION_5_DAYS))
                .compact();
    }

    public String getSubject(String token) {
        return Jwts.parser()
                .verifyWith(Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8)))
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .getSubject();
    }

    public boolean isTokenValid(String token) {
        try {
            Jwts.parser()
                    .verifyWith(Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8)))
                    .build()
                    .parse(token);
            return true;
        }catch (Exception e) {
            return false;
        }
    }
}
