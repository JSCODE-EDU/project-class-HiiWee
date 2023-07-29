package com.example.anonymousboard.image.controller;

import static com.example.anonymousboard.util.apidocs.ApiDocumentUtils.getDocumentRequest;
import static com.example.anonymousboard.util.apidocs.ApiDocumentUtils.getDocumentResponse;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.requestParameters;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.example.anonymousboard.image.dto.ImagesUploadRequest;
import com.example.anonymousboard.image.service.StubMultipartFile;
import com.example.anonymousboard.util.ControllerTest;
import java.io.FileInputStream;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.web.multipart.MultipartFile;

public class FileUploadControllerTest extends ControllerTest {

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

    @DisplayName("파일 업로드를 성공하면 200을 반환합니다.")
    @Test
    void uploadImages() throws Exception {
        // given
        String fileName = "screenshot";
        String contentType = "png";
        String filePath = "src/test/resources/testimages/" + fileName + "." + contentType;
        FileInputStream fileInputStream = new FileInputStream(filePath);

        MockMultipartFile file1 = new MockMultipartFile(
                "images",
                fileName + "." + contentType,
                contentType,
                fileInputStream
        );
        // when
        ResultActions result = mockMvc.perform(multipart("/images")
                .file("files", file1.getBytes())
                .param("category", "images"));

        result.andExpect(status().isCreated())
                .andDo(document("images/upload/success",
                                getDocumentRequest(),
                                getDocumentResponse(),
                                requestParameters(
                                        parameterWithName("category").description("업로드 파일 카테고리")
                                )
                        )
                );
    }
}
