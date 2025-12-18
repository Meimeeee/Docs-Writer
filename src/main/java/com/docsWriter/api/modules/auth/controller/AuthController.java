package com.docsWriter.api.modules.auth.controller;

import com.docsWriter.api.modules.auth.request.GoogleLoginRequestDTOO;
import com.docsWriter.api.modules.auth.request.LoginRequestDTO;
import com.docsWriter.api.modules.auth.request.SignupRequestDTO;
import com.docsWriter.api.modules.auth.response.AuthResponseDTO;
import com.docsWriter.api.modules.auth.service.AuthService;
import com.docsWriter.api.utils.BaseResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

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

}
