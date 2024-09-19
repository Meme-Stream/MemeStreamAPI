package com.memestream.api.service;

import com.memestream.api.model.MemeType;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Service
public class StorageUtil {

    @Value("${application.storage.location}")
    private String storageLocation;

    public Path getStoragePath() {
        return Paths.get(storageLocation);
    }

    public Path getMemesPath() {
        return getStoragePath().resolve("memes");
    }

    public Path getMemePath(UUID id, MemeType type) {
        return getMemesPath().resolve(id + MemeType.convertMemeTypeToFileExtension(type));
    }
}
