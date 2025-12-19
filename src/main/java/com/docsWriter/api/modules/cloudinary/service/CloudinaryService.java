package com.docsWriter.api.modules.cloudinary.service;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.docsWriter.api.enums.ErrorCode;
import com.docsWriter.api.exception.CustomException;
import com.docsWriter.api.modules.cloudinary.response.InitUploadResponseDTO;
import com.docsWriter.api.utils.CloudinarySign;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Data
public class CloudinaryService {

    private final Cloudinary cloudinary;

    @Value("${app.cloudinary.cloud-name}")
    private String cloudName;

    @Value("${app.cloudinary.API-key}")
    private String apiKey;

    @Value("${app.cloudinary.API-secret}")
    private String apiSecret;


    public InitUploadResponseDTO init(String resourceType) {
        long ts = Instant.now().getEpochSecond();
        String publicId = "u_" + UUID.randomUUID();

        Map<String, String> paramToSign = new HashMap<>();
        paramToSign.put("ts", String.valueOf(ts));
        paramToSign.put("public_id", publicId);

        String signature = CloudinarySign.sign(paramToSign, apiSecret);
        String uploadUrl =
                "http://api.cloudinary.com/"
                        + cloudName
                        + "/" + resourceType
                        + "/upload";

        return new InitUploadResponseDTO(
                UUID.randomUUID().toString(),
                uploadUrl,
                apiKey,
                ts,
                publicId,
                signature
        );
    }

    public void complete(String publicId, String resourceType) {
        try {
            cloudinary.api().resource(
                    publicId,
                    ObjectUtils.asMap("resource_type", resourceType)
            );
        } catch (Exception e) {
            throw new CustomException(ErrorCode.CLOUDINARY_VERIFICATION_FAILED);
        }
    }

}
