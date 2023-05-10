package com.example.anonymousboard.post.repository;

import com.example.anonymousboard.post.domain.Post;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostRepository extends JpaRepository<Post, Long> {
}
