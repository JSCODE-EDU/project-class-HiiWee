package com.example.anonymousboard.util;

import com.amazonaws.services.s3.AmazonS3;
import com.example.anonymousboard.comment.repository.CommentRepository;
import com.example.anonymousboard.comment.service.CommentService;
import com.example.anonymousboard.image.service.AwsS3Service;
import com.example.anonymousboard.like.repository.PostLikeRepository;
import com.example.anonymousboard.like.service.PostLikeService;
import com.example.anonymousboard.member.domain.Encryptor;
import com.example.anonymousboard.member.repository.MemberRepository;
import com.example.anonymousboard.member.service.MemberService;
import com.example.anonymousboard.post.repository.PostRepository;
import com.example.anonymousboard.post.service.PostService;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class ServiceTest {

    @InjectMocks
    protected MemberService memberService;

    @InjectMocks
    protected PostService postService;

    @InjectMocks
    protected CommentService commentService;

    @InjectMocks
    protected PostLikeService postLikeService;

    @InjectMocks
    protected AwsS3Service awsS3Service;

    @Mock
    protected MemberRepository memberRepository;

    @Mock
    protected PostRepository postRepository;

    @Mock
    protected CommentRepository commentRepository;

    @Mock
    protected PostLikeRepository postLikeRepository;

    @Mock
    protected Encryptor encryptor;

    @Mock
    protected AmazonS3 amazonS3;
}
