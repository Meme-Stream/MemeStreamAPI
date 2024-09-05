package com.amirali.memestream.service.commandhandlers;

import com.amirali.memestream.exceptions.ItemDoesNotExistException;
import com.amirali.memestream.exceptions.SimpleException;
import com.amirali.memestream.model.ChefRoleRequest;
import com.amirali.memestream.model.SimpleResponse;
import com.amirali.memestream.repository.ChefRepository;
import com.amirali.memestream.service.Validator;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DeleteRoleCommandHandler implements Command<ChefRoleRequest, SimpleResponse> {

    private final ChefRepository chefRepository;

    private final Validator validator;

    @Override
    public ResponseEntity<SimpleResponse> execute(ChefRoleRequest input) {
        validator.validateChefRoleRequest(input);

        final var name = input.getName();
        final var chef = chefRepository.findByName(name).orElseThrow(() -> new ItemDoesNotExistException(new SimpleResponse(name + " does not exist")));

        final var currentAdminName = ((String) SecurityContextHolder.getContext().getAuthentication().getPrincipal());
        if (name.equals(currentAdminName))
            throw new SimpleException(HttpStatus.FORBIDDEN, new SimpleResponse("You cannot remove your role yourself"));

        final var role = input.getRole();
        if (!chef.getRoles().contains(role)) {
            throw new SimpleException(HttpStatus.CONFLICT, new SimpleResponse(name + " does not have " + role.name() + " role"));
        }

        chef.getRoles().remove(role);

        chefRepository.save(chef);

        return ResponseEntity.ok(new SimpleResponse(role.name() + " role successfully deleted from " + name));
    }
}
