package com.ajoudev.backend.controller.post;

import com.ajoudev.backend.dto.post.request.NewPostDTO;
import com.ajoudev.backend.dto.post.response.PostMessageDTO;
import com.ajoudev.backend.dto.post.response.ViewPostDTO;
import com.ajoudev.backend.exception.post.PostingException;
import com.ajoudev.backend.service.post.PostingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/normal")
@RequiredArgsConstructor
public class NormalPostController {

    private final PostingService postingService;

    @PostMapping("/create")
    public ResponseEntity<Object> createPost(@RequestBody NewPostDTO postDTO) {
        PostMessageDTO messageDTO;

        try {
            ViewPostDTO viewPostDTO = postingService.createNewNormalPost(postDTO);
            messageDTO = PostMessageDTO.builder()
                    .status("success")
                    .post(viewPostDTO)
                    .build();
        } catch (PostingException e) {
            e.printStackTrace();
            messageDTO = PostMessageDTO.builder()
                    .status("error")
                    .message("잘못된 입력값입니다")
                    .build();
            return ResponseEntity.ok().body(messageDTO);
        }

        return ResponseEntity.ok().body(messageDTO);
    }

    @GetMapping("/{postNum}")
    public ResponseEntity<Object> viewPost(@PathVariable Long postNum) {
        PostMessageDTO messageDTO;

        try {
            ViewPostDTO viewPostDTO = postingService.viewPost(postNum);
            messageDTO = PostMessageDTO.builder()
                    .status("success")
                    .post(viewPostDTO)
                    .build();
        } catch (PostingException e) {
            e.printStackTrace();
            messageDTO = PostMessageDTO.builder()
                    .status("error")
                    .message("존재하지 않는 게시글입니다")
                    .build();
            return ResponseEntity.ok().body(messageDTO);
        }

        return ResponseEntity.ok().body(messageDTO);
    }

    @PostMapping("/{postNum}/edit")
    public ResponseEntity<Object> editPost(@PathVariable Long postNum, @RequestBody NewPostDTO newPostDTO) {
        PostMessageDTO messageDTO;

        try {
            ViewPostDTO viewPostDTO = postingService.editPost(newPostDTO, postNum);
            messageDTO = PostMessageDTO.builder()
                    .status("success")
                    .post(viewPostDTO)
                    .build();
        } catch (PostingException e) {
            e.printStackTrace();
            messageDTO = PostMessageDTO.builder()
                    .status("error")
                    .message("잘못된 게시글 수정입니다")
                    .build();
            return ResponseEntity.ok().body(messageDTO);
        }

        return ResponseEntity.ok().body(messageDTO);
    }
}
