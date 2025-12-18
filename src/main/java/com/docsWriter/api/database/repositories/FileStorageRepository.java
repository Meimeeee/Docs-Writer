package com.docsWriter.api.database.repositories;

import com.docsWriter.api.database.entities.FileStorageEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface FileStorageRepository extends JpaRepository<FileStorageEntity, UUID> {
}
