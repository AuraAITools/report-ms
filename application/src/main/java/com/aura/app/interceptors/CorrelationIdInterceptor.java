package com.aura.app.interceptors;

import java.util.Map;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.graphql.server.WebGraphQlInterceptor;
import org.springframework.graphql.server.WebGraphQlRequest;
import org.springframework.graphql.server.WebGraphQlResponse;
import org.springframework.util.StringUtils;
import reactor.core.publisher.Mono;

public class CorrelationIdInterceptor implements WebGraphQlInterceptor {
    private static final Logger log = LoggerFactory.getLogger(CorrelationIdInterceptor.class);
    private final String X_CORRELATION_ID = "x-correlation-id";

    @Override
    public Mono<WebGraphQlResponse> intercept(WebGraphQlRequest request, Chain chain) {
        String correlationId = request.getHeaders().getFirst(X_CORRELATION_ID);
        if (!StringUtils.hasText(correlationId)) {
            correlationId = UUID.randomUUID().toString();
            log.info("no x-correlation-id detected, generated correlation id of {}", correlationId);
        }
        String finalCorrelationId = correlationId;
        request.configureExecutionInput(((_, builder) -> builder.graphQLContext(Map.of(
                X_CORRELATION_ID, finalCorrelationId
        )).build()));
        return chain.next(request);
    }
}
