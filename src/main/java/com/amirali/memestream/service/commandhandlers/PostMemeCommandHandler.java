package com.amirali.memestream.service.commandhandlers;

import com.amirali.memestream.exceptions.ItemDoesNotExistException;
import com.amirali.memestream.model.MemeType;
import com.amirali.memestream.model.PostMemeInput;
import com.amirali.memestream.model.SimpleResponse;
import com.amirali.memestream.repository.ChefRepository;
import com.amirali.memestream.repository.MemeRepository;
import com.amirali.memestream.service.Validator;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
@RequiredArgsConstructor
public class PostMemeCommandHandler implements Command<PostMemeInput, SimpleResponse> {

    private final Validator validator;

    private final MemeRepository memeRepository;

    private final ChefRepository chefRepository;

    @Override
    public ResponseEntity<SimpleResponse> execute(PostMemeInput input) throws IOException {
        final var meme = input.getMetadata();

        validator.validateMemeInput(meme);

        final var chef = chefRepository
                .findByName(((String) SecurityContextHolder.getContext().getAuthentication().getPrincipal()))
                .orElseThrow(() -> new ItemDoesNotExistException(new SimpleResponse("Current chef does not exist")));

        meme.setChef(chef);

        final var file = input.getMeme();
        if (meme.getType() != null && meme.getType() != MemeType.TEXT && file != null)
            meme.setMeme(file.getBytes());
        else
            meme.setType(MemeType.TEXT);

        final var id = memeRepository.save(meme).getId();

        return ResponseEntity.ok(new SimpleResponse("Your meme uploaded successfully with id of: " + id));
    }
}
