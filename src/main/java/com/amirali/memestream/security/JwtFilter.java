package com.amirali.memestream.security;

import com.amirali.memestream.exceptions.InvalidTokenException;
import com.amirali.memestream.service.JwtUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AccountStatusUserDetailsChecker;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;

    private final ChefDetailsService chefDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        final var authorizationHeader = request.getHeader("Authorization");

        String token = null;
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer "))
            token = authorizationHeader.substring(7);

        if (token != null) {
            if (jwtUtil.isTokenValid(token)) {
                final var name = jwtUtil.getSubject(token);
                final var chefDetails = chefDetailsService.loadUserByUsername(name);
                new AccountStatusUserDetailsChecker().check(chefDetails);

                var authenticationToken = new UsernamePasswordAuthenticationToken(name, null, chefDetails.getAuthorities());

                SecurityContextHolder.getContext().setAuthentication(authenticationToken);
            }else {
                throw new InvalidTokenException("Your token is invalid");
            }
        }

        filterChain.doFilter(request, response);
    }
}
