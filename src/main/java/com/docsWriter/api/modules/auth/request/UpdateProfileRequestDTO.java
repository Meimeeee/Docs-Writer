package com.docsWriter.api.modules.auth.request;
import lombok.Data;

@Data
public class UpdateProfileRequestDTO {
    private String firstName;
    private String lastName;
}
