package com.docsWriter.api.modules.mail.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "app.otp")
public class OtpProperties {
    private int ttlMinutes = 5;
    private int maxAttempts = 5;
    private int resendCooldownSeconds = 60;
}
