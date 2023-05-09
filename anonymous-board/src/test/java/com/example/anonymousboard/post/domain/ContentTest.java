package com.example.anonymousboard.post.domain;

import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.example.anonymousboard.post.exception.InvalidContentException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class ContentTest {

    @DisplayName("게시글 내용이 5000자 이하라면 정상적으로 생성된다.")
    @Test
    void createContent_success() {
        String value = "A".repeat(5000);

        assertThatNoException()
                .isThrownBy(() -> new Content(value));
    }

    @DisplayName("게시글 내용이 5000자를 초과하면 예외가 발생한다.")
    @Test
    void createContent_exception_invalidContentsLength() {
        // given
        String value = "A".repeat(5001);

        // when & then
        assertThatThrownBy(() -> new Content(value))
                .isInstanceOf(InvalidContentException.class)
                .hasMessageContaining("게시글 본문은 5000자 이하여야 합니다.");
    }
}