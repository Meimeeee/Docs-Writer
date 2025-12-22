package com.docsWriter.api.modules.cloudinary.service;

import com.cloudinary.Cloudinary;
import com.docsWriter.api.modules.cloudinary.request.InitFileRequestDTO;
import com.docsWriter.api.modules.cloudinary.request.InitUploadRequestDTO;
import com.docsWriter.api.modules.cloudinary.response.InitFileResponseDTO;
import com.docsWriter.api.modules.cloudinary.response.InitUploadResponseDTO;
import com.docsWriter.api.utils.BaseResponse;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.HashMap;
import java.util.List;
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

    @Value("${app.cloudinary.upload-folder:uploads}")
    private String baseFolder;

    @Value(("${app.cloudinary.api-base-url}"))
    private String apiBaseUrl;


    //    private static final long MAX_FILE_SIZE = 10 * 1024 * 1024;
    private static final long MAX_IMAGE_SIZE = 5L * 1024 * 1024;   // 5MB
    private static final long MAX_PDF_SIZE = 10L * 1024 * 1024;  // 10MB

    public BaseResponse<InitUploadResponseDTO> init(InitUploadRequestDTO dto) {
        long ts = Instant.now().getEpochSecond();
        String folder = baseFolder;
        String uploadUrl = apiBaseUrl + cloudName + folder;

        List<InitFileResponseDTO> files = dto.getFiles().stream().map(file -> initOne(uploadUrl, folder, ts, file)).toList();

        InitUploadResponseDTO init = new InitUploadResponseDTO(files);

        return BaseResponse.success(init);
    }


    //    ================================HELPER================================
    private InitFileResponseDTO initOne(String uploadUrl, String folder, long timestamp, InitFileRequestDTO f) {
        validateFile(f);

        String publicId = UUID.randomUUID().toString();

        Map<String, Object> paramsToSign = new HashMap<>();
        paramsToSign.put("timestamp", String.valueOf(timestamp));
        paramsToSign.put("folder", folder);
        paramsToSign.put("public_id", publicId);

        String signature = cloudinary.apiSignRequest(paramsToSign, apiSecret);

        return new InitFileResponseDTO(publicId, f.getFilename(), uploadUrl, apiKey, timestamp, folder, signature);
    }

    private void validateFile(InitFileRequestDTO f) {
        String ext = getExtension(f.getFilename());

        long maxSize = switch (ext) {
            case "jpg", "jpeg", "png", "webp", "gif" -> MAX_IMAGE_SIZE;
            case "pdf" -> MAX_PDF_SIZE;
            default -> throw new IllegalArgumentException("Unsupported file type: " + f.getFilename());
        };

        if (f.getSize() > maxSize) {
            throw new IllegalArgumentException("File too large: " + f.getFilename());
        }
    }

    private String getExtension(String filename) {
        int i = filename.lastIndexOf('.');
        return (i > 0 && i < filename.length() - 1) ? filename.substring(i + 1).toLowerCase() : "";
    }

}
