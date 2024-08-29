package com.ajoudev.backend.repository.post;

import com.ajoudev.backend.entity.post.AnswerPost;
import org.springframework.data.jpa.repository.JpaRepository;

public interface QuestionPostingRepository extends JpaRepository<AnswerPost, Long> {
}
