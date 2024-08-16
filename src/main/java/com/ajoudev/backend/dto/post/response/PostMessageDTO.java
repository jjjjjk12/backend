package com.ajoudev.backend.dto.post.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PostMessageDTO {
    private String status;
    private String message;
    private ViewPostDTO post;
    private Page<PostPageDTO> posts;
}
