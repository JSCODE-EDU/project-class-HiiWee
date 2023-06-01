package com.example.anonymousboard.comment.repository;

import com.example.anonymousboard.comment.domain.Comment;
import com.example.anonymousboard.post.domain.Post;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    @Query("SELECT distinct c from Comment c left join fetch c.member where c.post = :post")
    List<Comment> findCommentsByPost(Post post);
}
