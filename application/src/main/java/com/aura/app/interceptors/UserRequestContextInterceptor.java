package com.aura.app.interceptors;

import com.aura.app.exceptions.AccessDeniedBusinessException;
import com.aura.app.utils.UserMapper;
import com.aura.core.auth.User;
import graphql.GraphQLContext;
import java.util.Map;
import java.util.Optional;
import org.springframework.graphql.server.WebGraphQlInterceptor;
import org.springframework.graphql.server.WebGraphQlRequest;
import org.springframework.graphql.server.WebGraphQlResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import reactor.core.publisher.Mono;

public class UserRequestContextInterceptor implements WebGraphQlInterceptor {

    @Override
    public Mono<WebGraphQlResponse> intercept(WebGraphQlRequest request, Chain chain) {
        if (isIntrospectionQuery(request)) {
            return chain.next(request);
        }

        Jwt jwt = Optional.ofNullable(SecurityContextHolder.getContext().getAuthentication())
                .map(Authentication::getPrincipal)
                .filter(Jwt.class::isInstance)
                .map(Jwt.class::cast)
                .orElseThrow(() -> new AccessDeniedBusinessException("Invalid principal type"));

        User user = UserMapper.from(jwt);

        // Resolve institutionId from GraphQL variables: top-level or nested in input
        resolveInstitutionId(request.getVariables())
                .ifPresent(user::setCurrentTenantAccessed);

        request.configureExecutionInput((executionInput, builder) -> {
            GraphQLContext context = executionInput.getGraphQLContext();
            context.put("user", user);
            return builder.build();
        });

        return chain.next(request);
    }

    static Optional<String> resolveInstitutionId(Map<String, Object> variables) {
        // Check top-level institutionId (used by queries)
        Object topLevel = variables.get("institutionId");
        if (topLevel instanceof String id && !id.isBlank()) {
            return Optional.of(id);
        }

        // Check input.institutionId (used by mutations)
        Object input = variables.get("input");
        if (input instanceof Map<?, ?> inputMap) {
            Object nested = inputMap.get("institutionId");
            if (nested instanceof String id && !id.isBlank()) {
                return Optional.of(id);
            }
        }

        return Optional.empty();
    }

    private static boolean isIntrospectionQuery(WebGraphQlRequest request) {
        String document = request.getDocument();
        return document.contains("__schema") || document.contains("__type");
    }
}
