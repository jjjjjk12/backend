package com.ajoudev.backend.service.comment;

import com.ajoudev.backend.dto.comment.request.NewCommentReplyDTO;
import com.ajoudev.backend.dto.comment.response.CommentPageDTO;
import com.ajoudev.backend.entity.comment.Comment;
import com.ajoudev.backend.entity.member.Member;
import com.ajoudev.backend.entity.post.Post;
import com.ajoudev.backend.exception.comment.CommentNotFoundException;
import com.ajoudev.backend.exception.comment.NullCommentBodyException;
import com.ajoudev.backend.exception.member.NotFoundUserException;
import com.ajoudev.backend.exception.post.PostNotFoundException;
import com.ajoudev.backend.repository.comment.CommentRepository;
import com.ajoudev.backend.repository.member.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
@Transactional
public class CommentReplyingService {

    private final CommentRepository commentRepository;
    private final MemberRepository memberRepository;

    public Page<CommentPageDTO> createReply(NewCommentReplyDTO commentDTO, Pageable pageable) throws RuntimeException {
        if (commentDTO.getCommentBody() == null) throw new NullCommentBodyException();

        String id = SecurityContextHolder.getContext().getAuthentication().getName();
        Member member = memberRepository.findByUserid(id).orElse(null);
        if (member == null) throw new NotFoundUserException("ERR_USER_NOT_FOUND");

        Comment parentComment = commentRepository.findByCommentNum(commentDTO.getParent()).orElse(null);
        if (parentComment == null) throw new CommentNotFoundException();
        Post post = parentComment.getPost();
        if (post == null) throw new PostNotFoundException();

        Comment reply = new Comment();
        reply.createReply(commentDTO.getCommentBody(), post, member, parentComment.getCommentNum());
        post.addComments();

        commentRepository.save(reply);
        return commentRepository.searchPage(post, pageable);

    }


}
