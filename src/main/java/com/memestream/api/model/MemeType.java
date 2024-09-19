package com.memestream.api.model;

import org.springframework.http.MediaType;

public enum MemeType {
    VIDEO_MP4,
    IMAGE_PNG,
    IMAGE_JPEG,
    TEXT;

    public static String convertMemeTypeToFileExtension(MemeType type) {
        return switch (type) {
            case VIDEO_MP4 -> ".mp4";
            case IMAGE_PNG -> ".png";
            case IMAGE_JPEG -> ".jpg";
            default -> throw new IllegalStateException("Unexpected value: " + type);
        };
    }

    public static MediaType convertMemeTypeToMediaType(MemeType type) {
        return switch (type) {
            case VIDEO_MP4 -> MediaType.valueOf("video/mp4");
            case IMAGE_PNG -> MediaType.IMAGE_PNG;
            case IMAGE_JPEG -> MediaType.IMAGE_JPEG;
            default -> throw new IllegalStateException("Unexpected value: " + type);
        };
    }
}
