package com.amirali.memestream.service.commandhandlers;

import com.amirali.memestream.exceptions.InvalidInputException;
import com.amirali.memestream.exceptions.ItemDoesNotExistException;
import com.amirali.memestream.exceptions.SimpleException;
import com.amirali.memestream.model.MemeInput;
import com.amirali.memestream.model.MemeType;
import com.amirali.memestream.model.SimpleResponse;
import com.amirali.memestream.repository.MemeRepository;
import com.amirali.memestream.service.Validator;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

@Service
@RequiredArgsConstructor
public class EditMemeCommandHandler implements Command<MemeInput, SimpleResponse> {

    private final Validator validator;

    private final MemeRepository memeRepository;

    @Value("${application.storage.location}")
    private String storageLocation;

    @Override
    public ResponseEntity<SimpleResponse> execute(MemeInput input) throws IOException {
        final var metadata = input.getMetadata();
        validator.validateMemeInput(metadata, true);

        final var file = input.getMeme();
        if (file.getBytes().length == 0) {
            throw new InvalidInputException(new SimpleResponse("Content of the file cannot be empty"));
        }

        final var meme = memeRepository.findById(metadata.getId()).orElseThrow(() -> new ItemDoesNotExistException(new SimpleResponse(metadata.getId() + " dose not exist")));
        final var chefName = ((String) SecurityContextHolder.getContext().getAuthentication().getPrincipal());

        if (!meme.getChef().getName().equals(chefName))
            throw new SimpleException(HttpStatus.FORBIDDEN, new SimpleResponse("You can't edit a meme that isn't yours"));


        final var memePath = Paths.get(meme.getMemePath());
        if (metadata.getType() == meme.getType()) {
            try (InputStream inputStream = file.getInputStream()) {
                Files.copy(inputStream, memePath, StandardCopyOption.REPLACE_EXISTING);
            }
        } else {
            Files.deleteIfExists(memePath);

            final var storagePath = Paths.get(storageLocation);
            final var newMemePath = storagePath.resolve(meme.getId() + convertMemeTypeToFileExtension(metadata.getType()));

            try (InputStream inputStream = file.getInputStream()) {
                Files.copy(inputStream, newMemePath, StandardCopyOption.REPLACE_EXISTING);

                meme.setType(metadata.getType());
                meme.setMemePath(newMemePath.toString());
            }
        }

        meme.setTitle(metadata.getTitle());
        meme.setDescription(metadata.getDescription());

        memeRepository.save(meme);

        return ResponseEntity.ok(new SimpleResponse("Your meme has been updated"));
    }

    private String convertMemeTypeToFileExtension(MemeType type) {
        return switch (type) {
            case VIDEO_MP4 -> ".mp4";
            case IMAGE_PNG -> ".png";
            case IMAGE_JPEG -> ".jpg";
        };
    }

}
