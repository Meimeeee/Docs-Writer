package com.docsWriter.api.utils;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BaseResponse<T> {
    private int status;
    private boolean success;
    private String message;
    private T data;
    private Map<String, List<String>> fieldErrors;
    private LocalDateTime timestamp;

    public static <T> BaseResponse<T> success() {
        return BaseResponse.<T>builder()
                .status(200)
                .success(true)
                .build();
    }

    public static <T> BaseResponse<T> success(T data) {
        return BaseResponse.<T>builder()
                .status(200)
                .success(true)
                .data(data)
                .build();
    }

    public static <T> BaseResponse<T> success(String message, T data) {
        return BaseResponse.<T>builder()
                .status(200)
                .success(true)
                .message(message)
                .data(data)
                .build();
    }

    public static <T> BaseResponse<T> failure(String message) {
        return BaseResponse.<T>builder()
                .success(false)
                .message(message)
                .build();
    }
}
