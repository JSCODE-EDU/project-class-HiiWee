package com.example.anonymousboard.util;

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

    @Mock
    protected MemberRepository memberRepository;

    @InjectMocks
    protected PostService postService;

    @Mock
    protected PostRepository postRepository;
}
