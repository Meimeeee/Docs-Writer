package com.docsWriter.api.modules.document.service;

import com.docsWriter.api.database.entities.AccountEntity;
import com.docsWriter.api.database.entities.DocumentEntity;
import com.docsWriter.api.database.entities.FolderEntity;
import com.docsWriter.api.database.repositories.DocumentRepository;
import com.docsWriter.api.database.repositories.FolderRepository;
import com.docsWriter.api.enums.ErrorCode;
import com.docsWriter.api.exception.CustomException;
import com.docsWriter.api.modules.auth.service.AuthService;
import com.docsWriter.api.modules.document.request.CreateDocumentRequestDTO;
import com.docsWriter.api.modules.document.request.UpdateDocumentRequestDTO;
import com.docsWriter.api.modules.document.response.DocumentResponseDTO;
import com.docsWriter.api.utils.BaseResponse;

import java.util.List;
import java.util.UUID;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class DocumentService {

    private final DocumentRepository documentRepository;
    private final FolderRepository folderRepository;
    private final AuthService authService;

    private static final String DEFAULT_DOC_TITLE = "New document";


    @Transactional
    public BaseResponse<DocumentResponseDTO> createDocument(CreateDocumentRequestDTO dto) {
        AccountEntity owner = authService.getCurrentAccount();
        FolderEntity folder = getFolder(owner.getId(), dto.getFolderId());

        String normalizedTitle = validTitle(dto.getTitle());
        String normalizedContent = trimToNull(dto.getContent());
        String normalizedDescription = trimToNull(dto.getDescription());

        DocumentEntity document = DocumentEntity.builder().owner(owner).folder(folder).title(normalizedTitle).description(normalizedDescription).content(normalizedContent).build();

        DocumentEntity saved = documentRepository.save(document);
        return BaseResponse.success("Document created", DocumentResponseDTO.from(saved));
    }

    @Transactional(readOnly = true)
    public BaseResponse<List<DocumentResponseDTO>> getDocuments(UUID folderId) {
        AccountEntity owner = authService.getCurrentAccount();
        List<DocumentEntity> documents;

        if (folderId != null) {
            getFolder(owner.getId(), folderId);
            documents = documentRepository.findAllByOwnerIdAndFolderId(owner.getId(), folderId);
        } else {
            documents = documentRepository.findAllByOwnerId(owner.getId());
        }

        List<DocumentResponseDTO> payload = documents.stream().map(DocumentResponseDTO::from).toList();

        return BaseResponse.success(payload);
    }

    @Transactional(readOnly = true)
    public BaseResponse<DocumentResponseDTO> getDocumentById(UUID documentId) {
        AccountEntity owner = authService.getCurrentAccount();
        DocumentEntity document = getDocumentOrThrow(owner.getId(), documentId);
        return BaseResponse.success(DocumentResponseDTO.from(document));
    }

    @Transactional
    public BaseResponse<DocumentResponseDTO> updateDocument(UUID documentId, UpdateDocumentRequestDTO dto) {
        AccountEntity owner = authService.getCurrentAccount();
        DocumentEntity document = getDocumentOrThrow(owner.getId(), documentId);

        if (dto.getTitle() != null) {
            String normalizedTitle = dto.getTitle().trim();
            if (normalizedTitle.isEmpty()) {
                normalizedTitle = DEFAULT_DOC_TITLE;
            }
            document.setTitle(normalizedTitle);
        }

        if (dto.getDescription() != null) {
            document.setDescription(trimToNull(dto.getDescription()));
        }

        if (dto.getContent() != null) {
            String normalizedContent = dto.getContent().trim();
            document.setContent(normalizedContent);
        }

        if (Boolean.TRUE.equals(dto.getDetachFromFolder())) {
            document.setFolder(null);
        } else if (dto.getFolderId() != null) {
            FolderEntity folder = getFolder(owner.getId(), dto.getFolderId());
            document.setFolder(folder);
        }

        DocumentEntity saved = documentRepository.save(document);
        return BaseResponse.success("Document updated", DocumentResponseDTO.from(saved));
    }

    @Transactional
    public BaseResponse<Void> deleteDocument(UUID documentId) {
        AccountEntity owner = authService.getCurrentAccount();
        DocumentEntity document = getDocumentOrThrow(owner.getId(), documentId);
        documentRepository.delete(document);
        return BaseResponse.success();
    }


    // ======================HELPER======================
    private DocumentEntity getDocumentOrThrow(UUID ownerId, UUID documentId) {
        return documentRepository.findByOwnerIdAndId(ownerId, documentId).orElseThrow(() -> new CustomException(ErrorCode.DOCUMENT_NOT_FOUND));
    }

    private FolderEntity getFolder(UUID ownerId, UUID folderId) {
        if (folderId == null) {
            return null;
        }
        return folderRepository.findByIdAndOwnerId(folderId, ownerId).orElseThrow(() -> new CustomException(ErrorCode.FOLDER_NOT_FOUND));
    }

    private String validTitle(String value) {
        if (value == null) return DEFAULT_DOC_TITLE;
        else return value.trim().isEmpty() ? DEFAULT_DOC_TITLE : value.trim();
    }

    private String trimToNull(String value) {
        if (value == null) {
            return null;
        }
        String trimmed = value.trim();
        return trimmed.isEmpty() ? null : trimmed;
    }
}
