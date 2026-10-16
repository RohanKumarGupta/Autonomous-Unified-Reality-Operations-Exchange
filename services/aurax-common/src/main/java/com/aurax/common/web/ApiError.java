package com.aurax.common.web;

import java.time.Instant;
import java.util.List;

public record ApiError(
    String code,
    String message,
    int status,
    List<String> details,
    String path,
    Instant timestamp,
    String correlationId
) {
}
