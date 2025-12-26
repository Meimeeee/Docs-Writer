package com.docsWriter.api.modules.otp.service;

import com.docsWriter.api.database.entities.OtpEntity;
import com.docsWriter.api.database.repositories.OtpRepository;
import com.docsWriter.api.enums.ErrorCode;
import com.docsWriter.api.enums.OtpPurpose;
import com.docsWriter.api.exception.CustomException;
import com.docsWriter.api.modules.mail.properties.OtpProperties;
import com.docsWriter.api.modules.mail.service.MailService;
import com.docsWriter.api.utils.BaseResponse;
import com.docsWriter.api.utils.OtpUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class OtpService {

    private final MailService mailService;
    private final OtpRepository otpRepository;
    private final OtpProperties otpProperties;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public BaseResponse<Void> sendOtp(String email, OtpPurpose purpose) {

        //check cooldown
        var latestOtp = otpRepository.findTopByEmailAndPurposeOrderByCreatedAtDesc(email, purpose);
        if (latestOtp.isPresent()) {
            var latest = latestOtp.get();
            var nextAllowed = latest.getCreatedAt().plusSeconds(otpProperties.getResendCooldownSeconds());

            //check if otp is expired
            if (LocalDateTime.now().isBefore(nextAllowed)) {
                throw new RuntimeException("Please wait before requesting a new code.");
            }
        }

        String otpPlain = OtpUtil.generate6Digits();
        String otpHash = passwordEncoder.encode(otpPlain);

        OtpEntity otp = OtpEntity.builder()
                .email(email)
                .purpose(purpose)
                .otpHash(otpHash)
                .expiresAt(LocalDateTime.now().plusMinutes(otpProperties.getTtlMinutes()))
                .used(false)
                .attempts(0)
                .createdAt(LocalDateTime.now())
                .build();

        otpRepository.save(otp);
        mailService.sendOtpEmail(email, otpPlain, purpose, otpProperties.getTtlMinutes());

        return BaseResponse.success();
    }


    @Transactional
    public BaseResponse<Void> verifyOtp(String email, OtpPurpose purpose, String otpInput) {
        OtpEntity token = otpRepository.findTopByEmailAndPurposeOrderByCreatedAtDesc(email, purpose)
                .orElseThrow(() -> new RuntimeException("OTP not found. Please request a new code."));

        if (token.isUsed()) {
            throw new RuntimeException("OTP already used. Please request a new code.");
        }

        if (LocalDateTime.now().isAfter(token.getExpiresAt())) {
            throw new RuntimeException("OTP expired. Please request a new code.");
        }

        if (token.getAttempts() >= otpProperties.getMaxAttempts()) {
            throw new RuntimeException("Too many attempts. Please request a new code.");
        }

        // tăng attempts trước (chống brute-force)
        token.setAttempts(token.getAttempts() + 1);

        boolean ok = passwordEncoder.matches(otpInput, token.getOtpHash());
        if (!ok) {
            otpRepository.save(token);
            throw new CustomException(ErrorCode.INVALID_OTP);
        }

        token.setUsed(true);
        token.setVerifiedAt(LocalDateTime.now());
        otpRepository.save(token);

        return BaseResponse.success();
    }


}
