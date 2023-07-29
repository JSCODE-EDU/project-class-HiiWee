package com.example.anonymousboard.image.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.example.anonymousboard.image.dto.ImagesUploadRequest;
import com.example.anonymousboard.image.exception.FileNotFoundException;
import com.example.anonymousboard.image.exception.FileUploadFailException;
import com.example.anonymousboard.image.utils.FileUtils;
import java.io.IOException;
import java.io.InputStream;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class AwsS3Service {

    private final AmazonS3 amazonS3;

    @Value("${cloud.aws.s3.bucket}")
    private String bucketName;

    public AwsS3Service(final AmazonS3 amazonS3) {
        this.amazonS3 = amazonS3;
    }

    private void uploadFile(final String category, final MultipartFile multipartFile) {
        validateExistFile(multipartFile);
        String fileName = FileUtils.buildFileName(category, multipartFile.getOriginalFilename());
        ObjectMetadata objectMetadata = new ObjectMetadata();
        objectMetadata.setContentType(multipartFile.getContentType());
        System.out.println(multipartFile.getContentType());
        putToS3(multipartFile, fileName, objectMetadata);
    }

    private void putToS3(final MultipartFile multipartFile, final String fileName,
                         final ObjectMetadata objectMetadata) {
        try (InputStream inputStream = multipartFile.getInputStream()) {
            PutObjectRequest putObjectRequest = new PutObjectRequest(bucketName, fileName, inputStream, objectMetadata);
            amazonS3.putObject(putObjectRequest.withCannedAcl(CannedAccessControlList.PublicRead));
        } catch (IOException e) {
            throw new FileUploadFailException();
        }
    }

    public void uploadFiles(final ImagesUploadRequest imagesUploadRequest) {
        for (MultipartFile file : imagesUploadRequest.getFiles()) {
            uploadFile(imagesUploadRequest.getCategory(), file);
        }
    }

    private void validateExistFile(final MultipartFile multipartFile) {
        if (multipartFile.isEmpty()) {
            throw new FileNotFoundException();
        }
    }
}
