package com.docsWriter.api.modules.auth.request;

import com.docsWriter.api.database.entities.FileStorageEntity;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SignupRequestDTO {
    @NotBlank(message = "username is required !")
    private String username;
    @NotBlank(message = "email is required !")
    @Email(message = "it must be email")
    private String email;
    @NotBlank(message = "first name is required !")
    private String firstName;
    @NotBlank(message = "last name is required !")
    private String lastName;
    @NotBlank(message = "password is required !")
    private String pass;
    @NotBlank(message = "Full name is required !")
    private String fullName;
    private String avatarUrl;
}
