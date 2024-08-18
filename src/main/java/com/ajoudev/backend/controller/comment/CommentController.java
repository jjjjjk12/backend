package com.ajoudev.backend.controller.comment;

import com.ajoudev.backend.dto.comment.request.EditCommentDTO;
import com.ajoudev.backend.dto.comment.request.NewCommentDTO;
import com.ajoudev.backend.dto.comment.response.CommentMessageDTO;
import com.ajoudev.backend.dto.comment.response.CommentPageDTO;
import com.ajoudev.backend.dto.post.response.PostMessageDTO;
import com.ajoudev.backend.service.comment.CommentingService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/comment")
public class CommentController {

    private final CommentingService commentingService;

    @PostMapping("/create")
    public CommentMessageDTO create(@RequestParam Long post,
                       @RequestBody @Valid NewCommentDTO commentDTO,
                       @PageableDefault(size = 20) Pageable pageable) {
        CommentMessageDTO messageDTO;

        try {
            Page<CommentPageDTO> comments = commentingService.createComment(commentDTO, pageable, post);
            messageDTO = CommentMessageDTO.builder()
                    .status("success")
                    .comments(comments)
                    .build();
        } catch (RuntimeException e) {
            e.printStackTrace();
            messageDTO = CommentMessageDTO.builder()
                    .status("error")
                    .message("댓글을 작성할 수 없습니다")
                    .build();
            return messageDTO;
        }
        return messageDTO;
    }

    @PostMapping("/edit")
    public CommentMessageDTO edit(@RequestBody @Valid EditCommentDTO commentDTO,
                     @PageableDefault(size = 20) Pageable pageable) {
        CommentMessageDTO messageDTO;

        try {
            Page<CommentPageDTO> comments = commentingService.editComment(commentDTO, pageable);
            messageDTO = CommentMessageDTO.builder()
                    .status("success")
                    .comments(comments)
                    .build();
        } catch (RuntimeException e) {
            e.printStackTrace();
            messageDTO = CommentMessageDTO.builder()
                    .status("error")
                    .message("댓글을 수정할 수 없습니다")
                    .build();
            return messageDTO;
        }
        return messageDTO;
    }
    
    @GetMapping("/list")
    public CommentMessageDTO view(@RequestParam Long post, @PageableDefault(size = 20) Pageable pageable) {
        CommentMessageDTO messageDTO;

        try {
            Page<CommentPageDTO> comments = commentingService.findByPage(pageable, post);
            messageDTO = CommentMessageDTO.builder()
                    .status("success")
                    .comments(comments)
                    .build();
        } catch (RuntimeException e) {
            e.printStackTrace();
            messageDTO = CommentMessageDTO.builder()
                    .status("error")
                    .message("댓글을 가져올 수 없습니다")
                    .build();
            return messageDTO;
        }
        return messageDTO;
    }
}
