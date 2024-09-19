package com.memestream.api.repository;

import com.memestream.api.model.Chef;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface ChefRepository extends JpaRepository<Chef, UUID> {

    boolean existsByName(String name);

    Optional<Chef> findByName(String name);
}
