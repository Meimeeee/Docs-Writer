package com.docsWriter.api.modules.auth.service;

import com.docsWriter.api.database.entities.AccountEntity;
import com.docsWriter.api.database.entities.ProfileEntity;
import com.docsWriter.api.database.repositories.AccountRepository;
import com.docsWriter.api.database.repositories.ProfileRepository;
import com.docsWriter.api.enums.ErrorCode;
import com.docsWriter.api.exception.CustomException;
import com.docsWriter.api.modules.auth.request.LoginRequestDTO;
import com.docsWriter.api.modules.auth.request.SignupRequestDTO;
import com.docsWriter.api.modules.auth.response.AuthResponseDTO;
import com.docsWriter.api.utils.BaseResponse;
import com.docsWriter.api.utils.JwtUtil;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Collections;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final AccountRepository accountRepository;
    private final ProfileRepository profileRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    @Value("${app.security.google.client-id}")
    private String googleClientId;

    public BaseResponse<AuthResponseDTO> signup(SignupRequestDTO requestDTO) {

        if (requestDTO.getEmail() == null || requestDTO.getEmail().isBlank())
            throw new CustomException(ErrorCode.VALIDATION_ERROR, "email is required");
        if (requestDTO.getUsername() == null || requestDTO.getUsername().isBlank())
            throw new CustomException(ErrorCode.VALIDATION_ERROR, "username is required");

        accountRepository.findByEmailIgnoreCase(requestDTO.getEmail()).ifPresent(account -> {
            throw new CustomException(ErrorCode.EMAIL_ALREADY_IN_USE);
        });

        if (requestDTO.getUsername() != null && !requestDTO.getUsername().isBlank()) {
            accountRepository.findByUsernameIgnoreCase(requestDTO.getUsername()).ifPresent(account -> {
                throw new CustomException(ErrorCode.USERNAME_ALREADY_IN_USE);
            });
        }

        AccountEntity account = AccountEntity.builder().email(requestDTO.getEmail()).username(requestDTO.getUsername()).password(passwordEncoder.encode(requestDTO.getPass())).build();

        ProfileEntity profile = ProfileEntity.builder().fullName(requestDTO.getFullName()).account(account).build();

        accountRepository.save(account);
        profileRepository.save(profile);

        String accessToken = jwtUtil.generateAccessToken(account);
        String refreshToken = jwtUtil.generateRefreshToken(account);

        AuthResponseDTO authResponseDTO = AuthResponseDTO.toDTO(accessToken, refreshToken, account.getId(), profile);

        BaseResponse<AuthResponseDTO> response = new BaseResponse<>();
        response.setData(authResponseDTO);
        response.setMessage("OK");
        response.setSuccess(true);

        return response;
    }

    public BaseResponse<AuthResponseDTO> login(LoginRequestDTO requestDTO) {

        if (requestDTO.getUsername() == null || requestDTO.getUsername().isBlank())
            throw new CustomException(ErrorCode.VALIDATION_ERROR, "username is required");
        if (requestDTO.getPass() == null || requestDTO.getPass().isBlank())
            throw new CustomException(ErrorCode.VALIDATION_ERROR, "password is required");

        AccountEntity account = accountRepository.findByUsernameIgnoreCase(requestDTO.getUsername()).orElseThrow(() -> new CustomException(ErrorCode.ACCOUNT_NOT_FOUND));

        if (!account.isActive()) throw new CustomException(ErrorCode.ACCOUNT_INACTIVE);

        if (account.getPassword() == null || !passwordEncoder.matches(requestDTO.getPass(), account.getPassword()))
            throw new CustomException(ErrorCode.INVALID_PASSWORD);

        ProfileEntity profile = profileRepository.findByAccountId(account.getId()).orElseThrow(() -> new CustomException(ErrorCode.PROFILE_NOT_FOUND));

        String accessToken = jwtUtil.generateAccessToken(account);
        String refreshToken = jwtUtil.generateRefreshToken(account);


        AuthResponseDTO authResponseDTO = AuthResponseDTO.toDTO(accessToken, refreshToken, account.getId(), profile);

        BaseResponse<AuthResponseDTO> response = new BaseResponse<>();
        response.setSuccess(true);
        response.setData(authResponseDTO);
        response.setMessage("OK");

        return response;
    }

    public BaseResponse<AuthResponseDTO> loginWithGoogle(String idTokenString) {
        GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.Builder(new NetHttpTransport(), JacksonFactory.getDefaultInstance()).setAudience(Collections.singletonList(googleClientId)).build();

        GoogleIdToken googleIdToken;
        try {
            googleIdToken = verifier.verify(idTokenString);
        } catch (GeneralSecurityException e) {
            throw new CustomException(ErrorCode.INVALID_TOKEN);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        GoogleIdToken.Payload payload = googleIdToken.getPayload();
        String googleId = payload.getSubject();
        String email = payload.getEmail();
        String fullName = (String) payload.get("name");

        // find/create account
        AccountEntity account = accountRepository.findByGoogleId(googleId).orElseGet(() -> {
            AccountEntity a = AccountEntity.builder().email(email).googleId(googleId).active(true).build();
            a = accountRepository.save(a);

            ProfileEntity p = ProfileEntity.builder().account(a).fullName(fullName).build();
            profileRepository.save(p);

            return a;
        });

        ProfileEntity profile = profileRepository.findByAccountId(account.getId()).orElseThrow(() -> new CustomException(ErrorCode.PROFILE_NOT_FOUND));

        String accessToken = jwtUtil.generateAccessToken(account);
        String refreshToken = jwtUtil.generateRefreshToken(account);

        AuthResponseDTO dto = AuthResponseDTO.toDTO(accessToken, refreshToken, account.getId(), profile);

        BaseResponse<AuthResponseDTO> response = new BaseResponse<>();
        response.setMessage("OK");
        response.setData(dto);
        response.setSuccess(true);
        return response;
    }

}
