package com.example.anonymousboard.like.repository;

import com.example.anonymousboard.like.domain.PostLike;
import com.example.anonymousboard.post.domain.Post;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostLikeRepository extends JpaRepository<PostLike, Long> {

    boolean existsByPostAndMemberId(Post post, Long memberId);

    void deleteByPostAndMemberId(Post post, Long memberId);

    void deleteAllByPost(Post post);
}
