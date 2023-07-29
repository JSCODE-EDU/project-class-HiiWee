package com.example.anonymousboard.image.service;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

import com.example.anonymousboard.image.dto.ImagesUploadRequest;
import com.example.anonymousboard.image.exception.FileNotFoundException;
import com.example.anonymousboard.image.exception.FileUploadFailException;
import com.example.anonymousboard.util.ServiceTest;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.web.multipart.MultipartFile;

class AwsS3ServiceTest extends ServiceTest {

    ImagesUploadRequest nonEmptyFileRequest;

    ImagesUploadRequest emptyFileRequest;

    MultipartFile nonEmptyFile;

    MultipartFile emptyFile;

    @BeforeEach
    void setUp() {
        emptyFile = new StubMultipartFile(true);

        nonEmptyFile = new StubMultipartFile(false);

        nonEmptyFileRequest = ImagesUploadRequest.builder()
                .category("images")
                .files(List.of(nonEmptyFile))
                .build();

        emptyFileRequest = ImagesUploadRequest.builder()
                .category("images")
                .files(List.of(emptyFile))
                .build();
    }

    @DisplayName("AWS 오류 발생시 파일 업로드를 할 수 없다.")
    @Test
    void uploadFiles_exception_withAwsError() {
        // given
        given(amazonS3.putObject(any())).willThrow(new FileUploadFailException());

        // when & then
        assertThatThrownBy(() -> awsS3Service.uploadFiles(nonEmptyFileRequest))
                .isInstanceOf(FileUploadFailException.class)
                .hasMessageContaining("파일 업로드를 실패했습니다.");
    }

    @DisplayName("파일이 비어있다면 파일 업로드를 할 수 없다.")
    @Test
    void uploadFiles_exception_withEmptyFile() {
        // given & when & then
        assertThatThrownBy(() -> awsS3Service.uploadFiles(emptyFileRequest))
                .isInstanceOf(FileNotFoundException.class)
                .hasMessageContaining("파일을 찾을 수 없습니다.");
    }
}