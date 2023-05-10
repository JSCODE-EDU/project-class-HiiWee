package com.example.anonymousboard.post.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.example.anonymousboard.post.exception.InvalidTitleException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class TitleTest {

    @DisplayName("게시글 제목이 200자 이하라면 정상적으로 생성된다.")
    @Test
    void createTitle_success() {
        // given
        String value = "A".repeat(200);

        // when
        Title title = Title.from(value);

        assertThat(title.getValue()).isEqualTo(value);
    }

    @DisplayName("게시글 제목이 200자를 초과하면 예외가 발생한다.")
    @Test
    void createTitle_exception_invalidTitleLength() {
        // given
        String value = "A".repeat(201);

        // when & then
        assertThatThrownBy(() -> Title.from(value))
                .isInstanceOf(InvalidTitleException.class)
                .hasMessageContaining("게시글 제목은 200자 이하여야 합니다.");
    }

}