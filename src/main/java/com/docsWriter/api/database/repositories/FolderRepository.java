package com.docsWriter.api.database.repositories;

import com.docsWriter.api.database.entities.FolderEntity;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FolderRepository extends JpaRepository<FolderEntity, UUID> {

    List<FolderEntity> findAllByOwnerIdOrderByCreatedAtAsc(UUID ownerId);

    Optional<FolderEntity> findByIdAndOwnerId(UUID id, UUID ownerId);

    Optional<FolderEntity> findByOwnerIdAndNameIgnoreCase(UUID ownerId, String name);
}
