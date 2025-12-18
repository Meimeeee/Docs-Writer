package com.docsWriter.api.modules.auth.response;

import com.docsWriter.api.database.entities.AccountEntity;
import com.docsWriter.api.database.entities.ProfileEntity;
import com.docsWriter.api.modules.profile.response.ProfileResponseDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AuthResponseDTO {
    private String accessToken;
    private String refreshToken;
    private UUID id;
    private ProfileResponseDTO profile;

    public static AuthResponseDTO toDTO(String accessToken,
                                        String refreshToken,
                                        UUID id,
                                        ProfileEntity profile) {
        return new AuthResponseDTO(accessToken, refreshToken, id, ProfileResponseDTO.toDTO(profile));
    }
}
