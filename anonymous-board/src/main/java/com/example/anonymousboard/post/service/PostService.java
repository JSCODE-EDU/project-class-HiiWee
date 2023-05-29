package com.example.anonymousboard.post.service;

import com.example.anonymousboard.auth.dto.AuthInfo;
import com.example.anonymousboard.auth.exception.AuthorizationException;
import com.example.anonymousboard.member.domain.Member;
import com.example.anonymousboard.member.exception.MemberNotFoundException;
import com.example.anonymousboard.member.repository.MemberRepository;
import com.example.anonymousboard.post.domain.Keyword;
import com.example.anonymousboard.post.domain.Post;
import com.example.anonymousboard.post.dto.PagePostsResponse;
import com.example.anonymousboard.post.dto.PostResponse;
import com.example.anonymousboard.post.dto.PostSaveRequest;
import com.example.anonymousboard.post.dto.PostSaveResponse;
import com.example.anonymousboard.post.dto.PostUpdateRequest;
import com.example.anonymousboard.post.exception.PostNotFoundException;
import com.example.anonymousboard.post.repository.PostRepository;
import java.util.List;
import java.util.Objects;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class PostService {

    private final PostRepository postRepository;
    private final MemberRepository memberRepository;

    public PostService(final PostRepository postRepository, final MemberRepository memberRepository) {
        this.postRepository = postRepository;
        this.memberRepository = memberRepository;
    }

    @Transactional
    public PostSaveResponse createPost(final AuthInfo authInfo, final PostSaveRequest postSaveRequest) {
        Member member = findMember(authInfo);
        Post post = Post.builder()
                .title(postSaveRequest.getTitle())
                .content(postSaveRequest.getContent())
                .member(member)
                .build();
        Post savedPost = postRepository.save(post);
        return PostSaveResponse.createPostSuccess(savedPost.getId());
    }

    public PagePostsResponse findPosts(Pageable pageable) {
        Page<Post> posts = postRepository.findPostsByOrderByCreatedAtDesc(pageable);
        return PagePostsResponse.from(posts.getContent());
    }

    public PagePostsResponse findPostsByKeyword(final String keyword, Pageable pageable) {
        Keyword validKeyword = Keyword.createValidKeyword(keyword);
        List<Post> posts = postRepository.findPostsByTitleValueContaining(validKeyword.getValue(), pageable);
        return PagePostsResponse.from(posts);
    }

    public PostResponse findPostById(final Long postId) {
        Post findPost = postRepository.findById(postId)
                .orElseThrow(PostNotFoundException::new);
        return PostResponse.from(findPost);
    }

    @Transactional
    public void updatePostById(final AuthInfo authInfo, final Long postId, final PostUpdateRequest postUpdateRequest) {
        Post post = findPostObject(postId);
        validateOwner(authInfo, post);
        post.updateTitle(postUpdateRequest.getTitle());
        post.updateContent(postUpdateRequest.getContent());
    }

    @Transactional
    public void deletePostById(final AuthInfo authInfo, final Long postId) {
        Post post = findPostObject(postId);
        validateOwner(authInfo, post);
        postRepository.delete(post);
    }

    private Post findPostObject(final Long postId) {
        return postRepository.findById(postId)
                .orElseThrow(PostNotFoundException::new);
    }

    private Member findMember(final AuthInfo authInfo) {
        return memberRepository.findById(authInfo.getId())
                .orElseThrow(MemberNotFoundException::new);
    }

    private void validateOwner(final AuthInfo authInfo, final Post post) {
        if (!post.isOwner(authInfo.getId())) {
            throw new AuthorizationException();
        }
    }
}
