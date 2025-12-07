package com.docsWriter.api.modules.auth.response;
import com.docsWriter.api.modules.profile.response.ProfileResponseDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AuthResponseDTO {
    private String accessToken;
    private String refreshToken;
    private ProfileResponseDTO profile;
}
