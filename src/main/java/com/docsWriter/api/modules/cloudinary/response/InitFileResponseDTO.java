package com.docsWriter.api.modules.cloudinary.response;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class InitFileResponseDTO {
    String publicId;
    String filename;
    String url;
    String apiKey;
    long timestamp;
    String folder;
    String signature;
}
