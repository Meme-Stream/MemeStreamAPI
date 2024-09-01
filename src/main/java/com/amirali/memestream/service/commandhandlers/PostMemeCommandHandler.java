package com.amirali.memestream.service.commandhandlers;

import com.amirali.memestream.exceptions.ItemDoesNotExistException;
import com.amirali.memestream.model.MemeType;
import com.amirali.memestream.model.PostMemeInput;
import com.amirali.memestream.model.SimpleResponse;
import com.amirali.memestream.repository.ChefRepository;
import com.amirali.memestream.repository.MemeRepository;
import com.amirali.memestream.service.Validator;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PostMemeCommandHandler implements Command<PostMemeInput, SimpleResponse> {

    private final Validator validator;

    private final MemeRepository memeRepository;

    private final ChefRepository chefRepository;

    @Value("${application.storage.location}")
    private String storageLocation;

    @Override
    public ResponseEntity<SimpleResponse> execute(PostMemeInput input) throws IOException {
        final var meme = input.getMetadata();

        validator.validateMemeInput(meme);

        final var chef = chefRepository
                .findByName(((String) SecurityContextHolder.getContext().getAuthentication().getPrincipal()))
                .orElseThrow(() -> new ItemDoesNotExistException(new SimpleResponse("Current chef does not exist")));

        meme.setChef(chef);

        final var file = input.getMeme();
        final var id = UUID.randomUUID();
        meme.setId(id);

        if (meme.getType() != null && meme.getType() != MemeType.TEXT && file != null) {
            final var storagePath = Paths.get(storageLocation);

            if (!Files.exists(storagePath))
                Files.createDirectories(storagePath);

            final var filePath = storagePath.resolve(id + convertMemeTypeToFileExtension(meme.getType()));

            try (InputStream inputStream = file.getInputStream()) {
                Files.copy(inputStream, filePath, StandardCopyOption.REPLACE_EXISTING);

                meme.setMemePath(filePath.toString());
            }
        } else {
            meme.setType(MemeType.TEXT);
        }

        memeRepository.save(meme);

        return ResponseEntity.ok(new SimpleResponse("Your meme uploaded successfully with id of: " + id));
    }

    private String convertMemeTypeToFileExtension(MemeType type) {
        return switch (type) {
            case VIDEO_MP4 -> ".mp4";
            case IMAGE_PNG -> ".png";
            case IMAGE_JPEG -> ".jpg";

            case TEXT -> ".txt";
        };
    }
}
