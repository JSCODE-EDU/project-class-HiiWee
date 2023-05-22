package com.example.anonymousboard.member.dto;

import javax.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;

@Getter
public class SignUpRequest {

    @NotBlank(message = "이메일은 반드시 입력해야 합니다.")
    private String email;

    @NotBlank(message = "패스워드는 반드시 입력해야 합니다.")
    private String password;

    @NotBlank(message = "패스워드 확인은 반드시 입력해야 합니다.")
    private String passwordConfirmation;

    private SignUpRequest() {
    }

    @Builder
    private SignUpRequest(final String email, final String password, final String passwordConfirmation) {
        this.email = email;
        this.password = password;
        this.passwordConfirmation = passwordConfirmation;
    }
}
