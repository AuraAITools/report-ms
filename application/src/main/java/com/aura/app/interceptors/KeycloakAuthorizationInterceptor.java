package com.aura.app.interceptors;

import com.aura.app.annotations.ProtectedOperation;
import com.aura.app.controllers.graphql.v1.institutions.exceptions.AccessDeniedException;
import com.aura.core.clients.PolicyEnforcementService;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationContext;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.graphql.server.WebGraphQlInterceptor;
import org.springframework.graphql.server.WebGraphQlRequest;
import org.springframework.graphql.server.WebGraphQlResponse;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Controller;
import reactor.core.publisher.Mono;

@Slf4j
public class KeycloakAuthorizationInterceptor implements WebGraphQlInterceptor {
    private final PolicyEnforcementService policyEnforcementService;
    private final Map<String, ProtectedOperation> operationPermissions;

    public KeycloakAuthorizationInterceptor(PolicyEnforcementService policyEnforcementService,
                                            ApplicationContext applicationContext) {
        this.policyEnforcementService = policyEnforcementService;
        this.operationPermissions = scanControllerAnnotations(applicationContext);
        log.info("keycloak authorization interceptor initialized with {} protected operations",
                operationPermissions.size());
    }

    @Override
    public Mono<WebGraphQlResponse> intercept(WebGraphQlRequest request, Chain chain) {
        String operationName = extractOperationName(request);
        if (operationName == null) {
            return chain.next(request);
        }

        ProtectedOperation permission = operationPermissions.get(operationName);
        if (permission == null) {
            return chain.next(request);
        }

        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (!(principal instanceof Jwt jwt)) {
            throw new AccessDeniedException("invalid principal for authorization check");
        }

        String accessToken = jwt.getTokenValue();
        String resource = resolveResource(permission, request.getVariables());
        boolean authorized = policyEnforcementService.isAuthorized(
                accessToken, resource, permission.scope());
        if (!authorized) {
            throw new AccessDeniedException(
                    "not authorized for " + resource + "#" + permission.scope());
        }

        log.debug("authorization granted for operation '{}' ({}#{})",
                operationName, resource, permission.scope());
        return chain.next(request);
    }

    private String extractOperationName(WebGraphQlRequest request) {
        String document = request.getDocument();
        int braceIndex = document.indexOf('{', document.indexOf('{') + 1);
        if (braceIndex == -1) braceIndex = document.indexOf('{');
        if (braceIndex == -1) return null;

        String afterBrace = document.substring(braceIndex + 1).trim();
        int endIndex = afterBrace.indexOf('(');
        if (endIndex == -1) endIndex = afterBrace.indexOf('{');
        if (endIndex == -1) endIndex = afterBrace.indexOf(' ');
        if (endIndex == -1) return null;

        return afterBrace.substring(0, endIndex).trim();
    }

    private String resolveResource(ProtectedOperation permission, Map<String, Object> variables) {
        String resourceIdParam = permission.resourceIdParam();
        if (resourceIdParam.isEmpty()) {
            return permission.resource();
        }
        String resourceId = resolveVariable(variables, resourceIdParam);
        if (resourceId == null) {
            log.warn("could not resolve resourceIdParam '{}' from variables, falling back to type-level check",
                    resourceIdParam);
            return permission.resource();
        }
        return permission.resource() + ":" + resourceId;
    }

    static String resolveVariable(Map<String, Object> variables, String path) {
        String[] parts = path.split("\\.");
        Object current = variables;
        for (String part : parts) {
            if (current instanceof Map<?, ?> map) {
                current = map.get(part);
            } else {
                return null;
            }
        }
        return current != null ? current.toString() : null;
    }

    private Map<String, ProtectedOperation> scanControllerAnnotations(ApplicationContext context) {
        Map<String, ProtectedOperation> permissions = new HashMap<>();
        Map<String, Object> controllers = context.getBeansWithAnnotation(Controller.class);

        for (Object controller : controllers.values()) {
            Class<?> clazz = controller.getClass();
            if (clazz.getName().contains("$$")) {
                clazz = clazz.getSuperclass();
            }
            for (Method method : clazz.getDeclaredMethods()) {
                ProtectedOperation annotation = method.getAnnotation(ProtectedOperation.class);
                if (annotation == null) continue;

                String operationName = null;
                MutationMapping mutation = method.getAnnotation(MutationMapping.class);
                if (mutation != null) {
                    operationName = mutation.name().isEmpty() ? mutation.value() : mutation.name();
                }
                QueryMapping query = method.getAnnotation(QueryMapping.class);
                if (query != null) {
                    operationName = query.name().isEmpty() ? query.value() : query.name();
                }

                if (operationName != null && !operationName.isEmpty()) {
                    permissions.put(operationName, annotation);
                    log.debug("registered protected operation: {} → {}#{}",
                            operationName, annotation.resource(), annotation.scope());
                }
            }
        }
        return permissions;
    }
}
