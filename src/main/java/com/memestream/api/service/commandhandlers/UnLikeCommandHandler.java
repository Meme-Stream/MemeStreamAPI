package com.memestream.api.service.commandhandlers;

import com.memestream.api.exceptions.ItemDoesNotExistException;
import com.memestream.api.model.SimpleResponse;
import com.memestream.api.repository.LikeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UnLikeCommandHandler implements Command<UUID, SimpleResponse> {

    private final LikeRepository likeRepository;

    @Override
    public ResponseEntity<SimpleResponse> execute(UUID id) {
        final var currentChefName = ((String) SecurityContextHolder.getContext().getAuthentication().getPrincipal());

        final var like = likeRepository.findByMemeIdAndChefName(id, currentChefName);

        if (like.isEmpty())
            throw new ItemDoesNotExistException(new SimpleResponse("You have not liked this meme"));

        likeRepository.delete(like.get());

        return ResponseEntity.ok(new SimpleResponse("Success"));
    }
}
