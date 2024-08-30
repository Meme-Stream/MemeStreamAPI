package com.amirali.memestream.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "chefs")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Chef {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "name")
    private String name;

    @Column(name = "email")
    private String email;

    @Column(name = "password")
    private String password;

    @OneToMany(mappedBy = "chef")
    private List<Meme> memes;

    public Chef(ChefDTO chefDTO) {
        this.name = chefDTO.getName();
        this.email = chefDTO.getEmail();
        this.password = chefDTO.getPassword();
    }
}
