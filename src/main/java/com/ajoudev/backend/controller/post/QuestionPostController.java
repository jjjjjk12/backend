package com.ajoudev.backend.controller.post;

import com.ajoudev.backend.dto.comment.response.CommentPageDTO;
import com.ajoudev.backend.dto.post.request.NewPostDTO;
import com.ajoudev.backend.dto.post.response.AnswerPageDTO;
import com.ajoudev.backend.dto.post.response.PostMessageDTO;
import com.ajoudev.backend.dto.post.response.QuestionPageDTO;
import com.ajoudev.backend.dto.post.response.ViewPostDTO;
import com.ajoudev.backend.exception.member.NotFoundUserException;
import com.ajoudev.backend.exception.post.PostingException;
import com.ajoudev.backend.service.comment.CommentingService;
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
@RequestMapping("/api/question")
@RequiredArgsConstructor
public class QuestionPostController {

    private final PostingService postingService;
    private final QuestionPostingService questionPostingService;

    @PostMapping("/create")
    public PostMessageDTO createQuestion(@RequestBody @Valid NewPostDTO postDTO) {
        PostMessageDTO messageDTO;

        try {
            ViewPostDTO viewPostDTO = postingService.createNewPost(postDTO, "Question");
            messageDTO = PostMessageDTO.builder()
                    .status("success")
                    .post(viewPostDTO)
                    .build();
        }catch (NotFoundUserException e) {
            e.printStackTrace();
            messageDTO = PostMessageDTO.builder()
                    .status("error")
                    .message(e.getMessage())
                    .build();
        }
        catch (PostingException e) {
            e.printStackTrace();
            messageDTO = PostMessageDTO.builder()
                    .status("error")
                    .message("잘못된 입력값입니다")
                    .build();
        }

        return messageDTO;
    }

    @PostMapping("/edit")
    public PostMessageDTO editQuestion(@RequestBody @Valid NewPostDTO postDTO,
                                       @RequestParam Long post) {
        PostMessageDTO messageDTO;

        try {
            ViewPostDTO viewPostDTO = postingService.editPost(postDTO, post);
            messageDTO = PostMessageDTO.builder()
                    .status("success")
                    .post(viewPostDTO)
                    .build();
        } catch (NotFoundUserException e) {
            e.printStackTrace();
            messageDTO = PostMessageDTO.builder()
                    .status("error")
                    .message(e.getMessage())
                    .build();
        }
        catch (PostingException e) {
            e.printStackTrace();
            messageDTO = PostMessageDTO.builder()
                    .status("error")
                    .message("잘못된 게시글 수정입니다")
                    .build();
        }

        return messageDTO;
    }

    @PostMapping("/delete")
    public PostMessageDTO delete(@RequestParam Long post) {
        PostMessageDTO messageDTO;

        try {
            questionPostingService.deleteQuestion(post);
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

    @GetMapping
    public ResponseEntity<Object> viewPost(@RequestParam Long post, @PageableDefault(size = 5) Pageable pageable) {
        PostMessageDTO messageDTO;

        try {
            ViewPostDTO viewPostDTO = postingService.viewPost(post);
            Slice<AnswerPageDTO> answers = questionPostingService.viewAnswers(pageable, post);
            messageDTO = PostMessageDTO.builder()
                    .status("success")
                    .post(viewPostDTO)
                    .answers(answers)
                    .build();
        } catch (RuntimeException e) {
            e.printStackTrace();
            messageDTO = PostMessageDTO.builder()
                    .status("error")
                    .message(e.getMessage())
                    .build();
        }


        return ResponseEntity.ok().body(messageDTO);
    }

    @GetMapping("/list")
    public PostMessageDTO viewList(@PageableDefault(size = 10, sort = "postingDate", direction = Sort.Direction.DESC)Pageable pageable) {
        PostMessageDTO messageDTO;

        try {
            Page<QuestionPageDTO> questions = questionPostingService.viewQuestions(pageable);
            messageDTO = PostMessageDTO.builder()
                    .status("success")
                    .questions(questions)
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
