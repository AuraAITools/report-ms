package com.aura.app.interceptors;

import com.aura.app.exceptions.AccessDeniedBusinessException;
import com.aura.app.utils.UserMapper;
import com.aura.core.auth.User;
import com.aura.core.utils.UUIDUtils;
import graphql.GraphQLContext;
import java.util.Optional;
import org.springframework.graphql.server.WebGraphQlInterceptor;
import org.springframework.graphql.server.WebGraphQlRequest;
import org.springframework.graphql.server.WebGraphQlResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import reactor.core.publisher.Mono;


import static java.util.Optional.ofNullable;
import static org.springframework.util.StringUtils.hasText;

public class UserRequestContextInterceptor implements WebGraphQlInterceptor {
    private static final String X_TENANT_ID = "x-tenant-id";
    private static String validateTenantId(String tenantId) {
        if (!hasText(tenantId)) throw new AccessDeniedBusinessException("tenant id cannot be empty");

        if (!UUIDUtils.isUUid(tenantId)) {
            throw new AccessDeniedBusinessException("invalid tenant-id");
        }
        return tenantId;
    }

    private static User extractUserRequestContext(String tenantId) {
        Jwt jwt = Optional.ofNullable(SecurityContextHolder.getContext().getAuthentication())
                .map(Authentication::getPrincipal)
                .filter(Jwt.class::isInstance)
                .map(Jwt.class::cast)
                .orElseThrow(() -> new AccessDeniedBusinessException("Invalid principal type"));
        User currentUser = UserMapper.from(jwt);
        currentUser.setCurrentTenantAccessed(tenantId);

        return currentUser;
    }

    private static void populateGQLContext(WebGraphQlRequest request, User user) {
        request.configureExecutionInput(((executionInput, builder) -> {
            GraphQLContext context = executionInput.getGraphQLContext();
            context.put("user", user);
            return builder.build();
        }));
    }

    @Override
    public Mono<WebGraphQlResponse> intercept(WebGraphQlRequest request, Chain chain) {
        if (isIntrospectionQuery(request)) {
            return chain.next(request);
        }

        Optional<String> targetTenantId = ofNullable(request.getHeaders().getFirst(X_TENANT_ID));
        targetTenantId
                .map(UserRequestContextInterceptor::validateTenantId)
                .map(UserRequestContextInterceptor::extractUserRequestContext)
                .ifPresentOrElse(user -> populateGQLContext(request, user)
                        , () -> {
                            throw new AccessDeniedBusinessException("no target tenant/institution selected");
                        });
        return chain.next(request);
    }

    private static boolean isIntrospectionQuery(WebGraphQlRequest request) {
        String document = request.getDocument();
        return document.contains("__schema") || document.contains("__type");
    }
}
