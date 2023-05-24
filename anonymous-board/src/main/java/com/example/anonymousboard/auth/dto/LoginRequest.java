package com.example.anonymousboard.auth.dto;

import javax.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;

@Getter
public class LoginRequest {

    @NotBlank(message = "이메일 혹은 비밀번호는 반드시 입력해야 합니다.")
    private String email;

    @NotBlank(message = "이메일 혹은 비밀번호는 반드시 입력해야 합니다.")
    private String password;

    private LoginRequest() {
    }

    @Builder
    private LoginRequest(final String email, final String password) {
        this.email = email;
        this.password = password;
    }
}
