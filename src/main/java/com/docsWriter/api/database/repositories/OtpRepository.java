package com.docsWriter.api.database.repositories;

import com.docsWriter.api.database.entities.OtpEntity;
import com.docsWriter.api.enums.OtpPurpose;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

public interface OtpRepository extends JpaRepository<OtpEntity, UUID> {

    Optional<OtpEntity> findTopByEmailAndPurposeOrderByCreatedAtDesc(String email, OtpPurpose purpose);
    long countByEmailAndPurposeAndCreatedAtAfter(String email, OtpPurpose purpose, LocalDateTime after);

}
