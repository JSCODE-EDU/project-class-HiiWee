package com.example.anonymousboard.post.repository;

import com.example.anonymousboard.post.domain.Post;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface PostRepository extends JpaRepository<Post, Long> {

    Page<Post> findPostsByOrderByCreatedAtDesc(Pageable pageable);

    @Query("SELECT p FROM Post p WHERE p.title.value LIKE :keyword")
    List<Post> findPostsByKeyword(@Param("keyword") String keyword, Pageable pageable);
}
