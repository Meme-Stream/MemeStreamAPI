package com.amirali.memestream.service.queryhandlers;

import com.amirali.memestream.model.Meme;
import com.amirali.memestream.repository.MemeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GetAllMemeMetadata implements Query<Pageable, List<Meme>> {

    private final MemeRepository memeRepository;

    @Override
    public ResponseEntity<List<Meme>> execute(Pageable pageable) {
        final var memePage = memeRepository.findAll(pageable);
        final var memeList = memePage.stream().toList();

        return ResponseEntity.ok(memeList);
    }
}
