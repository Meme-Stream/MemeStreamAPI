package com.amirali.memestream.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Entity
@Table(name = "memes")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Meme {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private UUID id;

    @Column(name = "title")
    private String title;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @Lob
    @Column(name = "meme", columnDefinition = "MEDIUMBLOB")
    @JsonIgnore
    private byte[] meme;

    @Enumerated(EnumType.STRING)
    @Column(name = "type")
    private MemeType type;

    @ManyToOne
    @JoinColumn(name = "chef_id")
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Chef chef;
}
