package com.docsWriter.api.database.repositories;

import com.docsWriter.api.database.entities.AccountEntity;

import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuthenticationRepository extends JpaRepository<AccountEntity, UUID> {
    Optional<AccountEntity> findByEmailIgnoreCase(String email);
    Optional<AccountEntity> findByUsernameIgnoreCase(String username);
}
