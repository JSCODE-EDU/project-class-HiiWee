package com.example.anonymousboard.member.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class EncryptorTest {

    Encryptor encryptor;

    @BeforeEach
    void setUp() {
        encryptor = new Encryptor();
    }

    @DisplayName("패스워드를 암호화 하면 64글자로 떨어진다.")
    @Test
    void encrypt() {
        // given
        String password = "!qwer123";

        // when
        String encryptedPassword = encryptor.encrypt(password);

        // then
        assertAll(
                () -> assertThat(encryptedPassword.length()).isEqualTo(64),
                () -> assertThat(encryptedPassword).isNotEqualTo(encryptor.encrypt("!qwer1234"))
        );
    }
}