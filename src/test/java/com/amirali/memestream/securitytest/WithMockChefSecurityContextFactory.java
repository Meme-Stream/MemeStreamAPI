package com.amirali.memestream.securitytest;

import com.amirali.memestream.model.Role;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithSecurityContextFactory;

import java.util.ArrayList;
import java.util.List;

public class WithMockChefSecurityContextFactory implements WithSecurityContextFactory<WithMockChef> {
    @Override
    public SecurityContext createSecurityContext(WithMockChef annotation) {
        var context = SecurityContextHolder.createEmptyContext();
        List<SimpleGrantedAuthority> authorities = new ArrayList<>();
        for (Role role : annotation.roles()){
            authorities.add(new SimpleGrantedAuthority("ROLE_" + role.name()));
        }
        var auth = UsernamePasswordAuthenticationToken.authenticated(annotation.name(), null, authorities);

        context.setAuthentication(auth);
        return context;
    }
}
