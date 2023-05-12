package com.example.anonymousboard.post.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.example.anonymousboard.post.exception.InvalidTitleException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class TitleTest {

    @DisplayName("게시글 제목이 빈 값이라면 예외가 발생한다.")
    @Test
    void createTitle_exception_emptyTitle() {
        // given
        String value = "";

        // when & then
        assertThatThrownBy(() -> Title.from(value))
                .isInstanceOf(InvalidTitleException.class)
                .hasMessageContaining("게시글 제목은 1자 이상 15자 이하까지 입력할 수 있습니다.");
    }

    @DisplayName("게시글 제목이 공백이라면 예외가 발생한다.")
    @Test
    void createTitle_exception_blankTitle() {
        // given
        String value = "     ";

        // when & then
        assertThatThrownBy(() -> Title.from(value))
                .isInstanceOf(InvalidTitleException.class)
                .hasMessageContaining("게시글 제목은 1자 이상 15자 이하까지 입력할 수 있습니다.");
    }

    @DisplayName("게시글 제목이 15자 이하라면 정상적으로 생성된다.")
    @Test
    void createTitle_success() {
        // given
        String value = "A".repeat(15);

        // when
        Title title = Title.from(value);

        // then
        assertThat(title.getValue()).isEqualTo(value);
    }

    @DisplayName("게시글 제목이 15자를 초과하면 예외가 발생한다.")
    @Test
    void createTitle_exception_invalidTitleLength() {
        // given
        String value = "A".repeat(16);

        // when & then
        assertThatThrownBy(() -> Title.from(value))
                .isInstanceOf(InvalidTitleException.class)
                .hasMessageContaining("게시글 제목은 1자 이상 15자 이하까지 입력할 수 있습니다.");
    }

    @DisplayName("게시글 제목 앞뒤에 공백을 제거하고 15글자를 넘지 않으면 제목을 생성한다.")
    @Test
    void createTitle_success_trim() {
        // given
        String value = "                안녕하세요!             ";

        // when
        Title title = Title.from(value);

        // then
        assertThat(title.getValue()).isEqualTo(value.trim());
    }

}