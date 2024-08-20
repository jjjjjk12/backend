package com.ajoudev.backend.controller.search;

import com.ajoudev.backend.dto.post.response.PostMessageDTO;
import com.ajoudev.backend.dto.search.request.SearchPostDTO;
import com.ajoudev.backend.service.search.SearchingService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/search")
public class SearchController {

    private final SearchingService searchingService;

    @GetMapping("/posts")
    public PostMessageDTO viewList(@PageableDefault(size = 10) Pageable pageable,
                                   @RequestBody @Valid SearchPostDTO postDTO) {
        return PostMessageDTO.builder()
                .status("success")
                .posts(searchingService.searchPosts(postDTO, pageable))
                .build();
    }

}
