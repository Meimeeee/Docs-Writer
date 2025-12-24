package com.docsWriter.api.modules.folder.response;

import com.docsWriter.api.database.entities.FolderEntity;
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
public class FolderResponseDTO {
    private UUID id;
    private String name;
    private String description;
    private long documentCount;
    private Instant createdAt;
    private Instant updatedAt;

    public static FolderResponseDTO from(FolderEntity entity, long documentCount) {
        return FolderResponseDTO.builder()
                .id(entity.getId())
                .name(entity.getName())
                .description(entity.getDescription())
                .documentCount(documentCount)
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .build();
    }
}
