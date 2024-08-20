package com.ajoudev.backend.repository.post;

import com.ajoudev.backend.dto.post.response.PostPageDTO;
import com.ajoudev.backend.entity.member.Member;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface PostingRepository {
    public Page<PostPageDTO> searchLikedPostsPage(Member user, Pageable pageable);
    public Page<PostPageDTO> searchMyPostsPage(Member user, Pageable pageable);
}
