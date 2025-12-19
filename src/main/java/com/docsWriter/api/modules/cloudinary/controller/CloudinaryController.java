package com.docsWriter.api.modules.cloudinary.controller;

import com.docsWriter.api.modules.cloudinary.response.InitUploadResponseDTO;
import com.docsWriter.api.modules.cloudinary.service.CloudinaryService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/cloudinary")
@RequiredArgsConstructor
public class CloudinaryController {

    private final CloudinaryService cloudinaryService;

    @PostMapping("/init")
    public InitUploadResponseDTO init(
            @RequestParam(defaultValue = "image") String resourceType
    ) {
        return cloudinaryService.init(resourceType);
    }

    @PostMapping("/complete")
    public void complete(
            @RequestParam String publicId,
            @RequestParam(defaultValue = "image") String resourceType
    ) {
        cloudinaryService.complete(publicId, resourceType);
    }


}
