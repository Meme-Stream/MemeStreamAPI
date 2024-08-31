package com.amirali.memestream.service.queryhandlers;

import com.amirali.memestream.model.MemeDTO;
import com.amirali.memestream.repository.MemeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GetAllMemeMetadata implements Query<Pageable, List<MemeDTO>> {

    private final MemeRepository memeRepository;

    @Override
    public ResponseEntity<List<MemeDTO>> execute(Pageable pageable) {
        final var memePage = memeRepository.findAll(pageable);
        final var memeDTOList = memePage.stream().map(meme -> new MemeDTO(
                meme.getId(),
                meme.getTitle(),
                meme.getDescription(),
                meme.getChef().getName(),
                meme.getType()
        )).toList();

        return ResponseEntity.ok(memeDTOList);
    }
}
