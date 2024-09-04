package com.ajoudev.backend.dto.post.response;

import com.ajoudev.backend.dto.comment.response.CommentPageDTO;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Slice;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PostMessageDTO {
    private String status;
    private String message;
    private ViewPostDTO post;
    private ViewAnswerDTO answer;
    private Page<PostPageDTO> posts;
    private Page<QuestionPageDTO> questions;
    private Page<AnswerPageDTO> answers;
    private Page<CommentPageDTO> comments;
}
