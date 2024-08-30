package com.amirali.memestream.service.queryhandlers;

import com.amirali.memestream.exceptions.ItemDoesNotExistException;
import com.amirali.memestream.model.MemeDTO;
import com.amirali.memestream.model.SimpleResponse;
import com.amirali.memestream.repository.MemeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class GetMemeMetadataByIdQueryHandler implements Query<UUID, MemeDTO> {

    private final MemeRepository memeRepository;

    @Override
    public ResponseEntity<MemeDTO> execute(UUID id) {
        final var meme = memeRepository.findById(id).orElseThrow(() -> new ItemDoesNotExistException(new SimpleResponse(id + " does not exist")));

        final var memeDTO = new MemeDTO(
                meme.getId(),
                meme.getTitle(),
                meme.getDescription(),
                meme.getChef().getName(),
                meme.getType()
        );

        return ResponseEntity.ok(memeDTO);
    }
}
