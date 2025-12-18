package com.docsWriter.api.modules.fileStorage.service;

import com.docsWriter.api.database.entities.FileStorageEntity;
import com.docsWriter.api.database.repositories.FileStorageRepository;
import com.docsWriter.api.enums.ErrorCode;
import com.docsWriter.api.exception.CustomException;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Instant;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class FileStorageService {

    private final FileStorageRepository fileStorageRepository;
    private final Path root = Paths.get("uploads");

    public FileStorageEntity save(MultipartFile file, String type){
        try {
            Files.createDirectories(root.resolve(type));

            String storedName = UUID.randomUUID() + "_" + file.getOriginalFilename();
            Path target = root.resolve(type).resolve(storedName);

            Files.copy(file.getInputStream(), target);

            FileStorageEntity entity = FileStorageEntity.builder()
                    .originalName(file.getOriginalFilename())
                    .storedName(storedName)
                    .contentType(file.getContentType())
                    .size(file.getSize())
                    .path(type.toString())
                    .type(type)
                    .createdAt(Instant.now())
                    .build();

            return fileStorageRepository.save(entity);

        } catch (IOException e) {
            throw new CustomException(ErrorCode.UPLOAD_FAILED);
        }
    }

    public Resource load(UUID id){
        FileStorageEntity fileStorage = fileStorageRepository.findById(id)
                .orElseThrow(() -> new CustomException(ErrorCode.FILE_NOT_FOUND));

        try {
            Path path = Paths.get(fileStorage.getPath());
            return new UrlResource(path.toUri());
        } catch (MalformedURLException e) {
            throw new CustomException(ErrorCode.FILE_NOT_FOUND);
        }
    }

}
