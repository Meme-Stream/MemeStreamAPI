package com.memestream.api.service.queryhandlers;

import com.memestream.api.exceptions.ItemDoesNotExistException;
import com.memestream.api.model.Meme;
import com.memestream.api.model.SimpleResponse;
import com.memestream.api.repository.MemeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class GetMemeMetadataByIdQueryHandler implements Query<UUID, Meme> {

    private final MemeRepository memeRepository;

    @Override
    public ResponseEntity<Meme> execute(UUID id) {
        final var meme = memeRepository.findById(id).orElseThrow(() -> new ItemDoesNotExistException(new SimpleResponse(id + " does not exist")));
        meme.setLikesSize(meme.getLikes().size());
        return ResponseEntity.ok(meme);
    }
}
