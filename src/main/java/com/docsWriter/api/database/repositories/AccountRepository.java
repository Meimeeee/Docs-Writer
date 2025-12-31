package com.docsWriter.api.database.repositories;

import com.docsWriter.api.database.entities.AccountEntity;

import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountRepository extends JpaRepository<AccountEntity, UUID> {
    Optional<AccountEntity> findByEmailIgnoreCase(String email);
    Optional<AccountEntity> findByUsernameIgnoreCase(String username);
    Optional<AccountEntity> findByUsernameIgnoreCaseOrEmailIgnoreCase(String username, String email);
    Optional<AccountEntity> findByGoogleId(String googleId);


}
