package com.amirali.memestream.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChefDTO {
    private String name;

    private String email;

    private String password;
}
