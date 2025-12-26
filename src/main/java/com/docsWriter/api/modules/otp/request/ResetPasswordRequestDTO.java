package com.docsWriter.api.modules.otp.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ResetPasswordRequestDTO {
    @NotBlank
    @Email
    private String email;
    @NotBlank
    private String otp;
    @NotBlank
    private String newPassword;
}
