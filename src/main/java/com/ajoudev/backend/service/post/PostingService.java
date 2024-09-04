package com.ajoudev.backend.service.post;

import com.ajoudev.backend.dto.post.request.NewPostDTO;
import com.ajoudev.backend.dto.post.response.PostPageDTO;
import com.ajoudev.backend.dto.post.response.ViewPostDTO;
import com.ajoudev.backend.entity.member.Member;
import com.ajoudev.backend.entity.post.AnswerPost;
import com.ajoudev.backend.entity.post.Post;
import com.ajoudev.backend.entity.post.QuestionPost;
import com.ajoudev.backend.exception.member.NotFoundUserException;
import com.ajoudev.backend.exception.post.*;
import com.ajoudev.backend.repository.comment.CommentRepository;
import com.ajoudev.backend.repository.like.LikeRepository;
import com.ajoudev.backend.repository.member.MemberRepository;
import com.ajoudev.backend.repository.post.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class PostingService {

    private final PostRepository postRepository;
    private final MemberRepository memberRepository;
    private final CommentRepository commentRepository;
    private final LikeRepository likeRepository;

    public ViewPostDTO createNewPost(NewPostDTO postDTO, String board) throws RuntimeException {
        validatePost(postDTO);

        String id = SecurityContextHolder.getContext().getAuthentication().getName();
        Member member = memberRepository.findByUserid(id).orElse(null);
        if (member == null) throw new NotFoundUserException("ERR_USER_NOT_FOUND");

        Post post;
        if (board.equals("Question")) post = new QuestionPost();
        else post = new Post();
        post.create(postDTO.getTitle(), postDTO.getTextBody(), board, member);
        return postRepository.save(post).toViewPostDTO(false);

    }

    public ViewPostDTO viewPost(Long postNum) throws RuntimeException {
        Post post = postRepository.findById(postNum).orElse(null);
        if (post == null) throw new PostNotFoundException();
        if (post.getPostBoard().equals("Answer")) {
            AnswerPost answerPost = (AnswerPost) post;
            post = answerPost.getParent();
        }
        post.addVisit();

        String id = SecurityContextHolder.getContext().getAuthentication().getName();
        if (id.equals("anonymousUser")) return post.toViewPostDTO(false);

        Member user = memberRepository.findByUserid(id).orElse(null);
        if (user == null) throw new NotFoundUserException("ERR_USER_NOT_FOUND");
        return post.toViewPostDTO(likeRepository.existsByUserAndPost(user,post));

    }

    public ViewPostDTO editPost(NewPostDTO postDTO, Long postNum) throws RuntimeException {
        validatePost(postDTO);
        String id = SecurityContextHolder.getContext().getAuthentication().getName();
        Member member = memberRepository.findByUserid(id).orElse(null);
        if (member == null) throw new NotFoundUserException("ERR_USER_NOT_FOUND");

        Post post = postRepository.findById(postNum).orElse(null);
        if (post == null) throw new PostNotFoundException();
        if (post.getUser() == null || !id.equals(post.getUser().getUserid())) throw new NotEditableException();

        post.edit(postDTO.getTitle(), postDTO.getTextBody());
        return post.toViewPostDTO(likeRepository.existsByUserAndPost(post.getUser(),post));
    }

    @Transactional(readOnly = true)
    public Page<PostPageDTO> findAll(Pageable pageable) {
        Page<Post> page = postRepository.findAllByPostBoard(pageable, "Normal");
        return page.map(Post::toPostPageDTO);
    }

    public void deletePost(Long postNum) throws RuntimeException{
        String id = SecurityContextHolder.getContext().getAuthentication().getName();
        Member member = memberRepository.findByUserid(id).orElse(null);
        if (member == null) throw new NotFoundUserException("ERR_USER_NOT_FOUND");

        Post post = postRepository.findById(postNum).orElse(null);
        if (post == null) throw new PostNotFoundException();
        if (!id.equals(post.getUser().getUserid())) throw new NotRemovableException();

        likeRepository.deleteByPost(post);
        commentRepository.deleteByPost(post);
        postRepository.delete(post);
    }

    public void validatePost(NewPostDTO postDTO) {
        if (postDTO.getTitle() == null) throw new NullTitleException();
        if (postDTO.getTextBody() == null) throw new NullTextBodyException();
    }
}
