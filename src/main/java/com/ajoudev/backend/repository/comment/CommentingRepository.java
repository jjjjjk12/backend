package com.ajoudev.backend.repository.comment;

import com.ajoudev.backend.dto.comment.response.CommentPageDTO;
import com.ajoudev.backend.dto.comment.response.MyCommentPageDTO;
import com.ajoudev.backend.entity.member.Member;
import com.ajoudev.backend.entity.post.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CommentingRepository {
    public Page<CommentPageDTO> searchPage(Post post, Pageable pageable);
    public Page<MyCommentPageDTO> searchMyCommentPage(Member user, Pageable pageable);
}
