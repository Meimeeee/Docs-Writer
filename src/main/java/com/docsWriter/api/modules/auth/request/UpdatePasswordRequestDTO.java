package com.docsWriter.api.modules.auth.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class UpdatePasswordRequestDTO {
    @NotBlank
    private String oldPassword;
    @NotBlank
    private String newPassword;
}
