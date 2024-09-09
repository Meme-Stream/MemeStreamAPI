package com.amirali.memestream.service.commandhandlers;

import com.amirali.memestream.configurations.MemeStreamConfigurationTest;
import com.amirali.memestream.exceptions.InvalidInputException;
import com.amirali.memestream.model.*;
import com.amirali.memestream.repository.ChefRepository;
import com.amirali.memestream.repository.MemeRepository;
import com.amirali.memestream.securitytest.WithMockChef;
import com.amirali.memestream.service.StorageUtil;
import com.amirali.memestream.service.Validator;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.util.FileSystemUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@SpringBootTest(classes = MemeStreamConfigurationTest.class)
@WithMockChef
class PostMemeCommandHandlerTest {

    @Mock
    private ChefRepository chefRepository;

    @Mock
    private MemeRepository memeRepository;

    private PostMemeCommandHandler postMemeCommandHandler;

    @Autowired
    private PasswordEncoder encoder;

    @Autowired
    private StorageUtil storageUtil;

    @Autowired
    private Validator validator;

    @BeforeEach
    void setUp() throws IOException {
        postMemeCommandHandler = new PostMemeCommandHandler(
                validator,
                memeRepository,
                chefRepository,
                storageUtil);

        FileSystemUtils.deleteRecursively(storageUtil.getStoragePath().getParent());
    }

    @AfterEach
    void tearDown() throws IOException {
        FileSystemUtils.deleteRecursively(storageUtil.getStoragePath().getParent());
    }

    @Test
    void validTextMemeShouldBeCreatedWithSuccessMessage() throws IOException {
        //given
        final var meme = Meme.builder()
                .title("Text Meme")
                .description("This is a text meme")
                .type(MemeType.TEXT)
                .build();

        when(chefRepository.findByName("test chef")).thenReturn(Optional.of(
                Chef.builder()
                        .id(UUID.randomUUID())
                        .name("test chef")
                        .email("test@gmail.com")
                        .password(encoder.encode("12345"))
                        .roles(List.of(Role.USER))
                        .build()
        ));

        final var input = new MemeRequest(meme, null);

        //when
        final var response = postMemeCommandHandler.execute(input);

        //then
        verify(memeRepository).save(any(Meme.class));
        assertThat(Objects.requireNonNull(response.getBody()).getResponse()).contains("Your meme uploaded successfully with id of:");
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    void validNonTextMemeShouldBeCreatedWithSuccessMessage() throws IOException {
        //given
        final var meme = Meme.builder()
                .title("Non-Text Meme")
                .description("This is not a text meme")
                .type(MemeType.IMAGE_JPEG)
                .build();

        when(chefRepository.findByName("test chef")).thenReturn(Optional.of(Chef.builder()
                .id(UUID.randomUUID())
                .name("test chef")
                .email("test@gmail.com")
                .password(encoder.encode("12345"))
                .roles(List.of(Role.USER))
                .build()
        ));

        var memeFile = mock(MultipartFile.class);
        final var bytes = new byte[]{1, 2, 3, 4, 5};
        when(memeFile.getBytes()).thenReturn(bytes);
        when(memeFile.getInputStream()).thenReturn(new ByteArrayInputStream(bytes));

        final var input = new MemeRequest(meme, memeFile);

        //when
        final var response = postMemeCommandHandler.execute(input);

        //then
        verify(memeRepository).save(any(Meme.class));
        assertThat(Objects.requireNonNull(response.getBody()).getResponse()).contains("Your meme uploaded successfully with id of:");
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    void nullMemeFileUploadShouldFailForNonTextMemes() {
        //given
        final var meme = Meme.builder()
                .title("Non-Text Meme")
                .description("This is not a text meme")
                .type(MemeType.IMAGE_JPEG)
                .build();

        when(chefRepository.findByName("test chef")).thenReturn(Optional.of(Chef.builder()
                .id(UUID.randomUUID())
                .name("test chef")
                .email("test@gmail.com")
                .password(encoder.encode("12345"))
                .roles(List.of(Role.USER))
                .build()
        ));

        final var input = new MemeRequest(meme, null);

        // when then
        assertThatThrownBy(() -> postMemeCommandHandler.execute(input))
                .isInstanceOf(InvalidInputException.class)
                .hasMessage("File cannot be null or empty when setting meme type to video, image etc");
    }
}