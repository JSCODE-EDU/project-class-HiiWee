package com.example.anonymousboard.image.service;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

public class StubMultipartFile implements MultipartFile {

    private final boolean emptyStatus;

    public StubMultipartFile(final boolean emptyStatus) {
        this.emptyStatus = emptyStatus;
    }

    @Override
    public String getName() {
        return null;
    }

    @Override
    public String getOriginalFilename() {
        return "test.jpg";
    }

    @Override
    public String getContentType() {
        return "image/jpg";
    }

    @Override
    public boolean isEmpty() {
        return emptyStatus;
    }

    @Override
    public long getSize() {
        return 100;
    }

    @Override
    public byte[] getBytes() throws IOException {
        return new byte[0];
    }

    @Override
    public InputStream getInputStream() throws IOException {
        return null;
    }

    @Override
    public Resource getResource() {
        return MultipartFile.super.getResource();
    }

    @Override
    public void transferTo(final File dest) throws IOException, IllegalStateException {

    }

    @Override
    public void transferTo(final Path dest) throws IOException, IllegalStateException {
        MultipartFile.super.transferTo(dest);
    }
}
