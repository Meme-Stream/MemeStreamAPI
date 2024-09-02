package com.amirali.memestream.service.commandhandlers;

import com.amirali.memestream.exceptions.ItemDoesNotExistException;
import com.amirali.memestream.exceptions.SimpleException;
import com.amirali.memestream.model.Role;
import com.amirali.memestream.model.SimpleResponse;
import com.amirali.memestream.repository.MemeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class DeleteMemeByIdCommandHandler implements Command<UUID, SimpleResponse> {

    private final MemeRepository memeRepository;

    @Override
    public ResponseEntity<SimpleResponse> execute(UUID id) throws IOException {
        final var meme = memeRepository.findById(id).orElseThrow(() -> new ItemDoesNotExistException(new SimpleResponse(id + " does not exist")));

        if (!hasAuthorityToDelete()) {
            final var chefName = ((String) SecurityContextHolder.getContext().getAuthentication().getPrincipal());

            if (!chefName.equals(meme.getChef().getName()))
                throw new SimpleException(HttpStatus.FORBIDDEN, new SimpleResponse("You can't delete a meme that isn't yours"));
        }

        final var memePath = Paths.get(meme.getMemePath());
        Files.deleteIfExists(memePath);

        memeRepository.delete(meme);

        return ResponseEntity.ok(new SimpleResponse("Meme successfully deleted"));
    }

    private boolean hasAuthorityToDelete() {
        for (GrantedAuthority authority : SecurityContextHolder.getContext().getAuthentication().getAuthorities()) {
            if (authority.getAuthority().equals("ROLE_" + Role.ADMIN.name()))
                return true;
        }

        return false;
    }
}
