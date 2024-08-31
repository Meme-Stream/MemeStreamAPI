package com.amirali.memestream.controller;

import com.amirali.memestream.model.MemeDTO;
import com.amirali.memestream.model.PostMemeInput;
import com.amirali.memestream.model.SimpleResponse;
import com.amirali.memestream.service.commandhandlers.PostMemeCommandHandler;
import com.amirali.memestream.service.queryhandlers.GetMemeByIdQueryHandler;
import com.amirali.memestream.service.queryhandlers.GetAllMemeMetadata;
import com.amirali.memestream.service.queryhandlers.GetMemeMetadataByIdQueryHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/meme")
@RequiredArgsConstructor
public class MemeController {

    private final PostMemeCommandHandler postMemeCommandHandler;

    private final GetMemeByIdQueryHandler getMemeByIdQueryHandler;

    private final GetMemeMetadataByIdQueryHandler getMemeMetadataByIdQueryHandler;

    private final GetAllMemeMetadata getAllMemeMetadata;

    @PostMapping(path = "/post", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<SimpleResponse> postMeme
            (@RequestPart("metadata") MemeDTO memeDTO,
            @RequestPart(name = "meme", required = false) MultipartFile multipartFile) throws IOException {
        return postMemeCommandHandler.execute(new PostMemeInput(memeDTO, multipartFile));
    }

    @GetMapping("/get/{uuid}")
    public ResponseEntity<Object> getMemeById(@PathVariable String uuid) {
        return getMemeByIdQueryHandler.execute(UUID.fromString(uuid));
    }

    @GetMapping("/getMetadata/{uuid}")
    public ResponseEntity<MemeDTO> getMemeMetadataById(@PathVariable String uuid) {
        return getMemeMetadataByIdQueryHandler.execute(UUID.fromString(uuid));
    }

    @GetMapping("/getAllMetadata")
    public ResponseEntity<List<MemeDTO>> getAllMemeMetadata
            (@RequestParam("pageNumber") int pageNumber,
             @RequestParam("pageSize") int pageSize) {

        return getAllMemeMetadata.execute(PageRequest.of(pageNumber, pageSize));
    }
}
