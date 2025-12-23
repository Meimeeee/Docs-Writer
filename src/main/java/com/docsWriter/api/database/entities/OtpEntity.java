package com.docsWriter.api.database.entities;

import com.docsWriter.api.enums.OtpPurpose;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "otp")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OtpEntity {
    @Id
    @GeneratedValue
    private UUID id;

    @Column(nullable = false)
    private String email;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private OtpPurpose purpose;

    @Column(nullable = false, length = 100)
    private String otpHash;

    @Column(nullable = false)
    private LocalDateTime expiresAt;

    @Column(nullable = false)
    private boolean used;

    @Column(nullable = false)
    private int attempts;

    // để rate limit resend
    @Column(nullable = false)
    private LocalDateTime createdAt;

    private LocalDateTime verifiedAt;
}
