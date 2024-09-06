package com.amirali.memestream.service.commandhandlers;

import com.amirali.memestream.exceptions.InvalidInputException;
import com.amirali.memestream.exceptions.ItemDoesNotExistException;
import com.amirali.memestream.model.MemeRequest;
import com.amirali.memestream.model.MemeType;
import com.amirali.memestream.model.SimpleResponse;
import com.amirali.memestream.repository.ChefRepository;
import com.amirali.memestream.repository.MemeRepository;
import com.amirali.memestream.service.StorageUtil;
import com.amirali.memestream.service.Validator;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PostMemeCommandHandler implements Command<MemeRequest, SimpleResponse> {

    private final Validator validator;

    private final MemeRepository memeRepository;

    private final ChefRepository chefRepository;

    private final StorageUtil storageUtil;

    @Override
    public ResponseEntity<SimpleResponse> execute(MemeRequest input) throws IOException {
        final var meme = input.getMetadata();
        validator.validateMemeRequest(meme, false);

        final var chef = chefRepository
                .findByName(((String) SecurityContextHolder.getContext().getAuthentication().getPrincipal()))
                .orElseThrow(() -> new ItemDoesNotExistException(new SimpleResponse("Current chef does not exist")));

        meme.setChef(chef);

        final var id = UUID.randomUUID();
        meme.setId(id);

        final var file = input.getMeme();
        if (meme.getType() != MemeType.TEXT) {
            if (file != null && file.getBytes().length > 0) {
                final var memeStoragePath = storageUtil.getMemesPath();
                if (!Files.exists(memeStoragePath))
                    Files.createDirectories(memeStoragePath);

                final var memePath = storageUtil.getMemePath(id, meme.getType());
                try (InputStream inputStream = file.getInputStream()) {
                    Files.copy(inputStream, memePath, StandardCopyOption.REPLACE_EXISTING);

                    meme.setMemePath(memePath.toString());
                }
            } else {
                throw new InvalidInputException(new SimpleResponse("File cannot be null or empty when setting meme type to video, image etc"));
            }
        }

        memeRepository.save(meme);

        return ResponseEntity.ok(new SimpleResponse("Your meme uploaded successfully with id of: " + id));
    }
}
