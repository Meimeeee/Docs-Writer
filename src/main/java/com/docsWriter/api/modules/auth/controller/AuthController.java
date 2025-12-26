package com.docsWriter.api.modules.auth.controller;

import com.docsWriter.api.modules.auth.request.*;
import com.docsWriter.api.modules.auth.response.AuthResponseDTO;
import com.docsWriter.api.modules.auth.service.AuthService;
import com.docsWriter.api.modules.otp.request.ResetPasswordRequestDTO;
import com.docsWriter.api.modules.otp.request.SendOtpRequestDTO;
import com.docsWriter.api.modules.otp.request.VerifyOtpRequestDTO;
import com.docsWriter.api.modules.otp.service.OtpService;
import com.docsWriter.api.utils.BaseResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    private final OtpService otpService;


    @PostMapping("/signup")
    public BaseResponse<AuthResponseDTO> signup(@RequestBody SignupRequestDTO request) {
        BaseResponse<AuthResponseDTO> res = authService.signup(request);
        return res;
    }

    @PostMapping("/login")
    public BaseResponse<AuthResponseDTO> login(@RequestBody LoginRequestDTO request) {
        BaseResponse<AuthResponseDTO> res = authService.login(request);
        return res;
    }

    @PostMapping("/google")
    public BaseResponse<AuthResponseDTO> googleLogin(@RequestBody GoogleLoginRequestDTOO googleLoginRequestDTOO) {
        return authService.loginWithGoogle(googleLoginRequestDTOO.getIdToken());
    }

    @PutMapping("/update_profile")
    public BaseResponse<Void> updateAccount(@RequestBody UpdateProfileRequestDTO dto){
        return authService.updateAccount(dto);
    }

    @PostMapping("/otp/send")
    public void sendOtp(@RequestBody SendOtpRequestDTO dto){
        otpService.sendOtp(dto.getEmail(), dto.getPurpose());
    }

    @PostMapping("/otp/verify")
    public void verifyOtp(@RequestBody VerifyOtpRequestDTO dto){
        otpService.verifyOtp(dto.getEmail(), dto.getPurpose(), dto.getOtp());
    }

    @PostMapping("/reset_password")
    public BaseResponse<Void> resetPassword(@RequestBody ResetPasswordRequestDTO dto){
        return authService.resetPassword(dto);
    }

    @PostMapping("/update_password")
    public BaseResponse<Void> updatePassword(@RequestBody UpdatePasswordRequestDTO dto){
        return authService.updatePassword(dto);
    }



}
