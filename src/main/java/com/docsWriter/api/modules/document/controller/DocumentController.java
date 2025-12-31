package com.docsWriter.api.modules.document.controller;

import com.docsWriter.api.modules.document.request.CreateDocumentRequestDTO;
import com.docsWriter.api.modules.document.request.UpdateDocumentRequestDTO;
import com.docsWriter.api.modules.document.response.DocumentResponseDTO;
import com.docsWriter.api.modules.document.service.DocumentService;
import com.docsWriter.api.utils.BaseResponse;
import jakarta.validation.Valid;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/documents")
@RequiredArgsConstructor
public class DocumentController {

    private final DocumentService documentService;

    @PostMapping
    public BaseResponse<DocumentResponseDTO> createDocument(
            @Valid @RequestBody CreateDocumentRequestDTO dto
    ) {
        return documentService.createDocument(dto);
    }

    @GetMapping
    public BaseResponse<List<DocumentResponseDTO>> getDocuments(
            @RequestParam(name = "folderId", required = false) UUID folderId
    ) {
        return documentService.getDocuments(folderId);
    }

    @GetMapping("/{documentId}")
    public BaseResponse<DocumentResponseDTO> getDocument(
            @PathVariable UUID documentId
    ) {
        return documentService.getDocument(documentId);
    }

    @PatchMapping("/{documentId}")
    public BaseResponse<DocumentResponseDTO> updateDocument(
            @PathVariable UUID documentId,
            @Valid @RequestBody UpdateDocumentRequestDTO dto
    ) {
        return documentService.updateDocument(documentId, dto);
    }

    @DeleteMapping("/{documentId}")
    public BaseResponse<Void> deleteDocument(
            @PathVariable UUID documentId
    ) {
        return documentService.deleteDocument(documentId);
    }
}
