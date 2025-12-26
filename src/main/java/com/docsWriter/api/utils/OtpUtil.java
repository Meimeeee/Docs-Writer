package com.docsWriter.api.utils;

import java.security.SecureRandom;

public class OtpUtil {
    public static final SecureRandom SECURE_RANDOM = new SecureRandom();

    public static String generate6Digits() {
        int n = SECURE_RANDOM.nextInt(900000) + 100000;
        return String.valueOf(n);
    }

}
