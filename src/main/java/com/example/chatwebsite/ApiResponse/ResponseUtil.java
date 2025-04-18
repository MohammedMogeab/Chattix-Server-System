package com.example.chatwebsite.ApiResponse;

import org.springframework.http.ResponseEntity;

public class ResponseUtil {

    public static <T> ResponseEntity<ApiResponse<T>> success(String message, T data) {
        return ResponseEntity.ok(new ApiResponse<>(200, message, data));
    }

    public static ResponseEntity<ApiResponse<Void>> success(String message) {
        return ResponseEntity.ok(new ApiResponse<>(200, message, null));
    }

    public static ResponseEntity<ApiResponse<Void>> error(int code, String message) {
        return ResponseEntity.status(code).body(new ApiResponse<>(code, message, null));
    }
}
