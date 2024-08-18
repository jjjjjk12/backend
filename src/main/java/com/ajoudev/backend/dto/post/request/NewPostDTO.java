package com.ajoudev.backend.dto.post.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class NewPostDTO {
    @NotBlank(message = "제목을 입력해주세요")
    private String title;
    @NotBlank(message = "본문을 입력해주세요")
    private String textBody;
}
