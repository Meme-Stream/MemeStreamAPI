package com.amirali.memestream.service.commandhandlers;

import com.amirali.memestream.exceptions.InvalidInputException;
import com.amirali.memestream.exceptions.ItemDoesNotExistException;
import com.amirali.memestream.model.MemeRequest;
import com.amirali.memestream.model.MemeType;
import com.amirali.memestream.model.SimpleResponse;
import com.amirali.memestream.repository.ChefRepository;
import com.amirali.memestream.repository.MemeRepository;
import com.amirali.memestream.service.Validator;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PostMemeCommandHandler implements Command<MemeRequest, SimpleResponse> {

    private final Validator validator;

    private final MemeRepository memeRepository;

    private final ChefRepository chefRepository;

    @Value("${application.storage.location}")
    private String storageLocation;

    @Override
    public ResponseEntity<SimpleResponse> execute(MemeRequest input) throws IOException {
        final var meme = input.getMetadata();
        validator.validateMemeRequest(meme, false);

        final var file = input.getMeme();
        if (file.getBytes().length == 0) {
            throw new InvalidInputException(new SimpleResponse("Content of the file cannot be empty"));
        }

        final var chef = chefRepository
                .findByName(((String) SecurityContextHolder.getContext().getAuthentication().getPrincipal()))
                .orElseThrow(() -> new ItemDoesNotExistException(new SimpleResponse("Current chef does not exist")));

        meme.setChef(chef);

        final var id = UUID.randomUUID();
        meme.setId(id);

        final var memeStoragePath = Paths.get(storageLocation, File.separator + "memes");
        if (!Files.exists(memeStoragePath))
            Files.createDirectories(memeStoragePath);

        final var memePath = memeStoragePath.resolve(id + convertMemeTypeToFileExtension(meme.getType()));
        try (InputStream inputStream = file.getInputStream()) {
            Files.copy(inputStream, memePath, StandardCopyOption.REPLACE_EXISTING);

            meme.setMemePath(memePath.toString());
        }

        memeRepository.save(meme);

        return ResponseEntity.ok(new SimpleResponse("Your meme uploaded successfully with id of: " + id));
    }

    private String convertMemeTypeToFileExtension(MemeType type) {
        return switch (type) {
            case VIDEO_MP4 -> ".mp4";
            case IMAGE_PNG -> ".png";
            case IMAGE_JPEG -> ".jpg";
        };
    }

    private void copyMultipartFileToStorage(MultipartFile file, Path path) throws IOException {
        try (InputStream inputStream = file.getInputStream()) {
            Files.copy(inputStream, path, StandardCopyOption.REPLACE_EXISTING);
        }
    }
}
