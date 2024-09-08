package com.amirali.memestream.service.commandhandlers;

import com.amirali.memestream.exceptions.SimpleException;
import com.amirali.memestream.model.Chef;
import com.amirali.memestream.model.Meme;
import com.amirali.memestream.model.MemeType;
import com.amirali.memestream.model.Role;
import com.amirali.memestream.repository.MemeRepository;
import com.amirali.memestream.securitytest.WithMockChef;
import com.amirali.memestream.service.StorageUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;

import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@SpringBootTest
class DeleteMemeByIdCommandHandlerTest {

    @Mock
    private MemeRepository memeRepository;

    private DeleteMemeByIdCommandHandler deleteMemeByIdCommandHandler;

    @Autowired
    private StorageUtil storageUtil;

    @BeforeEach
    void setup() {
        deleteMemeByIdCommandHandler = new DeleteMemeByIdCommandHandler(memeRepository);
    }

    @Test
    @WithMockChef(roles = {Role.USER, Role.ADMIN})
    void deleteAnExistingMemeByAnAdminShouldReturnSuccessMessage() throws IOException {
        //given
        final var id = UUID.randomUUID();

        when(memeRepository.findById(id)).thenReturn(Optional.of(
                new Meme(
                        id,
                        "Test meme",
                        "This is a test meme",
                        null,
                        MemeType.TEXT,
                        Chef.builder()
                                .name("test chef")
                                .roles(List.of(Role.USER, Role.ADMIN))
                                .build()
                )
        ));
        //when
        var response = deleteMemeByIdCommandHandler.execute(id);

        //then
        verify(memeRepository).delete(any(Meme.class));
        assertThat(Objects.requireNonNull(response.getBody()).getResponse()).isEqualTo("Meme successfully deleted");
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    @WithMockChef
    void deleteAnExistingMemeByAuthorShouldReturnSuccessMessage() throws IOException {
        //given
        final var id = UUID.randomUUID();

        when(memeRepository.findById(id)).thenReturn(Optional.of(
                new Meme(
                        id,
                        "Test meme",
                        "This is a test meme",
                        null,
                        MemeType.TEXT,
                        Chef.builder()
                                .name("test chef")
                                .roles(List.of(Role.USER))
                                .build()
                )
        ));
        //when
        var response = deleteMemeByIdCommandHandler.execute(id);

        //then
        verify(memeRepository).delete(any(Meme.class));
        assertThat(Objects.requireNonNull(response.getBody()).getResponse()).isEqualTo("Meme successfully deleted");
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    @WithMockChef
    void deleteAnExistingMemeByAnotherUserShouldFail() throws IOException {
        //given
        final var id = UUID.randomUUID();

        when(memeRepository.findById(id)).thenReturn(Optional.of(
                new Meme(
                        id,
                        "Test meme",
                        "This is a test meme",
                        null,
                        MemeType.TEXT,
                        Chef.builder()
                                .name("test chef 2")
                                .roles(List.of(Role.USER))
                                .build()
                )
        ));
        //when
        assertThatThrownBy(() -> deleteMemeByIdCommandHandler.execute(id))
                .isInstanceOf(SimpleException.class)
                .hasMessage("You can't delete a meme that isn't yours")
                .extracting("status")
                .isEqualTo(HttpStatus.FORBIDDEN);
    }

    @Test
    @WithMockChef
    void deleteAnExistingNonTextMemeShouldReturnSuccessMessage() throws IOException {
        //given
        final var id = UUID.randomUUID();

        when(memeRepository.findById(id)).thenReturn(Optional.of(
                new Meme(
                        id,
                        "Test meme",
                        "This is a test meme",
                        storageUtil.getMemePath(id, MemeType.IMAGE_JPEG).toString(),
                        MemeType.IMAGE_JPEG,
                        Chef.builder()
                                .name("test chef")
                                .roles(List.of(Role.USER))
                                .build()
                )
        ));
        //when
        var response = deleteMemeByIdCommandHandler.execute(id);

        //then
        verify(memeRepository).delete(any(Meme.class));
        assertThat(Objects.requireNonNull(response.getBody()).getResponse()).isEqualTo("Meme successfully deleted");
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }
}