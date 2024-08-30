package com.amirali.memestream.service.commandhandlers;

import com.amirali.memestream.model.SimpleResponse;
import com.amirali.memestream.model.Chef;
import com.amirali.memestream.model.ChefDTO;
import com.amirali.memestream.repository.ChefRepository;
import com.amirali.memestream.service.Validator;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CreateChefCommandHandler implements Command<ChefDTO, SimpleResponse>  {

    private final Validator validator;

    private final ChefRepository chefRepository;

    private final PasswordEncoder passwordEncoder;

    @Override
    public ResponseEntity<SimpleResponse> execute(ChefDTO input) {
        validator.validateChefDTOInput(input);
        input.setPassword(passwordEncoder.encode(input.getPassword()));

        final var newChef = new Chef(input);

        chefRepository.save(newChef);

        return ResponseEntity.ok(new SimpleResponse("Success"));
    }
}
