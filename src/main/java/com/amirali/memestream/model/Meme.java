package com.amirali.memestream.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "memes")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Meme {

    @Id
    private UUID id;

    @Column(name = "title")
    private String title;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @Column(name = "meme_path")
    @JsonIgnore
    private String memePath;

    @Enumerated(EnumType.STRING)
    @Column(name = "type")
    private MemeType type;

    @ManyToOne
    @JoinColumn(name = "chef_id")
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Chef chef;

    @OneToMany(mappedBy = "meme", cascade = CascadeType.REMOVE)
    @JsonIgnore
    private List<Like> likes;

    @Transient
    @JsonProperty("likes")
    private int likesSize;
}
