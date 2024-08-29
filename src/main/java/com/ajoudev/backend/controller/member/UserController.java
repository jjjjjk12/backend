package com.ajoudev.backend.controller.member;

import com.ajoudev.backend.dto.comment.response.CommentMessageDTO;
import com.ajoudev.backend.dto.comment.response.MyCommentPageDTO;
import com.ajoudev.backend.dto.member.EditUserDTO;
import com.ajoudev.backend.dto.member.RegistrationMessageDTO;
import com.ajoudev.backend.dto.member.UserDTO;
import com.ajoudev.backend.dto.member.UserRegistrationDTO;
import com.ajoudev.backend.dto.post.response.PostMessageDTO;
import com.ajoudev.backend.dto.post.response.PostPageDTO;
import com.ajoudev.backend.exception.member.NotFoundUserException;
import com.ajoudev.backend.service.comment.CommentingService;
import com.ajoudev.backend.service.member.UserService;
import com.ajoudev.backend.service.post.PostingService;
import lombok.RequiredArgsConstructor;
import org.apache.catalina.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/member")
public class UserController {

    private final UserService userService;

    @GetMapping
    public RegistrationMessageDTO view(@RequestParam String user) {
        RegistrationMessageDTO messageDTO;

        try {
            UserDTO userDTO = userService.viewInfo(user);
            messageDTO = RegistrationMessageDTO.builder()
                    .status("success")
                    .user(userDTO)
                    .build();
        } catch (RuntimeException e) {
            e.printStackTrace();
            messageDTO = RegistrationMessageDTO.builder()
                    .status("error")
                    .message(e.getMessage())
                    .build();
            return messageDTO;
        }

        return messageDTO;
    }

    @PostMapping("/delete")
    public RegistrationMessageDTO delete() {
        RegistrationMessageDTO messageDTO;

        try {
            userService.delete();
            messageDTO = RegistrationMessageDTO.builder()
                    .status("success")
                    .build();
        }
        catch (NotFoundUserException e) {
            e.printStackTrace();
            messageDTO = RegistrationMessageDTO.builder()
                    .status("error")
                    .message(e.getMessage())
                    .build();
        }
        catch (RuntimeException e) {
            e.printStackTrace();
            messageDTO = RegistrationMessageDTO.builder()
                    .status("error")
                    .message("회원을 탈퇴할 수 없습니다")
                    .build();
            return messageDTO;
        }

        return messageDTO;
    }

    @PostMapping("/edit")
    public RegistrationMessageDTO edit(@RequestBody EditUserDTO editUserDTO) {
        RegistrationMessageDTO messageDTO;

        try {
            UserDTO userDTO = userService.edit(editUserDTO);
            messageDTO = RegistrationMessageDTO.builder()
                    .status("success")
                    .user(userDTO)
                    .build();
        } catch (RuntimeException e) {
            e.printStackTrace();
            messageDTO = RegistrationMessageDTO.builder()
                    .status("error")
                    .message(e.getMessage())
                    .build();
            return messageDTO;
        }

        return messageDTO;
    }

    @GetMapping("/posts")
    public PostMessageDTO viewPosts(@PageableDefault(size = 10) Pageable pageable,
                                    @RequestParam String user,
                                    @RequestParam(required = false) String board) {
        PostMessageDTO messageDTO;

        try {
            Page<PostPageDTO> posts = userService.viewPosts(user, pageable, board);
            messageDTO = PostMessageDTO.builder()
                    .status("success")
                    .posts(posts)
                    .build();
        } catch (RuntimeException e) {
            e.printStackTrace();
            messageDTO = PostMessageDTO.builder()
                    .status("error")
                    .message(e.getMessage())
                    .build();
            return messageDTO;
        }

        return messageDTO;
    }

    @GetMapping("/likes")
    public PostMessageDTO viewLikes(@PageableDefault(size = 10) Pageable pageable) {
        PostMessageDTO messageDTO;

        try {
            Page<PostPageDTO> posts = userService.viewLiked(pageable);
            messageDTO = PostMessageDTO.builder()
                    .status("success")
                    .posts(posts)
                    .build();
        } catch (RuntimeException e) {
            e.printStackTrace();
            messageDTO = PostMessageDTO.builder()
                    .status("error")
                    .message(e.getMessage())
                    .build();
            return messageDTO;
        }

        return messageDTO;
    }

    @GetMapping("/comments")
    public CommentMessageDTO viewComments(@PageableDefault(size = 10) Pageable pageable,
                                       @RequestParam String user) {
        CommentMessageDTO messageDTO;

        try {
            Page<MyCommentPageDTO> comments = userService.viewComments(user, pageable);
            messageDTO = CommentMessageDTO.builder()
                    .status("success")
                    .comments(comments)
                    .build();
        } catch (RuntimeException e) {
            e.printStackTrace();
            messageDTO = CommentMessageDTO.builder()
                    .status("error")
                    .message(e.getMessage())
                    .build();
            return messageDTO;
        }

        return messageDTO;
    }
}
