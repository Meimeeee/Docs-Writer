package com.docsWriter.api.modules.cloudinary.controller;

import com.docsWriter.api.modules.cloudinary.request.InitUploadRequestDTO;
import com.docsWriter.api.modules.cloudinary.response.InitUploadResponseDTO;
import com.docsWriter.api.modules.cloudinary.service.CloudinaryService;
import com.docsWriter.api.utils.BaseResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/uploads")
@RequiredArgsConstructor
public class CloudinaryController {

    private final CloudinaryService cloudinaryService;

    @PostMapping("/init")
    public BaseResponse<InitUploadResponseDTO> init(
            @Valid @RequestBody InitUploadRequestDTO dto
    ) {
        return cloudinaryService.init(dto);
    }



}
