package com.ajoudev.backend.controller.post;

import com.ajoudev.backend.dto.post.request.NewPostDTO;
import com.ajoudev.backend.dto.post.response.PostMessageDTO;
import com.ajoudev.backend.dto.post.response.ViewPostDTO;
import com.ajoudev.backend.exception.post.PostingException;
import com.ajoudev.backend.service.post.PostingService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
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

    @GetMapping
    public ResponseEntity<Object> viewPost(@RequestParam Long post) {
        PostMessageDTO messageDTO;

        try {
            ViewPostDTO viewPostDTO = postingService.viewPost(post);
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

    @PostMapping("/edit")
    public ResponseEntity<Object> editPost(@RequestParam Long post, @RequestBody NewPostDTO newPostDTO) {
        PostMessageDTO messageDTO;

        try {
            ViewPostDTO viewPostDTO = postingService.editPost(newPostDTO, post);
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

    @GetMapping("/list")
    public PostMessageDTO viewList(@PageableDefault(size = 10, sort = "postingDate", direction = Sort.Direction.DESC)Pageable pageable) {
        return PostMessageDTO.builder()
                .status("success")
                .posts(postingService.findAll(pageable))
                .build();
    }
}
