package com.ajoudev.backend.controller.like;


import com.ajoudev.backend.dto.comment.response.CommentMessageDTO;
import com.ajoudev.backend.dto.post.response.PostMessageDTO;
import com.ajoudev.backend.dto.post.response.ViewAnswerDTO;
import com.ajoudev.backend.dto.post.response.ViewPostDTO;
import com.ajoudev.backend.exception.like.DuplicatedLikeException;
import com.ajoudev.backend.exception.like.LikeException;
import com.ajoudev.backend.exception.member.NotFoundUserException;
import com.ajoudev.backend.service.like.LikeService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/like")
@RequiredArgsConstructor
public class LikeController {

    private final LikeService likeService;

    @GetMapping
    public PostMessageDTO create(@RequestParam Long post) {
        PostMessageDTO messageDTO;

        try {
            ViewPostDTO viewPostDTO = likeService.addLike(post);
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
        catch (LikeException e) {
            e.printStackTrace();
            messageDTO = PostMessageDTO.builder()
                    .status("error")
                    .message("잘못된 게시글 혹은 사용자입니다")
                    .build();

            return messageDTO;
        }

        return messageDTO;

    }

    @GetMapping("/answer")
    public PostMessageDTO createAns(@RequestParam Long post) {
        PostMessageDTO messageDTO;

        try {
            ViewAnswerDTO answerDTO = likeService.addLikeToAnswer(post);
            messageDTO = PostMessageDTO.builder()
                    .status("success")
                    .answer(answerDTO)
                    .build();
        } catch (NotFoundUserException e) {
            e.printStackTrace();
            messageDTO = PostMessageDTO.builder()
                    .status("error")
                    .message(e.getMessage())
                    .build();
        }
        catch (LikeException e) {
            e.printStackTrace();
            messageDTO = PostMessageDTO.builder()
                    .status("error")
                    .message("잘못된 게시글 혹은 사용자입니다")
                    .build();

            return messageDTO;
        }

        return messageDTO;

    }
}
