package com.example.anonymousboard.post.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.example.anonymousboard.post.exception.InvalidPostKeywordException;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class KeywordTest {

    @DisplayName("키워드를 생성할 수 있다.")
    @Test
    void createValidKeyword() {
        // given
        String value = "안녕";

        // when
        Keyword validKeyword = Keyword.createValidKeyword(value);

        // then
        assertThat(validKeyword.getValue()).isEqualTo("안녕");
    }

    @DisplayName("앞뒤에 공백이 포함된 키워드라면 공백은 제거된다.")
    @Test
    void createValidKeyword_success_trimBlank() {
        // given
        String value = "  안녕   ";

        // when
        Keyword validKeyword = Keyword.createValidKeyword(value);

        // then
        assertThat(validKeyword.getValue()).isEqualTo("안녕");
    }

    @DisplayName("키워드가 공백으로만 구성되면 예외가 발생한다.")
    @ParameterizedTest
    @ValueSource(strings = {" ", "", "        "})
    void createValidKeyword_exception_onlyBlank(String blankKeyword) {
        // when & then
        Assertions.assertThatThrownBy(() -> Keyword.createValidKeyword(blankKeyword))
                .isInstanceOf(InvalidPostKeywordException.class)
                .hasMessageContaining("검색 키워드는 공백을 입력할 수 없으며, 1글자 이상 입력해야 합니다.");
    }
}