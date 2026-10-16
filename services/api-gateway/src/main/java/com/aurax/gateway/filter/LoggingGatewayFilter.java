package com.aurax.gateway.filter;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Slf4j
@Component
public class LoggingGatewayFilter implements GlobalFilter, Ordered {

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        long startTime = System.currentTimeMillis();
        String path = exchange.getRequest().getURI().getPath();
        String method = exchange.getRequest().getMethod().name();
        String correlationId = exchange.getRequest().getHeaders().getFirst(CorrelationIdGatewayFilter.CORRELATION_ID_HEADER_NAME);

        log.info("Incoming request: method={}, path={}, correlationId={}", method, path, correlationId);

        return chain.filter(exchange).then(Mono.fromRunnable(() -> {
            long duration = System.currentTimeMillis() - startTime;
            int statusCode = exchange.getResponse().getStatusCode() != null ? 
                    exchange.getResponse().getStatusCode().value() : 0;
            
            log.info("Outgoing response: method={}, path={}, correlationId={}, status={}, duration={}ms",
                    method, path, correlationId, statusCode, duration);
        }));
    }

    @Override
    public int getOrder() {
        return 0;
    }
}
