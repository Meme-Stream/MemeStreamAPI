package com.memestream.api.service.commandhandlers;

import com.memestream.api.exceptions.ItemAlreadyExistsException;
import com.memestream.api.exceptions.ItemDoesNotExistException;
import com.memestream.api.exceptions.SimpleException;
import com.memestream.api.model.Like;
import com.memestream.api.model.SimpleResponse;
import com.memestream.api.repository.ChefRepository;
import com.memestream.api.repository.LikeRepository;
import com.memestream.api.repository.MemeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class LikeMemeCommandHandler implements Command<UUID, SimpleResponse> {

    private final MemeRepository memeRepository;

    private final LikeRepository likeRepository;

    private final ChefRepository chefRepository;

    @Override
    public ResponseEntity<SimpleResponse> execute(UUID id) {
        final var meme = memeRepository.findById(id).orElseThrow(() -> new ItemDoesNotExistException(new SimpleResponse(id + " dose not exist")));

        final var currentChefName = ((String) SecurityContextHolder.getContext().getAuthentication().getPrincipal());

        if (currentChefName.equals(meme.getChef().getName()))
            throw new SimpleException(HttpStatus.CONFLICT, new SimpleResponse("You cannot like your meme"));

        if (likeRepository.existsByChefName(currentChefName))
            throw new ItemAlreadyExistsException(new SimpleResponse("You already liked this meme"));

        final var currentChef = chefRepository
                .findByName(currentChefName)
                .orElseThrow(() -> new ItemDoesNotExistException(new SimpleResponse("Current chef does not exist")));

        final var newLike = new Like();
        newLike.setMeme(meme);
        newLike.setChef(currentChef);

        likeRepository.save(newLike);

        return ResponseEntity.ok(new SimpleResponse("Success"));
    }
}
