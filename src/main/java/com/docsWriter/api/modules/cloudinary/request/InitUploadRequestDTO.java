package com.docsWriter.api.modules.cloudinary.request;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class InitUploadRequestDTO {
    List<InitFileRequestDTO> files;
}
