package com.docsWriter.api.modules.cloudinary.request;

import lombok.Data;

@Data
public class InitFileRequestDTO {
    String filename;
    Long size;
}
