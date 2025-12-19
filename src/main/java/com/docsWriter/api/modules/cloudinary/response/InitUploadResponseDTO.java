package com.docsWriter.api.modules.cloudinary.response;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class InitUploadResponseDTO {
    String sessionId;
    String uploadUrl;
    String apiKey;
    long timestamp;
    String publicId;
    String signature;
}
