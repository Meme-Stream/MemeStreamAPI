package com.amirali.memestream.security;

import com.amirali.memestream.repository.ChefRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ChefDetailsService implements UserDetailsService {

    private final ChefRepository chefRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        var chef = chefRepository.findByName(username).orElseThrow(() -> new UsernameNotFoundException(username + " does not exists"));
        return User.withUsername(chef.getName())
                .password(chef.getPassword())
                .roles(chef.getRoles().stream().map(Enum::name).toArray(String[]::new))
                .build();
    }
}
