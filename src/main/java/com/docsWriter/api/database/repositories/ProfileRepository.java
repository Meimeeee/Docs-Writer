package com.docsWriter.api.database.repositories;

import com.docsWriter.api.database.entities.ProfileEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface ProfileRepository extends JpaRepository<ProfileEntity, UUID> {
    Optional<ProfileEntity> findByAccountId(UUID accountId);
}
