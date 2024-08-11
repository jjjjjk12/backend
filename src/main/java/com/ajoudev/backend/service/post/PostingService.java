package com.ajoudev.backend.service.post;

import com.ajoudev.backend.dto.post.request.NewPostDTO;
import com.ajoudev.backend.dto.post.response.ViewPostDTO;
import com.ajoudev.backend.entity.member.Member;
import com.ajoudev.backend.entity.post.Post;
import com.ajoudev.backend.exception.post.NullTextBodyException;
import com.ajoudev.backend.exception.post.NullTitleException;
import com.ajoudev.backend.exception.post.PostNotFoundException;
import com.ajoudev.backend.exception.post.PostingException;
import com.ajoudev.backend.repository.member.MemberRepository;
import com.ajoudev.backend.repository.post.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class PostingService {

    private final PostRepository postRepository;
    private final MemberRepository memberRepository;

    public ViewPostDTO createNewNormalPost(NewPostDTO postDTO) throws PostingException {
        validatePost(postDTO);

        String id = SecurityContextHolder.getContext().getAuthentication().getName();
        Member member = memberRepository.findByUserid(id).orElse(null);

        Post post = new Post();
        post.create(postDTO.getTitle(), postDTO.getTextBody(), "Nomal", member);
        return postRepository.save(post).toViewPostDTO();

    }

    public ViewPostDTO viewPost(Long postNum) {
        Post post = postRepository.findById(postNum).orElse(null);
        if (post == null) throw new PostNotFoundException();

        return post.toViewPostDTO();

    }

    public ViewPostDTO editPost(NewPostDTO postDTO, Long postNum) throws PostingException {
        validatePost(postDTO);
        Post post = postRepository.findById(postNum).orElse(null);
        if (post == null) throw new PostNotFoundException();

        post.edit(postDTO.getTitle(), postDTO.getTextBody());
        return post.toViewPostDTO();
    }

    private void validatePost(NewPostDTO postDTO) {
        if (postDTO.getTitle() == null) throw new NullTitleException();
        if (postDTO.getTextBody() == null) throw new NullTextBodyException();
    }
}
