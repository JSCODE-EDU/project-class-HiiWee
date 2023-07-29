package com.example.anonymousboard.comment.repository;

import com.example.anonymousboard.comment.domain.Comment;
import com.example.anonymousboard.post.domain.Post;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    @Query("SELECT c FROM Comment c LEFT JOIN FETCH c.member WHERE c.post = :post")
    List<Comment> findCommentsByPost(@Param("post") Post post);

    @Query("SELECT c FROM Comment c LEFT JOIN FETCH c.member WHERE c.post IN :posts ORDER BY c.createdAt ASC")
    List<Comment> findCommentsPagesByPostIn(@Param("posts") List<Post> posts);

    void deleteAllByPost(Post post);
}

