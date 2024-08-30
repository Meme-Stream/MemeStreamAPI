package com.amirali.memestream.security;

import com.amirali.memestream.service.JwtUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections;

@Component
@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        final var authorizationHeader = request.getHeader("Authorization");

        String token = null;
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer "))
            token = authorizationHeader.substring(7);

        if (token != null && jwtUtil.isTokenValid(token)) {
            final var name = jwtUtil.getSubject(token);
            var authenticationToken = new UsernamePasswordAuthenticationToken(name, null, Collections.emptyList());

            SecurityContextHolder.getContext().setAuthentication(authenticationToken);
        }

        filterChain.doFilter(request, response);
    }
}
