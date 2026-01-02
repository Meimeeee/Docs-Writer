package com.docsWriter.api.modules.folder.controller;

import com.docsWriter.api.modules.folder.request.CreateFolderRequestDTO;
import com.docsWriter.api.modules.folder.request.UpdateFolderRequestDTO;
import com.docsWriter.api.modules.folder.response.FolderResponseDTO;
import com.docsWriter.api.modules.folder.service.FolderService;
import com.docsWriter.api.utils.BaseResponse;
import jakarta.validation.Valid;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/folders")
@RequiredArgsConstructor
public class FolderController {

    private final FolderService folderService;

    @PostMapping("/")
    public BaseResponse<FolderResponseDTO> createFolder(
            @Valid @RequestBody CreateFolderRequestDTO dto
    ) {
        return folderService.createFolder(dto);
    }

    @GetMapping
    public BaseResponse<List<FolderResponseDTO>> getFolders() {
        return folderService.getFolders();
    }

    @PatchMapping("/{folderId}")
    public BaseResponse<FolderResponseDTO> updateFolder(
            @PathVariable UUID folderId,
            @Valid @RequestBody UpdateFolderRequestDTO dto
    ) {
        return folderService.updateFolder(folderId, dto);
    }

    @DeleteMapping("/{folderId}")
    public BaseResponse<Void> deleteFolder(@PathVariable UUID folderId) {
        return folderService.deleteFolder(folderId);
    }
}
