package com.ajoudev.backend.dto.post.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class NewPostDTO {
    private String title;
    private String textBody;
}
