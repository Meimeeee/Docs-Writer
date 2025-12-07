package com.docsWriter.api.modules.profile.response;

import com.docsWriter.api.database.entities.ProfileEnity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProfileResponseDTO {
    private UUID id;
    private String fullName;
    private String avatarUrl;
    private String bio;

    public static ProfileResponseDTO toDTO(ProfileEnity enity) {
        return ProfileResponseDTO.builder()
                .fullName(enity.getFullName())
                .avatarUrl(enity.getAvatarUrl())
                .bio(enity.getBio())
                .build();
    }

}
