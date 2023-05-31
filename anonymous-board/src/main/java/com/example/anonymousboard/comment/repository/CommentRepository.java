package com.example.anonymousboard.comment.repository;

import com.example.anonymousboard.comment.domain.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, Long> {
}
