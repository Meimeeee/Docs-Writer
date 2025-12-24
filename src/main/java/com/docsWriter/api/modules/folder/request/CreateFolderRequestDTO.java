package com.docsWriter.api.modules.folder.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class CreateFolderRequestDTO {

    @NotBlank(message = "name is required")
    @Size(max = 160, message = "name must be <= 160 characters")
    private String name;

    @Size(max = 500, message = "description must be <= 500 characters")
    private String description;
}
