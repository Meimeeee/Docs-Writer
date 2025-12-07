package com.docsWriter.api.database.repositories;

import com.docsWriter.api.database.entities.DocumentEntity;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DocumentRepository extends JpaRepository<DocumentEntity, UUID> {
    List<DocumentEntity> findAllByOwnerId(UUID ownerId);
    Optional<DocumentEntity> findByOwnerIdAndId(UUID ownerId, UUID documentId);
}
