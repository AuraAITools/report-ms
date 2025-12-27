package com.aura.app.interceptors;

import com.aura.app.controllers.graphql.v1.institutions.exceptions.AccessDeniedException;
import com.aura.app.utils.UserMapper;
import com.aura.core.auth.User;
import com.aura.core.utils.UUIDUtils;
import graphql.GraphQLContext;
import java.util.Optional;
import org.springframework.graphql.server.WebGraphQlInterceptor;
import org.springframework.graphql.server.WebGraphQlRequest;
import org.springframework.graphql.server.WebGraphQlResponse;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import reactor.core.publisher.Mono;


import static java.util.Optional.ofNullable;
import static org.springframework.util.StringUtils.hasText;

public class UserRequestContextInterceptor implements WebGraphQlInterceptor {
    private static final String X_TENANT_ID = "x-tenant-id";
    private static final String AURA_ADMIN = "AURA_ADMIN";

    private static String validateTenantId(String tenantId) {
        if (!hasText(tenantId)) throw new AccessDeniedException("tenant id cannot be empty");

        boolean isUUID = UUIDUtils.isUUid(tenantId);
        if (!isUUID && !AURA_ADMIN.equals(tenantId)) {
            throw new AccessDeniedException("invalid tenant-id");
        }
        return tenantId;
    }

    private static User extractUserRequestContext(String tenantId) {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (!(principal instanceof Jwt jwt)) throw new AccessDeniedException("Invalid principal type");
        User currentUser = UserMapper.from(jwt);

        // if aura_admin, set current tenantId = AURA_ADMIN
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
        Optional<String> targetTenantId = ofNullable(request.getHeaders().getFirst(X_TENANT_ID));
        targetTenantId
                .map(UserRequestContextInterceptor::validateTenantId)
                .map(UserRequestContextInterceptor::extractUserRequestContext)
                .ifPresentOrElse((user) -> populateGQLContext(request, user)
                        , () -> {
                            throw new AccessDeniedException("no target tenant/institution selected");
                        });
        return chain.next(request);
    }
}
