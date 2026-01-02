package com.docsWriter.api.modules.document.request;

import jakarta.validation.constraints.Size;
import java.util.UUID;
import lombok.Data;

@Data
public class UpdateDocumentRequestDTO {

    @Size(max = 160, message = "title must be <= 160 characters")
    private String title;

    @Size(max = 400, message = "description must be <= 400 characters")
    private String description;

    private String content;

    private UUID folderId;

    private Boolean detachFromFolder;
}
