package com.amirali.memestream.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Pageable;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class SearchRequest {
    private String query;

    private Pageable pageable;
}
