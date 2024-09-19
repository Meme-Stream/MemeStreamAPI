package com.memestream.api.securitytest;

import com.memestream.api.model.Role;
import org.springframework.security.test.context.support.WithSecurityContext;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@WithSecurityContext(factory = WithMockChefSecurityContextFactory.class)
public @interface WithMockChef {

    String name() default "test chef";

    Role[] roles() default {Role.USER};
}
