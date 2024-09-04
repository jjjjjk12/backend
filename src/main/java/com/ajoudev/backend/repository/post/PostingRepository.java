package com.ajoudev.backend.repository.post;

import com.ajoudev.backend.dto.post.response.AnswerPageDTO;
import com.ajoudev.backend.dto.post.response.PostPageDTO;
import com.ajoudev.backend.dto.post.response.QuestionPageDTO;
import com.ajoudev.backend.dto.post.response.ViewAnswerDTO;
import com.ajoudev.backend.entity.member.Member;
import com.ajoudev.backend.entity.post.QuestionPost;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

public interface PostingRepository {
    public Page<PostPageDTO> searchLikedPostsPage(Member user, Pageable pageable);
    public Page<PostPageDTO> searchMyPostsPage(Member user, Pageable pageable, String board);
    public Page<QuestionPageDTO> searchQuestions(Pageable pageable);
    public Page<AnswerPageDTO> searchAnswers(Pageable pageable, QuestionPost parent, Member user);
    public ViewAnswerDTO findAnswerByUserAndPostNum(Member user, Long postNum);
}
