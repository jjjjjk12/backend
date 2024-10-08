package com.ajoudev.backend.service.comment;

import com.ajoudev.backend.dto.comment.request.EditCommentDTO;
import com.ajoudev.backend.dto.comment.request.NewCommentDTO;
import com.ajoudev.backend.dto.comment.response.CommentPageDTO;
import com.ajoudev.backend.entity.comment.Comment;
import com.ajoudev.backend.entity.member.Member;
import com.ajoudev.backend.entity.post.Post;
import com.ajoudev.backend.exception.comment.NullCommentBodyException;
import com.ajoudev.backend.exception.member.NotFoundUserException;
import com.ajoudev.backend.exception.post.NotEditableException;
import com.ajoudev.backend.exception.post.NotRemovableException;
import com.ajoudev.backend.exception.post.PostNotFoundException;
import com.ajoudev.backend.repository.comment.CommentRepository;
import com.ajoudev.backend.repository.member.MemberRepository;
import com.ajoudev.backend.repository.post.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@RequiredArgsConstructor
@Service
@Transactional
public class CommentingService {

    private final CommentRepository commentRepository;
    private final MemberRepository memberRepository;
    private final PostRepository postRepository;

    public Page<CommentPageDTO> createComment(NewCommentDTO commentDTO, Pageable pageable, Long postNum) throws RuntimeException {
        if (commentDTO.getCommentBody() == null) throw new NullCommentBodyException();

        String id = SecurityContextHolder.getContext().getAuthentication().getName();
        Member member = memberRepository.findByUserid(id).orElse(null);
        if (member == null) throw new NotFoundUserException("ERR_USER_NOT_FOUND");

        Post post = postRepository.findById(postNum).orElse(null);
        if (post == null) throw new PostNotFoundException();
        Comment comment = new Comment();
        comment.createComment(commentDTO.getCommentBody(), post, member);
        post.addComments();

        commentRepository.save(comment);
        return commentRepository.searchPage(post, createPageRequest(post, pageable));

    }

    public Page<CommentPageDTO> findByPage(Pageable pageable, Long postNum) throws RuntimeException {
        Post post = postRepository.findById(postNum).orElse(null);
        if (post == null) throw new PostNotFoundException();

        return commentRepository.searchPage(post, createPageRequest(post, pageable));
    }

    public Page<CommentPageDTO> editComment(EditCommentDTO commentDTO, Pageable pageable) throws RuntimeException {
        if (commentDTO.getCommentBody() == null) throw new NullCommentBodyException();

        String id = SecurityContextHolder.getContext().getAuthentication().getName();
        Member member = memberRepository.findByUserid(id).orElse(null);
        Comment comment = commentRepository.findByCommentNum(commentDTO.getCommentNum()).orElse(null);

        if (member == null) throw new NotFoundUserException("ERR_USER_NOT_FOUND");
        if (comment == null || !member.getUserid().equals(comment.getUser().getUserid()))
            throw new NotEditableException();

        comment.editComment(commentDTO.getCommentBody());
        return commentRepository.searchPage(comment.getPost(), createPageRequest(comment.getPost(), pageable));
    }

    public Page<CommentPageDTO> deleteComment(Long commentNum, Pageable pageable) throws RuntimeException {
        String id = SecurityContextHolder.getContext().getAuthentication().getName();
        Member member = memberRepository.findByUserid(id).orElse(null);
        Comment comment = commentRepository.findByCommentNum(commentNum).orElse(null);

        if (member == null) throw new NotFoundUserException("ERR_USER_NOT_FOUND");
        if (comment == null || !member.getUserid().equals(comment.getUser().getUserid()))
            throw new NotRemovableException();

        Post post = comment.getPost();
        post.deleteComments();

        if (commentRepository.countByParentComment(commentNum) > 1) {
            commentRepository.updateNull(commentNum);
        }
        else {
            commentRepository.deleteById(commentNum);
        }

        commentRepository.deleteAllNulls();
        return commentRepository.searchPage(post, createPageRequest(post, pageable));
    }

    private Pageable createPageRequest(Post post, Pageable pageable) {
        if (post.getPostBoard().equals("Question")) {
            return PageRequest.of(pageable.getPageNumber(), 3, Sort.Direction.DESC, "commentNum");
        }
        return pageable;


    }
}
