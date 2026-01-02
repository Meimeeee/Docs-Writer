package com.docsWriter.api.modules.folder.service;

import com.docsWriter.api.database.entities.AccountEntity;
import com.docsWriter.api.database.entities.DocumentEntity;
import com.docsWriter.api.database.entities.FolderEntity;
import com.docsWriter.api.database.repositories.DocumentRepository;
import com.docsWriter.api.database.repositories.FolderRepository;
import com.docsWriter.api.enums.ErrorCode;
import com.docsWriter.api.exception.CustomException;
import com.docsWriter.api.modules.auth.service.AuthService;
import com.docsWriter.api.modules.folder.request.CreateFolderRequestDTO;
import com.docsWriter.api.modules.folder.request.UpdateFolderRequestDTO;
import com.docsWriter.api.modules.folder.response.FolderResponseDTO;
import com.docsWriter.api.utils.BaseResponse;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class FolderService {

    private final FolderRepository folderRepository;
    private final DocumentRepository documentRepository;
    private final AuthService authService;

    @Transactional
    public BaseResponse<FolderResponseDTO> createFolder(CreateFolderRequestDTO dto) {
        AccountEntity owner = authService.getCurrentAccount();
        String normalizedName = dto.getName().trim();

        if (normalizedName.isEmpty()) {
            throw new CustomException(ErrorCode.VALIDATION_ERROR, "Folder name is required");
        }

        folderRepository.findByOwnerIdAndNameIgnoreCase(owner.getId(), normalizedName)
                .ifPresent(folder -> {
                    throw new CustomException(ErrorCode.VALIDATION_ERROR, "Folder name already exists");
                });

        FolderEntity folder = FolderEntity.builder()
                .owner(owner)
                .name(normalizedName)
                .description(trimToNull(dto.getDescription()))
                .build();

        folderRepository.save(folder);
        FolderResponseDTO payload = FolderResponseDTO.from(folder, 0L);
        return BaseResponse.success("Folder created", payload);
    }

    @Transactional(readOnly = true)
    public BaseResponse<List<FolderResponseDTO>> getFolders() {
        AccountEntity owner = authService.getCurrentAccount();
        List<FolderEntity> folders = folderRepository.findAllByOwnerIdOrderByCreatedAtAsc(owner.getId());

        List<FolderResponseDTO> payload = folders.stream()
                .map(folder -> FolderResponseDTO.from(
                        folder,
                        documentRepository.countByOwnerIdAndFolderId(owner.getId(), folder.getId())
                ))
                .toList();

        return BaseResponse.success(payload);
    }

    @Transactional
    public BaseResponse<FolderResponseDTO> updateFolder(UUID folderId, UpdateFolderRequestDTO dto) {
        AccountEntity owner = authService.getCurrentAccount();
        FolderEntity folder = getFolder(folderId, owner.getId());

        if (dto.getName() != null) {
            String normalizedName = dto.getName().trim();
            if (!normalizedName.isEmpty() && !normalizedName.equalsIgnoreCase(folder.getName())) {
                folderRepository.findByOwnerIdAndNameIgnoreCase(owner.getId(), normalizedName)
                        .ifPresent(existing -> {
                            if (!existing.getId().equals(folder.getId())) {
                                throw new CustomException(ErrorCode.VALIDATION_ERROR, "Folder name already exists");
                            }
                        });
                folder.setName(normalizedName);
            }
        }

        if (dto.getDescription() != null) {
            folder.setDescription(trimToNull(dto.getDescription()));
        }

        FolderEntity saved = folderRepository.save(folder);
        long documentCount = documentRepository.countByOwnerIdAndFolderId(owner.getId(), folder.getId());
        return BaseResponse.success("Folder updated", FolderResponseDTO.from(saved, documentCount));
    }

    @Transactional
    public BaseResponse<Void> deleteFolder(UUID folderId) {
        AccountEntity owner = authService.getCurrentAccount();
        FolderEntity folder = getFolder(folderId, owner.getId());

        List<DocumentEntity> documents = documentRepository.findAllByOwnerIdAndFolderId(owner.getId(), folder.getId());
        if (!documents.isEmpty()) {
            documents.forEach(doc -> doc.setFolder(null));
            documentRepository.saveAll(documents);
        }

        folderRepository.delete(folder);
        return BaseResponse.success();
    }


    //=======================HELPER=======================
    private FolderEntity getFolder(UUID folderId, UUID ownerId) {
        return folderRepository.findByIdAndOwnerId(folderId, ownerId)
                .orElseThrow(() -> new CustomException(ErrorCode.FOLDER_NOT_FOUND));
    }

    private String trimToNull(String value) {
        if (value == null) {
            return null;
        }
        String trimmed = value.trim();
        return trimmed.isEmpty() ? null : trimmed;
    }
}
