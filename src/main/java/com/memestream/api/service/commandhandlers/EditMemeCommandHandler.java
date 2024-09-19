package com.memestream.api.service.commandhandlers;

import com.memestream.api.exceptions.ItemDoesNotExistException;
import com.memestream.api.exceptions.SimpleException;
import com.memestream.api.model.Meme;
import com.memestream.api.model.MemeRequest;
import com.memestream.api.model.MemeType;
import com.memestream.api.model.SimpleResponse;
import com.memestream.api.repository.MemeRepository;
import com.memestream.api.service.StorageUtil;
import com.memestream.api.service.Validator;
import io.micrometer.common.util.StringUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

@Service
@RequiredArgsConstructor
public class EditMemeCommandHandler implements Command<MemeRequest, SimpleResponse> {

    private final Validator validator;

    private final MemeRepository memeRepository;

    private final StorageUtil storageUtil;

    private boolean isAnythingChanged;

    @Override
    public ResponseEntity<SimpleResponse> execute(MemeRequest input) throws IOException {
        final var metadata = input.getMetadata();
        validator.validateMemeRequest(metadata, true);

        final var meme = memeRepository.findById(metadata.getId()).orElseThrow(() -> new ItemDoesNotExistException(new SimpleResponse(metadata.getId() + " dose not exist")));
        final var chefName = ((String) SecurityContextHolder.getContext().getAuthentication().getPrincipal());

        if (!meme.getChef().getName().equals(chefName))
            throw new SimpleException(HttpStatus.FORBIDDEN, new SimpleResponse("You can't edit a meme that isn't yours"));

        editMemeFile(input.getMeme(), meme, input.getMetadata());

        if (StringUtils.isNotBlank(metadata.getTitle())) {
            meme.setTitle(metadata.getTitle());
            isAnythingChanged = true;
        }
        if (StringUtils.isNotBlank(metadata.getDescription())) {
            meme.setDescription(metadata.getDescription());
            isAnythingChanged = true;
        }

        if (isAnythingChanged) {
            memeRepository.save(meme);

            return ResponseEntity.ok(new SimpleResponse("Your meme has been updated"));
        }else {
            return ResponseEntity.ok(new SimpleResponse("Nothing changed"));
        }
    }

    private void editMemeFile(MultipartFile file, Meme meme, Meme metadata) throws IOException {
        final var type = meme.getType();
        final var metadataType = metadata.getType();

        if (type == metadataType && metadataType != MemeType.TEXT) {
            if (file != null && file.getBytes().length > 0) {
                final var memePath = Paths.get(meme.getMemePath());
                try (InputStream inputStream = file.getInputStream()) {
                    Files.copy(inputStream, memePath, StandardCopyOption.REPLACE_EXISTING);
                }

                isAnythingChanged = true;
            }
            return;
        }
        if (type != metadataType && type != MemeType.TEXT && metadataType != MemeType.TEXT) {
            if (file != null && file.getBytes().length > 0) {
                var memePath = Paths.get(meme.getMemePath());
                Files.deleteIfExists(memePath);

                memePath = storageUtil.getMemePath(meme.getId(), metadataType);
                try (InputStream inputStream = file.getInputStream()) {
                    Files.copy(inputStream, memePath, StandardCopyOption.REPLACE_EXISTING);
                }

                meme.setType(metadataType);
                meme.setMemePath(memePath.toString());

                isAnythingChanged = true;
            }
            return;
        }
        if (type != metadataType && metadataType == MemeType.TEXT) {
            var memePath = Paths.get(meme.getMemePath());
            Files.deleteIfExists(memePath);

            meme.setType(metadataType);
            meme.setMemePath(null);

            isAnythingChanged = true;
            return;
        }
        if (type == MemeType.TEXT && metadataType != MemeType.TEXT) {
            if (file != null && file.getBytes().length > 0) {
                final var memePath = storageUtil.getMemePath(meme.getId(), metadataType);

                try (InputStream inputStream = file.getInputStream()) {
                    Files.copy(inputStream, memePath, StandardCopyOption.REPLACE_EXISTING);
                }

                meme.setType(metadataType);
                meme.setMemePath(memePath.toString());

                isAnythingChanged = true;
            }
        }
    }

}
