package com.amirali.memestream.service.commandhandlers;

import com.amirali.memestream.exceptions.ItemAlreadyExistsException;
import com.amirali.memestream.model.Chef;
import com.amirali.memestream.repository.ChefRepository;
import com.amirali.memestream.service.Validator;
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
import static org.mockito.ArgumentMatchers.anyString;
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
    void createChef_returnsSuccessMessage() {
        //given
        final var chef = new Chef();
        chef.setName("Test Chef");
        chef.setPassword("12345");
        chef.setEmail("test@gmail.com");

        //when
        final var response = createChefCommandHandler.execute(chef);

        //then
        assertThat(Objects.requireNonNull(response.getBody()).getResponse()).isEqualTo("Success");
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }


    @Test
    void createChef_returnsItemAlreadyExistsException() {
        //given
        final var chef = new Chef();
        chef.setName("Test Chef");
        chef.setPassword("12345");
        chef.setEmail("test@gmail.com");

        when(chefRepository.existsByName(anyString())).thenReturn(true);

        //when then
        assertThatThrownBy(() -> createChefCommandHandler.execute(chef))
                .isInstanceOf(ItemAlreadyExistsException.class)
                .hasMessageContaining(chef.getName() + " already exists");
    }
}