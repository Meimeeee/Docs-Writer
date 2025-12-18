package com.docsWriter.api.modules.fileStorage.controller;

import com.docsWriter.api.database.entities.FileStorageEntity;
import com.docsWriter.api.modules.fileStorage.service.FileStorageService;
import com.docsWriter.api.utils.BaseResponse;
import lombok.RequiredArgsConstructor;
import org.osgi.resource.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

@RestController
@RequestMapping("/api/files")
@RequiredArgsConstructor
public class FileStorageController {

    private final FileStorageService fileStorageService;

    @PostMapping("/upload")
    public BaseResponse<UUID> upload(
            @RequestParam("file") MultipartFile file,
            @RequestParam(defaultValue = "image") String type
    ) {
        FileStorageEntity saved = fileStorageService.save(file, type);

        return BaseResponse.success(saved.getId());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Resource> get(@PathVariable UUID id) {
        Resource file = (Resource) fileStorageService.load(id);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "inline")
                .body(file);
    }

}
