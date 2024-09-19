package com.memestream.api.service.commandhandlers;

import com.memestream.api.exceptions.ItemAlreadyExistsException;
import com.memestream.api.model.Chef;
import com.memestream.api.repository.ChefRepository;
import com.memestream.api.service.Validator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Objects;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CreateChefCommandHandlerTest {

    @Mock
    private ChefRepository chefRepository;

    private CreateChefCommandHandler createChefCommandHandler;

    @BeforeEach
    void setUp() {
        createChefCommandHandler = new CreateChefCommandHandler(new Validator(), chefRepository, new BCryptPasswordEncoder());
    }

    @Test
    void validChefShouldBeCreatedWithSuccessMessage() {
        //given
        final var chef = Chef.builder()
                .name("Test chef")
                .email("test@gmail.com")
                .password("12345")
                .build();

        //when
        final var response = createChefCommandHandler.execute(chef);

        //then
        verify(chefRepository).save(any(Chef.class));
        assertThat(Objects.requireNonNull(response.getBody()).getResponse()).isEqualTo("Success");
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }


    @Test
    void chefWithDuplicateNameShouldFail() {
        //given
        final var chef = Chef.builder()
                .name("Test chef")
                .email("test@gmail.com")
                .password("12345")
                .build();

        when(chefRepository.existsByName(anyString())).thenReturn(true);

        //when then
        assertThatThrownBy(() -> createChefCommandHandler.execute(chef))
                .isInstanceOf(ItemAlreadyExistsException.class)
                .hasMessage(chef.getName() + " already exists");
    }
}