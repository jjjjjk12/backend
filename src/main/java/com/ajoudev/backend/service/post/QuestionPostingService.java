package com.ajoudev.backend.service.post;

import com.ajoudev.backend.dto.post.request.NewPostDTO;
import com.ajoudev.backend.dto.post.response.AnswerPageDTO;
import com.ajoudev.backend.dto.post.response.QuestionPageDTO;
import com.ajoudev.backend.dto.post.response.ViewAnswerDTO;
import com.ajoudev.backend.dto.post.response.ViewPostDTO;
import com.ajoudev.backend.entity.member.Member;
import com.ajoudev.backend.entity.post.AnswerPost;
import com.ajoudev.backend.entity.post.Post;
import com.ajoudev.backend.entity.post.QuestionPost;
import com.ajoudev.backend.exception.member.NotFoundUserException;
import com.ajoudev.backend.exception.post.NotEditableException;
import com.ajoudev.backend.exception.post.PostNotFoundException;
import com.ajoudev.backend.exception.post.PostingException;
import com.ajoudev.backend.repository.comment.CommentRepository;
import com.ajoudev.backend.repository.like.DislikeRepository;
import com.ajoudev.backend.repository.like.LikeRepository;
import com.ajoudev.backend.repository.member.MemberRepository;
import com.ajoudev.backend.repository.post.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class QuestionPostingService {

    private final PostRepository postRepository;
    private final MemberRepository memberRepository;
    private final CommentRepository commentRepository;
    private final LikeRepository likeRepository;
    private final PostingService postingService;
    private final DislikeRepository dislikeRepository;

    public Slice<AnswerPageDTO> createAns(NewPostDTO postDTO, Long parentNum, Pageable pageable) throws RuntimeException {
        String id = SecurityContextHolder.getContext().getAuthentication().getName();
        Member member = memberRepository.findByUserid(id).orElse(null);
        if (member == null) throw new NotFoundUserException("ERR_USER_NOT_FOUND");

        QuestionPost parent = (QuestionPost) postRepository.findById(parentNum).orElse(null);
        if (parent == null) throw new PostNotFoundException("ERR_PARENT_NOT_FOUND");

        postingService.validatePost(postDTO);

        parent.addQuestion();
        AnswerPost aPost = new AnswerPost();
        aPost.create(postDTO.getTitle(), postDTO.getTextBody(),
                "Answer", member, parent);


        postRepository.save(aPost);
        return postRepository.searchAnswers(pageable, parent, member);
    }

    public void deleteQuestion(Long postNum) throws RuntimeException{
        String id = SecurityContextHolder.getContext().getAuthentication().getName();
        Member member = memberRepository.findByUserid(id).orElse(null);
        if (member == null) throw new NotFoundUserException("ERR_USER_NOT_FOUND");

        QuestionPost post = (QuestionPost) postRepository.findById(postNum).orElse(null);
        if (post == null) throw new PostNotFoundException("ERR_POST_NOT_FOUND");

        if (post.getAnswers() == 0) {
            likeRepository.deleteByPost(post);
            commentRepository.deleteByPost(post);
            postRepository.delete(post);
        }

        else {
            postRepository.updateNullByPost(post);
        }

    }

    public Slice<AnswerPageDTO> deleteAnswer(Long postNum, Pageable pageable) throws RuntimeException {
        String id = SecurityContextHolder.getContext().getAuthentication().getName();
        Member member = memberRepository.findByUserid(id).orElse(null);
        if (member == null) throw new NotFoundUserException("ERR_USER_NOT_FOUND");

        AnswerPost post = (AnswerPost) postRepository.findById(postNum).orElse(null);
        if (post == null) throw new PostNotFoundException("ERR_POST_NOT_FOUND");

        QuestionPost parent = post.getParent();
        parent.deleteQuestion();

        likeRepository.deleteByPost(post);
        dislikeRepository.deleteByPost(post);

        commentRepository.deleteByPost(post);
        postRepository.delete(post);

        return postRepository.searchAnswers(pageable, parent, member);
    }

    public Slice<AnswerPageDTO> viewAnswers(Pageable pageable, Long postNum) throws RuntimeException {
        QuestionPost post = (QuestionPost) postRepository.findById(postNum).orElse(null);
        if (post == null) throw new PostNotFoundException();

        return postRepository.searchAnswers(pageable, post, null);
    }

    public Page<QuestionPageDTO> viewQuestions(Pageable pageable) throws RuntimeException {
        return postRepository.searchQuestions(pageable);
    }

    public ViewAnswerDTO adoptAns(Long postNum) throws RuntimeException {
        AnswerPost post = (AnswerPost) postRepository.findById(postNum).orElse(null);
        if (post == null) throw new PostNotFoundException();

        String id = SecurityContextHolder.getContext().getAuthentication().getName();
        Member member = memberRepository.findByUserid(id).orElse(null);
        if (member == null) throw new NotFoundUserException("ERR_USER_NOT_FOUND");

        QuestionPost parent = post.getParent();
        if (!parent.getUser().getUserid().equals(member.getUserid())) {
            throw new PostingException();
        }

        if (parent.isHasAdopted()) throw new PostingException();
        parent.doAdopt();
        post.adopted();

        return post.toViewAnswerDTO(likeRepository.existsByUserAndPost(member, post),
                dislikeRepository.existsByUserAndPost(member, post));
    }

    public ViewAnswerDTO editAns(NewPostDTO postDTO, Long postNum) throws RuntimeException {
        postingService.validatePost(postDTO);
        String id = SecurityContextHolder.getContext().getAuthentication().getName();
        Member member = memberRepository.findByUserid(id).orElse(null);
        if (member == null) throw new NotFoundUserException("ERR_USER_NOT_FOUND");

        AnswerPost post = (AnswerPost) postRepository.findById(postNum).orElse(null);
        if (post == null) throw new PostNotFoundException();
        if (post.getUser() == null || !id.equals(post.getUser().getUserid())) throw new NotEditableException();

        post.edit(postDTO.getTitle(), postDTO.getTextBody());
        return post.toViewAnswerDTO(likeRepository.existsByUserAndPost(member, post),
                dislikeRepository.existsByUserAndPost(member, post));
    }
}
