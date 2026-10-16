package com.aurax.gateway.config;

import org.springframework.cloud.gateway.filter.ratelimit.KeyResolver;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import reactor.core.publisher.Mono;

import java.net.InetSocketAddress;

@Configuration
public class RateLimiterConfig {

    @Bean
    public KeyResolver ipKeyResolver() {
        return exchange -> {
            InetSocketAddress address = exchange.getRequest().getRemoteAddress();
            if (address != null && address.getAddress() != null) {
                return Mono.just(address.getAddress().getHostAddress());
            }
            return Mono.just("unknown");
        };
    }
}
