package com.aurax.common.web;

import java.time.Instant;
import java.util.UUID;
import org.slf4j.MDC;

public record ResponseMeta(
    Instant timestamp,
    String correlationId,
    String requestId
) {
    public static ResponseMeta now() {
        String correlationId = MDC.get("correlationId");
        if (correlationId == null) {
            correlationId = UUID.randomUUID().toString();
        }
        return new ResponseMeta(Instant.now(), correlationId, UUID.randomUUID().toString());
    }
}
