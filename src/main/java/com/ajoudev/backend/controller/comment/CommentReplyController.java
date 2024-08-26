package com.ajoudev.backend.controller.comment;

import com.ajoudev.backend.dto.comment.request.NewCommentReplyDTO;
import com.ajoudev.backend.dto.comment.response.CommentMessageDTO;
import com.ajoudev.backend.dto.comment.response.CommentPageDTO;
import com.ajoudev.backend.exception.member.NotFoundUserException;
import com.ajoudev.backend.service.comment.CommentReplyingService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/comment/reply")
public class CommentReplyController {

    private final CommentReplyingService commentReplyingService;

    @PostMapping("/create")
    public CommentMessageDTO create(@RequestBody @Valid NewCommentReplyDTO commentDTO,
                                    @PageableDefault(size = 20) Pageable pageable) {
        CommentMessageDTO messageDTO;

        try {
            Page<CommentPageDTO> comments = commentReplyingService.createReply(commentDTO, pageable);
            messageDTO = CommentMessageDTO.builder()
                    .status("success")
                    .comments(comments)
                    .build();
        } catch (NotFoundUserException e) {
            e.printStackTrace();
            messageDTO = CommentMessageDTO.builder()
                    .status("error")
                    .message(e.getMessage())
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

}
