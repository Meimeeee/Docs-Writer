package com.docsWriter.api.modules.otp.request;

import com.docsWriter.api.enums.OtpPurpose;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class SendOtpRequestDTO {
    private String email;
    private OtpPurpose purpose;
}
