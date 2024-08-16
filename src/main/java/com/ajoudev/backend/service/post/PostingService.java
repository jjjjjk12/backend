package com.ajoudev.backend.service.post;

import com.ajoudev.backend.dto.post.request.NewPostDTO;
import com.ajoudev.backend.dto.post.response.PostPageDTO;
import com.ajoudev.backend.dto.post.response.ViewPostDTO;
import com.ajoudev.backend.entity.member.Member;
import com.ajoudev.backend.entity.post.Post;
import com.ajoudev.backend.exception.post.*;
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

    public ViewPostDTO createNewNormalPost(NewPostDTO postDTO) throws PostingException {
        validatePost(postDTO);

        String id = SecurityContextHolder.getContext().getAuthentication().getName();
        Member member = memberRepository.findByUserid(id).orElse(null);

        Post post = new Post();
        post.create(postDTO.getTitle(), postDTO.getTextBody(), "Nomal", member);
        return postRepository.save(post).toViewPostDTO();

    }

    @Transactional(readOnly = true)
    public ViewPostDTO viewPost(Long postNum) {
        Post post = postRepository.findById(postNum).orElse(null);
        if (post == null) throw new PostNotFoundException();

        return post.toViewPostDTO();

    }

    public ViewPostDTO editPost(NewPostDTO postDTO, Long postNum) throws PostingException {
        validatePost(postDTO);
        String id = SecurityContextHolder.getContext().getAuthentication().getName();
        Post post = postRepository.findById(postNum).orElse(null);
        if (post == null) throw new PostNotFoundException();
        if (!id.equals(post.getUser().getUserid())) throw new NotEditableException();

        post.edit(postDTO.getTitle(), postDTO.getTextBody());
        return post.toViewPostDTO();
    }

    @Transactional(readOnly = true)
    public Page<PostPageDTO> findAll(Pageable pageable) {
        Page<Post> page = postRepository.findAll(pageable);
        return page.map(Post::toPostPageDTO);
    }

    private void validatePost(NewPostDTO postDTO) {
        if (postDTO.getTitle() == null) throw new NullTitleException();
        if (postDTO.getTextBody() == null) throw new NullTextBodyException();
    }
}
