package com.amirali.memestream.repository;

import com.amirali.memestream.model.Meme;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface MemeRepository extends JpaRepository<Meme, UUID> {
}
