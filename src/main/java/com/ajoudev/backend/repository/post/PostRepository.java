package com.ajoudev.backend.repository.post;

import com.ajoudev.backend.entity.post.Post;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostRepository extends JpaRepository<Post, Long> {
}
