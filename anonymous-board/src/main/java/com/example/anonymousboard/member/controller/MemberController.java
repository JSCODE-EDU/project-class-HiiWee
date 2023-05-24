package com.example.anonymousboard.member.controller;

import com.example.anonymousboard.auth.dto.AuthInfo;
import com.example.anonymousboard.member.dto.MyInfoResponse;
import com.example.anonymousboard.member.dto.SignUpRequest;
import com.example.anonymousboard.member.service.MemberService;
import com.example.anonymousboard.support.token.Login;
import javax.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MemberController {

    private final MemberService memberService;

    public MemberController(final MemberService memberService) {
        this.memberService = memberService;
    }

    @PostMapping("/members/signup")
    public ResponseEntity<Void> signUp(@Valid @RequestBody final SignUpRequest signUpRequest) {
        memberService.signUp(signUpRequest);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping("/members/me")
    public ResponseEntity<MyInfoResponse> findMyInfo(@Login AuthInfo authInfo) {
        MyInfoResponse myInfoResponse = memberService.findMyInfo(authInfo);
        return ResponseEntity.ok(myInfoResponse);
    }
}
