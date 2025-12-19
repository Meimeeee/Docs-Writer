package com.docsWriter.api.utils;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;

public class CloudinarySign {

    public static String sign(
            Map<String, String> paramsToSign,
            String apiSecret
    ) {
        String paramString = new TreeMap<>(paramsToSign)
                .entrySet()
                .stream()
                .filter(e -> e.getValue() != null && !e.getValue().isBlank())
                .map(e -> e.getKey() + "=" + e.getValue())
                .collect(Collectors.joining("&"));

        return sha1(paramString + apiSecret);
    }

    private static String sha1(String input) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-1");
            byte[] digest = md.digest(input.getBytes(StandardCharsets.UTF_8));
            StringBuilder sb = new StringBuilder();
            for (byte b : digest) sb.append(String.format("%02x", b));
            return sb.toString();
        } catch (Exception e) {
            throw new RuntimeException("Cannot sha1", e);
        }
    }

}
