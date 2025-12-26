package com.docsWriter.api.modules.cloudinary.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class InitUploadResponseDTO {
    List<InitFileResponseDTO> files;
}
