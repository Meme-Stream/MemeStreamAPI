package com.memestream.api.service.commandhandlers;

import com.memestream.api.exceptions.ItemAlreadyExistsException;
import com.memestream.api.model.Role;
import com.memestream.api.model.SimpleResponse;
import com.memestream.api.model.Chef;
import com.memestream.api.repository.ChefRepository;
import com.memestream.api.service.Validator;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CreateChefCommandHandler implements Command<Chef, SimpleResponse>  {

    private final Validator validator;

    private final ChefRepository chefRepository;

    private final PasswordEncoder passwordEncoder;

    @Override
    public ResponseEntity<SimpleResponse> execute(Chef input) {
        validator.validateChefInput(input);
        if (chefRepository.existsByName(input.getName()))
            throw new ItemAlreadyExistsException(new SimpleResponse(input.getName() + " already exists"));
        input.setPassword(passwordEncoder.encode(input.getPassword()));
        input.setRoles(List.of(Role.USER));
        chefRepository.save(input);

        return ResponseEntity.ok(new SimpleResponse("Success"));
    }
}
