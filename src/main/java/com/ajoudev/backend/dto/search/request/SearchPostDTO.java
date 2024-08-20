package com.ajoudev.backend.dto.search.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SearchPostDTO {

    @NotBlank
    @Size(min = 1, max = 50)
    protected String keyword;
    protected Integer title;
    protected Integer text;
    protected Integer user;
    protected String board;
    protected List<String> parse = new ArrayList<>();

}
