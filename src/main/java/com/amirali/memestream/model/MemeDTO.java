package com.amirali.memestream.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MemeDTO {

    private UUID id;

    private String title;

    private String description;

    @JsonProperty("chef_name")
    private String chefName;

    private MemeType type = MemeType.TEXT;
}
