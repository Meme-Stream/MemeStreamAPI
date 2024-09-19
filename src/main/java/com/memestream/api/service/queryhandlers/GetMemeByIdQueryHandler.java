package com.memestream.api.service.queryhandlers;

import com.memestream.api.exceptions.ItemDoesNotExistException;
import com.memestream.api.model.MemeType;
import com.memestream.api.model.SimpleResponse;
import com.memestream.api.repository.MemeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class GetMemeByIdQueryHandler implements Query<UUID, Object> {

    private final MemeRepository memeRepository;

    @Override
    public ResponseEntity<Object> execute(UUID id) {
        final var meme = memeRepository.findById(id).orElseThrow(() -> new ItemDoesNotExistException(new SimpleResponse(id + " does not exist")));

        if (meme.getType() != MemeType.TEXT) {
            final var resource = new FileSystemResource(meme.getMemePath());

            return ResponseEntity.status(HttpStatus.OK)
                    .header("Content-Disposition", "attachment")
                    .contentType(MemeType.convertMemeTypeToMediaType(meme.getType()))
                    .body(resource);
        } else {
            meme.setLikesSize(meme.getLikes().size());
            return ResponseEntity.ok(meme);
        }
    }
}
