package com.example.anonymousboard.image.utils;

public class FileUtils {

    private static final String FILE_EXTENSION_SEPARATOR = ".";
    private static final String FILE_SEPARATOR = "_";
    private static final String CATEGORY_PREFIX = "/";

    public static String buildFileName(final String category, final String originalFileName) {
        int fileExtensionIndex = originalFileName.lastIndexOf(FILE_EXTENSION_SEPARATOR);
        String fileExtension = originalFileName.substring(fileExtensionIndex);
        String fileName = originalFileName.substring(0, fileExtensionIndex);
        String currentTime = String.valueOf(System.currentTimeMillis());
        return category + CATEGORY_PREFIX + fileName + FILE_SEPARATOR + currentTime + FILE_EXTENSION_SEPARATOR
                + fileExtension;
    }
}
