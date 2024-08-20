package com.ajoudev.backend.service.member;

import com.ajoudev.backend.dto.comment.response.MyCommentPageDTO;
import com.ajoudev.backend.dto.member.EditUserDTO;
import com.ajoudev.backend.dto.member.RegistrationMessageDTO;
import com.ajoudev.backend.dto.member.UserDTO;
import com.ajoudev.backend.dto.member.UserRegistrationDTO;
import com.ajoudev.backend.dto.post.response.PostPageDTO;
import com.ajoudev.backend.entity.member.Member;
import com.ajoudev.backend.exception.member.MemberException;
import com.ajoudev.backend.exception.member.NotFoundUserException;
import com.ajoudev.backend.repository.comment.CommentRepository;
import com.ajoudev.backend.repository.like.LikeRepository;
import com.ajoudev.backend.repository.member.MemberRepository;
import com.ajoudev.backend.repository.post.PostRepository;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
@Transactional
public class UserService {

    private final MemberRepository memberRepository;
    private final PostRepository postRepository;
    private final CommentRepository commentRepository;
    private final LikeRepository likeRepository;
    private final BCryptPasswordEncoder encoder;

    public void delete() throws MemberException {
        String id = SecurityContextHolder.getContext().getAuthentication().getName();
        Member user = memberRepository.findByUserid(id).orElse(null);
        if (user == null) throw new NotFoundUserException();


        //좋아요 삭제
        likeRepository.deleteByUser(user);

        //댓글 단 게시글의 댓글 수 조정 후 작성 게시글 null 처리
        postRepository.updateCommentsByUser(user);
        postRepository.updateNullByUser(user);

        //댓글 처리
        commentRepository.updateNullByUser(user.getUuid());
        commentRepository.deleteByUser(user);
        commentRepository.deleteAllNulls();

        memberRepository.delete(user);
    }

    public UserDTO edit(EditUserDTO userDTO) throws MemberException {
        String id = SecurityContextHolder.getContext().getAuthentication().getName();
        Member user = memberRepository.findByUserid(id).orElse(null);
        if (user == null) throw new NotFoundUserException("회원 정보를 수정할 수 없습니다");
        if (validatePW(userDTO.getPassword(), user)) throw new MemberException("잘못된 비밀번호입니다");


        if (memberRepository.existsByUserid(userDTO.getId())) {
            throw new MemberException("중복된 아이디입니다");
        }
        for (ConstraintViolation<EditUserDTO> violation : Validation.buildDefaultValidatorFactory().getValidator().validate(userDTO)) {
            if(violation.getInvalidValue() == null) continue;
            throw new MemberException(violation.getMessage());
        }

        user.changInfo(userDTO, encoder);
        return user.toUserDTO();
    }

    public Page<MyCommentPageDTO> viewComments(String userId, Pageable pageable) {
        Member user = memberRepository.findByUserid(userId).orElse(null);
        if (user == null) throw new NotFoundUserException("존재하지 않는 회원입니다");

        return commentRepository.searchMyCommentPage(user, pageable);
    }

    public Page<PostPageDTO> viewPosts(String userId, Pageable pageable) {
        Member user = memberRepository.findByUserid(userId).orElse(null);
        if (user == null) throw new NotFoundUserException("존재하지 않는 회원입니다");

        return postRepository.searchMyPostsPage(user, pageable);
    }

    public Page<PostPageDTO> viewLiked(Pageable pageable) {
        String id = SecurityContextHolder.getContext().getAuthentication().getName();
        Member user = memberRepository.findByUserid(id).orElse(null);
        if (user == null) throw new NotFoundUserException("존재하지 않는 회원입니다");

        return postRepository.searchLikedPostsPage(user, pageable);
    }

    private boolean validatePW(String pw, Member user) {
        return  (user.getPassword().equals(encoder.encode(pw)));
    }
}
