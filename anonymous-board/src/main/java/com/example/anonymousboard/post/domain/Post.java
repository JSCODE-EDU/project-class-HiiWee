package com.example.anonymousboard.post.domain;

import com.example.anonymousboard.comment.domain.Comment;
import com.example.anonymousboard.member.domain.Member;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import lombok.Builder;
import org.hibernate.annotations.BatchSize;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity
@EntityListeners(AuditingEntityListener.class)
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "post_id")
    private Long id;

    @Embedded
    private Title title;

    @Embedded
    private Content content;

    @CreatedDate
    private LocalDateTime createdAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @BatchSize(size = 100)
    @OneToMany(mappedBy = "post")
    private List<Comment> comments = new ArrayList<>();

    protected Post() {
    }

    @Builder
    private Post(final String title, final String content, final Member member) {
        this.title = Title.from(title);
        this.content = Content.from(content);
        this.member = member;
    }

    public Post(final Long id, final String title, final String content, final Member member) {
        this.id = id;
        this.title = Title.from(title);
        this.content = Content.from(content);
        this.member = member;
    }

    public Long getId() {
        return id;
    }

    public String getTitle() {
        return title.getValue();
    }

    public String getContent() {
        return content.getValue();
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public Member getMember() {
        return member;
    }

    public List<Comment> getComments() {
        return Collections.unmodifiableList(comments);
    }

    public void updateTitle(final String value) {
        this.title = Title.from(value);
    }

    public void updateContent(final String value) {
        this.content = Content.from(value);
    }

    public boolean isOwner(final Long id) {
        return Objects.equals(member.getId(), id);
    }
}
