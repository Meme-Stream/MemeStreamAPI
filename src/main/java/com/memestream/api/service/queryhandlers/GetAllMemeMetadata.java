package com.memestream.api.service.queryhandlers;

import com.memestream.api.model.Meme;
import com.memestream.api.repository.MemeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class GetAllMemeMetadata implements Query<Pageable, Map<String, Object>> {

    private final MemeRepository memeRepository;

    @Override
    public ResponseEntity<Map<String, Object>> execute(Pageable pageable) {
        final var memePage = memeRepository.findAll(pageable);
        for (Meme meme : memePage.getContent())
            meme.setLikesSize(meme.getLikes().size());

        final var result = new LinkedHashMap<String, Object>();
        result.put("totalElements", memePage.getTotalElements());
        result.put("totalPages", memePage.getTotalPages());
        result.put("content", memePage.getContent());

        return ResponseEntity.ok(result);
    }
}
