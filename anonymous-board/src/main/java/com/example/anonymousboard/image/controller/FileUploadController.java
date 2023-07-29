package com.example.anonymousboard.image.controller;

import com.example.anonymousboard.image.dto.ImagesUploadRequest;
import com.example.anonymousboard.image.service.AwsS3Service;
import javax.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class FileUploadController {

    private final AwsS3Service awsS3Service;

    public FileUploadController(final AwsS3Service awsS3Service) {
        this.awsS3Service = awsS3Service;
    }

    @PostMapping("/images")
    public ResponseEntity<Void> uploadImages(@Valid final ImagesUploadRequest imagesUploadRequest) {
        awsS3Service.uploadFiles(imagesUploadRequest);
        return ResponseEntity.status(HttpStatus.CREATED)
                .build();
    }
}
