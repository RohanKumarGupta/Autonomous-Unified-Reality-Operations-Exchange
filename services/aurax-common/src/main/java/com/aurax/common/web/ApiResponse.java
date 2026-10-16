package com.aurax.common.web;

public record ApiResponse<T>(
    boolean success,
    T data,
    ApiError error,
    ResponseMeta meta
) {
    public static <T> ApiResponse<T> success(T data) {
        return new ApiResponse<>(true, data, null, ResponseMeta.now());
    }

    public static <T> ApiResponse<T> error(ApiError error) {
        return new ApiResponse<>(false, null, error, ResponseMeta.now());
    }
}
