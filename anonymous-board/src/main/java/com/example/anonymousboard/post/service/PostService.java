package com.example.anonymousboard.post.service;

import com.example.anonymousboard.auth.dto.AuthInfo;
import com.example.anonymousboard.auth.exception.AuthorizationException;
import com.example.anonymousboard.comment.domain.Comment;
import com.example.anonymousboard.comment.exception.CommentLimitException;
import com.example.anonymousboard.comment.repository.CommentRepository;
import com.example.anonymousboard.like.repository.PostLikeRepository;
import com.example.anonymousboard.member.domain.Member;
import com.example.anonymousboard.member.exception.MemberNotFoundException;
import com.example.anonymousboard.member.repository.MemberRepository;
import com.example.anonymousboard.post.domain.Keyword;
import com.example.anonymousboard.post.domain.Post;
import com.example.anonymousboard.post.dto.PagePostsDetailResponse;
import com.example.anonymousboard.post.dto.PagePostsResponse;
import com.example.anonymousboard.post.dto.PostDetailResponse;
import com.example.anonymousboard.post.dto.PostResponse;
import com.example.anonymousboard.post.dto.PostSaveRequest;
import com.example.anonymousboard.post.dto.PostSaveResponse;
import com.example.anonymousboard.post.dto.PostUpdateRequest;
import com.example.anonymousboard.post.exception.PostLimitException;
import com.example.anonymousboard.post.exception.PostNotFoundException;
import com.example.anonymousboard.post.repository.PostRepository;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class PostService {

    private static final int COMMENT_SEARCHING_LIMIT = 100;
    private static final int POST_SEARCHING_LIMIT = 100;

    private final PostRepository postRepository;
    private final MemberRepository memberRepository;
    private final CommentRepository commentRepository;
    private final PostLikeRepository postLikeRepository;

    public PostService(final PostRepository postRepository, final MemberRepository memberRepository,
                       final CommentRepository commentRepository, final PostLikeRepository postLikeRepository) {
        this.postRepository = postRepository;
        this.memberRepository = memberRepository;
        this.commentRepository = commentRepository;
        this.postLikeRepository = postLikeRepository;
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

    // TODO: N번의 댓글 조회와 전체 댓글 조회후 게시글에 맞게 그룹화 시키는 방법을 성능테스트 해보자!!
    public PagePostsDetailResponse findPosts(final int commentLimit, final Pageable pageable) {
        validatePostsViewCondition(commentLimit, pageable);
        Page<Post> postPages = postRepository.findPostsByOrderByCreatedAtDesc(pageable);
        List<Comment> comments = commentRepository.findCommentsPagesByPostIn(postPages.getContent());
        Map<Post, List<Comment>> groupedComments = separateLimitCommentsByPost(comments, commentLimit);
        List<PostDetailResponse> postsResponse = createPostsResponse(postPages.getContent(), groupedComments);
        return PagePostsDetailResponse.from(postsResponse);
    }

    public PagePostsResponse findPostsByKeyword(final String keyword, Pageable pageable) {
        Keyword validKeyword = Keyword.createValidKeyword(keyword);
        List<Post> posts = postRepository.findPostsByTitleValueContaining(validKeyword.getValue(), pageable);
        return PagePostsResponse.from(posts);
    }

    public PostDetailResponse findPostDetailById(final Long postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(PostNotFoundException::new);
        List<Comment> comments = commentRepository.findCommentsByPost(post);
        return PostDetailResponse.of(post, comments);
    }

    public PostResponse findPostById(final Long postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(PostNotFoundException::new);
        return PostResponse.from(post);
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
        postLikeRepository.deleteAllByPost(post);
        commentRepository.deleteAllByPost(post);

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

    private List<PostDetailResponse> createPostsResponse(final List<Post> postPages,
                                                         final Map<Post, List<Comment>> groupedComments) {
        return postPages.stream()
                .map(post -> PostDetailResponse.of(post, groupedComments.getOrDefault(post, Collections.emptyList())))
                .collect(Collectors.toList());
    }

    private Map<Post, List<Comment>> separateLimitCommentsByPost(final List<Comment> comments, final int commentLimit) {
        return comments.stream()
                .collect(Collectors.groupingBy(Comment::getPost, Collectors.collectingAndThen(Collectors.toList(),
                        eachComments -> eachComments.subList(0, Math.min(eachComments.size(), commentLimit)))));
    }

    private void validateOwner(final AuthInfo authInfo, final Post post) {
        if (!post.isOwner(authInfo.getId())) {
            throw new AuthorizationException();
        }
    }

    private void validatePostsViewCondition(final int commentLimit, final Pageable pageable) {
        validateCommentLimit(commentLimit);
        validatePostLimit(pageable);
    }

    private void validatePostLimit(final Pageable pageable) {
        if (pageable.getPageSize() > POST_SEARCHING_LIMIT) {
            throw new PostLimitException();
        }
    }

    private void validateCommentLimit(final int commentLimit) {
        if (commentLimit > COMMENT_SEARCHING_LIMIT) {
            throw new CommentLimitException();
        }
    }
}
