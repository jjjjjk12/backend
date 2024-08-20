package com.ajoudev.backend.repository.post;

import com.ajoudev.backend.dto.post.response.PostPageDTO;
import com.ajoudev.backend.dto.search.request.SearchPostDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface SearchingRepository {
    public Page<PostPageDTO> searchByRequest(SearchPostDTO filterDTO, Pageable pageable);
}
