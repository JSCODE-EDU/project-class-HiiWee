package com.example.anonymousboard.post.repository;

import com.example.anonymousboard.post.domain.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostRepository extends JpaRepository<Post, Long> {

    Page<Post> findPostsByOrderByCreatedAtDesc(Pageable pageable);
}
