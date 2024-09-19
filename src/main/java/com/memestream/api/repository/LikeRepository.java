package com.memestream.api.repository;

import com.memestream.api.model.Like;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface LikeRepository extends JpaRepository<Like, UUID> {

    boolean existsByChefName(String name);

    Optional<Like> findByMemeIdAndChefName(UUID id, String name);
}
