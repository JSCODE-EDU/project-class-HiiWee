package com.example.anonymousboard.post.service;

import com.example.anonymousboard.post.domain.Keyword;
import com.example.anonymousboard.post.domain.Post;
import com.example.anonymousboard.post.dto.PagePostsResponse;
import com.example.anonymousboard.post.dto.PostResponse;
import com.example.anonymousboard.post.dto.PostSaveRequest;
import com.example.anonymousboard.post.dto.PostUpdateRequest;
import com.example.anonymousboard.post.exception.PostNotFoundException;
import com.example.anonymousboard.post.repository.PostRepository;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class PostService {

    private final PostRepository postRepository;

    public PostService(final PostRepository postRepository) {
        this.postRepository = postRepository;
    }

    @Transactional
    public Long createPost(final PostSaveRequest postSaveRequest) {
        Post post = Post.builder()
                .title(postSaveRequest.getTitle())
                .content(postSaveRequest.getContent())
                .build();
        Post savedPost = postRepository.save(post);
        return savedPost.getId();
    }

    public PagePostsResponse findPosts(@Nullable final String keyword, Pageable pageable) {
        Keyword validKeyword = Keyword.createValidKeyword(keyword);
        if (validKeyword != null) {
            List<Post> posts = postRepository.findPostsByKeyword(validKeyword.getValue(), pageable);
            return PagePostsResponse.of(posts);
        }
        Page<Post> posts = postRepository.findPostsByOrderByCreatedAtDesc(pageable);
        return PagePostsResponse.of(posts.getContent());
    }

    public PostResponse findPostById(final Long postId) {
        Post findPost = postRepository.findById(postId)
                .orElseThrow(PostNotFoundException::new);
        return PostResponse.from(findPost);
    }

    @Transactional
    public PostResponse updatePostById(final Long postId, final PostUpdateRequest postUpdateRequest) {
        Post post = findPostObject(postId);

        post.updateTitle(postUpdateRequest.getTitle());
        post.updateContent(postUpdateRequest.getContent());
        return PostResponse.from(post);
    }

    private Post findPostObject(final Long postId) {
        return postRepository.findById(postId)
                .orElseThrow(PostNotFoundException::new);
    }

    @Transactional
    public void deletePostById(final Long postId) {
        Post post = findPostObject(postId);
        postRepository.delete(post);
    }
}
