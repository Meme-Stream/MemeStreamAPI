package com.amirali.memestream.service;

import com.amirali.memestream.exceptions.InvalidInputException;
import com.amirali.memestream.model.*;
import io.micrometer.common.util.StringUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class Validator {

    public void validateChefInput(Chef chef) {
        if (StringUtils.isBlank(chef.getName()))
            throw new InvalidInputException(new SimpleResponse("Name cannot be null or contain whitespace"));
        if (StringUtils.isBlank(chef.getEmail()))
            throw new InvalidInputException(new SimpleResponse("Email cannot be null or contain whitespace"));
        if (StringUtils.isBlank(chef.getPassword()))
            throw new InvalidInputException(new SimpleResponse("Password cannot be null or contain whitespace"));
        if (chef.getPassword().length() <= 4)
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

    public void validateMemeRequest(Meme meme, boolean editMode) {
        if (editMode) {
            if (meme.getId() == null) {
                throw new InvalidInputException(new SimpleResponse("Id cannot be null"));
            }
        }else {
            if (StringUtils.isBlank(meme.getTitle()))
                throw new InvalidInputException(new SimpleResponse("Title cannot be null or contain whitespace"));
            if (StringUtils.isBlank(meme.getDescription()))
                throw new InvalidInputException(new SimpleResponse("Description cannot be null or contain whitespace"));
        }
        if (meme.getType() == null)
            throw new InvalidInputException(new SimpleResponse("Type cannot be null"));
    }
}
