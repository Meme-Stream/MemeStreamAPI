package com.amirali.memestream.configurations;

import com.amirali.memestream.service.StorageUtil;
import com.amirali.memestream.service.Validator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class MemeStreamConfigurationTest {

    @Bean
    public PasswordEncoder encoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public Validator validator() {
        return new Validator();
    }

    @Bean
    public StorageUtil storageUtil() {
        return new StorageUtil();
    }
}
