package com.amirali.memestream.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PostMemeInput {

    private MemeDTO metadata;

    private MultipartFile meme;
}
