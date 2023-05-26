package com.example.anonymousboard.util;

import com.example.anonymousboard.config.JpaConfig;
import com.example.anonymousboard.member.repository.MemberRepository;
import com.example.anonymousboard.post.repository.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

@DataJpaTest
@Import(JpaConfig.class)
public class RepositoryTest {

    @Autowired
    protected MemberRepository memberRepository;

    @Autowired
    protected PostRepository postRepository;

}
