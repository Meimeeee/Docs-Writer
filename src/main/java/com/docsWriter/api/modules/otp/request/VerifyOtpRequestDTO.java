package com.docsWriter.api.modules.otp.request;

import com.docsWriter.api.enums.OtpPurpose;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class VerifyOtpRequestDTO {
    private String email;
    private OtpPurpose purpose;
    private String otp;
}
