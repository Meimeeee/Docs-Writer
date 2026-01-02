package com.docsWriter.api.modules.document.response;

import com.docsWriter.api.database.entities.DocumentEntity;
import java.time.Instant;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DocumentResponseDTO {
    private UUID id;
    private String title;
    private String description;
    private String content;
    private UUID folderId;
    private String folderName;
    private Instant createdAt;
    private Instant updatedAt;

    public static DocumentResponseDTO from(DocumentEntity entity) {
        return DocumentResponseDTO.builder()
                .id(entity.getId())
                .title(entity.getTitle())
                .description(entity.getDescription())
                .content(entity.getContent())
                .folderId(entity.getFolder() != null ? entity.getFolder().getId() : null)
                .folderName(entity.getFolder() != null ? entity.getFolder().getName() : null)
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .build();
    }
}
