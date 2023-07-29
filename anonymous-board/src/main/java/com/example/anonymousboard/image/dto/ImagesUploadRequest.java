package com.example.anonymousboard.image.dto;

import java.util.List;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;
import org.springframework.web.multipart.MultipartFile;

@Getter
public class ImagesUploadRequest {

    @NotBlank(message = "카테고리는 반드시 입력해야 합니다.")
    private String category;

    @NotNull(message = "이미지는 반드시 첨부해야 합니다.")
    @Size.List(value = @Size(min = 1, max = 10, message = "최대 10장까지 업로드할 수 있습니다."))
    private List<MultipartFile> files;

    @Builder
    private ImagesUploadRequest(final String category, final List<MultipartFile> files) {
        this.category = category;
        this.files = files;
    }
}
