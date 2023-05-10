package com.example.anonymousboard.post.domain;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import lombok.Builder;

@Entity
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "post_id")
    private Long id;

    @Embedded
    private Title title;

    @Embedded
    private Content content;

    protected Post() {
    }

    @Builder
    private Post(final Title title, final Content content) {
        this.title = title;
        this.content = content;
    }

    public Long getId() {
        return id;
    }
}
