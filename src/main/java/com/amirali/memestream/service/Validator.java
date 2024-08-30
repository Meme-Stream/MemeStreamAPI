package com.amirali.memestream.service;

import com.amirali.memestream.exceptions.InvalidInputException;
import com.amirali.memestream.exceptions.ItemAlreadyExistsException;
import com.amirali.memestream.model.MemeDTO;
import com.amirali.memestream.model.SimpleResponse;
import com.amirali.memestream.model.AuthRequest;
import com.amirali.memestream.model.ChefDTO;
import com.amirali.memestream.repository.ChefRepository;
import io.micrometer.common.util.StringUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class Validator {

    private final ChefRepository chefRepository;

    public void validateChefDTOInput(ChefDTO chefDTO) {
        if (StringUtils.isBlank(chefDTO.getName()))
            throw new InvalidInputException(new SimpleResponse("Name cannot be null or contain whitespace"));
        if (chefRepository.existsByName(chefDTO.getName()))
            throw new ItemAlreadyExistsException(new SimpleResponse(chefDTO.getName() + " already exists"));
        if (StringUtils.isBlank(chefDTO.getEmail()))
            throw new InvalidInputException(new SimpleResponse("Email cannot be null or contain whitespace"));
        if (StringUtils.isBlank(chefDTO.getPassword()))
            throw new InvalidInputException(new SimpleResponse("Password cannot be null or contain whitespace"));
        if (chefDTO.getPassword().length() <= 4)
            throw new InvalidInputException(new SimpleResponse("Password cannot be less than or equal 4 characters"));
    }

    public void validateChefAuthRequest(AuthRequest authRequest) {
        if (StringUtils.isBlank(authRequest.getName()))
            throw new InvalidInputException(new SimpleResponse("Name cannot be null or contain whitespace"));
        if (StringUtils.isBlank(authRequest.getPassword()))
            throw new InvalidInputException(new SimpleResponse("Password cannot be null or contain whitespace"));
        if (authRequest.getPassword().length() <= 4)
            throw new InvalidInputException(new SimpleResponse("Password cannot be less than or equal 4 characters"));
    }

    public void validateMemeDTOPInput(MemeDTO memeDTO) {
        if (StringUtils.isBlank(memeDTO.getTitle()))
            throw new InvalidInputException(new SimpleResponse("Title cannot be null or contain whitespace"));
        if (StringUtils.isBlank(memeDTO.getDescription()))
            throw new InvalidInputException(new SimpleResponse("Description cannot be null or contain whitespace"));
    }
}
