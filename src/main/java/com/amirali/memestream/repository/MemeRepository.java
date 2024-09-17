package com.amirali.memestream.repository;

import com.amirali.memestream.model.Meme;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface MemeRepository extends JpaRepository<Meme, UUID> {

    @Query("""
            select m from Meme m where
                lower(m.title) like concat('%', lower(:query), '%') or
                lower(m.description) like concat('%', lower(:query), '%') or
                lower(m.chef.name) like concat('%', lower(:query), '%')
            """)
    Page<Meme> search(String query, Pageable pageable);
}
