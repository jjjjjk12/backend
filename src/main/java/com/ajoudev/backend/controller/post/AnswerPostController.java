package com.ajoudev.backend.controller.post;

import com.ajoudev.backend.dto.post.request.NewPostDTO;
import com.ajoudev.backend.dto.post.response.*;
import com.ajoudev.backend.exception.member.NotFoundUserException;
import com.ajoudev.backend.exception.post.PostingException;
import com.ajoudev.backend.service.post.PostingService;
import com.ajoudev.backend.service.post.QuestionPostingService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/answer")
@RequiredArgsConstructor
public class AnswerPostController {

    private final PostingService postingService;
    private final QuestionPostingService questionPostingService;

    @PostMapping("/create")
    public PostMessageDTO createAnswer(@RequestBody @Valid NewPostDTO postDTO, @RequestParam Long questionId,
                                       @PageableDefault(size = 5) Pageable pageable) {
        PostMessageDTO messageDTO;

        try {
            Slice<AnswerPageDTO> posts = questionPostingService.createAns(postDTO, questionId, pageable);
            messageDTO = PostMessageDTO.builder()
                    .status("success")
                    .answers(posts)
                    .build();
        }catch (RuntimeException e) {
            e.printStackTrace();
            messageDTO = PostMessageDTO.builder()
                    .status("error")
                    .message(e.getMessage())
                    .build();
        }

        return messageDTO;
    }

    @PostMapping("/edit")
    public PostMessageDTO edit(@RequestBody @Valid NewPostDTO postDTO,
                                       @RequestParam Long post) {
        PostMessageDTO messageDTO;

        try {
            ViewAnswerDTO answerDTO = questionPostingService.editAns(postDTO, post);
            messageDTO = PostMessageDTO.builder()
                    .status("success")
                    .answer(answerDTO)
                    .build();
        } catch (RuntimeException e) {
            e.printStackTrace();
            messageDTO = PostMessageDTO.builder()
                    .status("error")
                    .message(e.getMessage())
                    .build();
        }

        return messageDTO;
    }

    @PostMapping("/delete")
    public PostMessageDTO delete(@RequestParam Long post, @PageableDefault(size = 5) Pageable pageable) {
        PostMessageDTO messageDTO;

        try {
            questionPostingService.deleteAnswer(post, pageable);
            messageDTO = PostMessageDTO.builder()
                    .status("success")
                    .build();
        } catch (RuntimeException e) {
            e.printStackTrace();
            messageDTO = PostMessageDTO.builder()
                    .status("error")
                    .message(e.getMessage())
                    .build();
        }


        return messageDTO;
    }

    @GetMapping("/list")
    public PostMessageDTO viewList(@PageableDefault(size = 5) Pageable pageable,
                                   @RequestParam Long questionId) {
        PostMessageDTO messageDTO;

        try {
            Slice<AnswerPageDTO> answers = questionPostingService.viewAnswers(pageable, questionId);
            messageDTO = PostMessageDTO.builder()
                    .status("success")
                    .answers(answers)
                    .build();
        } catch (RuntimeException e) {
            e.printStackTrace();
            messageDTO = PostMessageDTO.builder()
                    .status("error")
                    .message(e.getMessage())
                    .build();
        }

        return messageDTO;
    }
}
