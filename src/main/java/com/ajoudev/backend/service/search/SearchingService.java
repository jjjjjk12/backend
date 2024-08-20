package com.ajoudev.backend.service.search;

import com.ajoudev.backend.dto.post.response.PostPageDTO;
import com.ajoudev.backend.dto.search.request.SearchPostDTO;
import com.ajoudev.backend.repository.post.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@RequiredArgsConstructor
@Service
@Transactional
public class SearchingService {

    private final PostRepository postRepository;

    public Page<PostPageDTO> searchPosts(SearchPostDTO postDTO, Pageable pageable) {
        parseKeyword(postDTO);

        return postRepository.searchByRequest(postDTO, pageable);
    }

    private void parseKeyword(SearchPostDTO postDTO) {
        List<String> tmp = new ArrayList<>(Arrays.stream(postDTO.getKeyword().split("\\s+")).toList());
        postDTO.setParse(tmp);
    }

}
