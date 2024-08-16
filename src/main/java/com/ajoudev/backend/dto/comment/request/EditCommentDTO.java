package com.ajoudev.backend.dto.comment.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class EditCommentDTO {
    private String commentBody;
    private Long commentNum;
}
