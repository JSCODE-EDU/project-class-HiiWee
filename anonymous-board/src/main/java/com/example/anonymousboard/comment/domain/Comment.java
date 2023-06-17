package com.example.anonymousboard.comment.domain;

import com.example.anonymousboard.member.domain.Member;
import com.example.anonymousboard.post.domain.Post;
import java.time.LocalDateTime;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import lombok.Builder;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity
@EntityListeners(AuditingEntityListener.class)
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "comment_id")
    private Long id;

    private Content content;

    @CreatedDate
    private LocalDateTime createdAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")
    private Post post;

    protected Comment() {
    }

    @Builder
    private Comment(final String content, final Member member, final Post post) {
        this.content = Content.from(content);
        this.member = member;
        this.post = post;
    }

    public Comment(final Long id, final String content, final Member member, final Post post) {
        this.id = id;
        this.content = Content.from(content);
        this.member = member;
        this.post = post;
    }

    public String getContent() {
        return content.getValue();
    }

    public Long getId() {
        return id;
    }

    public Member getMember() {
        return member;
    }

    public Post getPost() {
        return post;
    }

    public String getWriterEmail() {
        return member.getEmail();
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
}
