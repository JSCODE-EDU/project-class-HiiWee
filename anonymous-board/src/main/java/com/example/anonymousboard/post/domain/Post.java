package com.example.anonymousboard.post.domain;

import java.time.LocalDateTime;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import lombok.Builder;
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

    protected Post() {
    }

    @Builder
    private Post(final Long id, final String title, final String content) {
        this.id = id;
        this.title = Title.from(title);
        this.content = Content.from(content);
    }

    public Long getId() {
        return id;
    }

    public String getTitleValue() {
        return title.getValue();
    }

    public String getContentValue() {
        return content.getValue();
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
}
