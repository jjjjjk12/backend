package com.ajoudev.backend.dto.comment.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class EditCommentDTO {
    @NotBlank(message = "댓글 내용을 입력해주세요")
    private String commentBody;
    private Long commentNum;
}
