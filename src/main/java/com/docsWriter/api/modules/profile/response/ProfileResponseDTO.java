package com.docsWriter.api.modules.profile.response;

import com.docsWriter.api.database.entities.ProfileEntity;
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
    private String firstName;
    private String lastName;
    private String avatarUrl;
    private String bio;

    public static ProfileResponseDTO toDTO(ProfileEntity enity) {
        return ProfileResponseDTO.builder()
                .firstName(enity.getFirstName())
                .lastName(enity.getLastName())
                .avatarUrl(enity.getAvatarUrl())
                .bio(enity.getBio())
                .build();
    }

}
