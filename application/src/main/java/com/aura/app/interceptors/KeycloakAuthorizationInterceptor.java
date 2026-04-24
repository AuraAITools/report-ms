package com.aura.app.interceptors;

import com.aura.app.annotations.ProtectedOperation;
import com.aura.app.exceptions.AccessDeniedBusinessException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
    private final Map<String, ProtectedOperation> operationPermissions;

    public KeycloakAuthorizationInterceptor(ApplicationContext applicationContext) {
        this.operationPermissions = scanControllerAnnotations(applicationContext);
        log.info("keycloak authorization interceptor initialized with {} protected operations",
                operationPermissions.size());
    }

    @Override
    public Mono<WebGraphQlResponse> intercept(WebGraphQlRequest request, Chain chain) {
        List<String> operationNames = extractOperationNames(request.getDocument());
        if (operationNames.isEmpty()) {
            return chain.next(request);
        }

        Jwt jwt = null;
        Map<String, Object> authorization = null;

        for (String operationName : operationNames) {
            ProtectedOperation permission = operationPermissions.get(operationName);
            if (permission == null) {
                continue;
            }

            if (jwt == null) {
                Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
                if (!(principal instanceof Jwt j)) {
                    throw new AccessDeniedBusinessException("invalid principal for authorization check");
                }
                jwt = j;
                authorization = jwt.getClaim("authorization");
            }

            String resource = resolveResource(permission, request.getVariables());
            boolean authorized = hasPermission(authorization, resource, permission.scope());
            if (!authorized) {
                throw new AccessDeniedBusinessException(
                        "not authorized for " + resource + "#" + permission.scope());
            }

            log.debug("authorization granted for operation '{}' ({}#{})",
                    operationName, resource, permission.scope());
        }

        return chain.next(request);
    }

    @SuppressWarnings("unchecked")
    static boolean hasPermission(Map<String, Object> authorization, String resource, String scope) {
        if (authorization == null) {
            return false;
        }
        List<Map<String, Object>> permissions =
                (List<Map<String, Object>>) authorization.get("permissions");
        if (permissions == null) {
            return false;
        }
        return permissions.stream().anyMatch(perm -> {
            String rsname = (String) perm.get("rsname");
            List<String> scopes = (List<String>) perm.get("scopes");
            return resource.equals(rsname) && scopes != null && scopes.contains(scope);
        });
    }

    private static final Pattern ROOT_FIELD_PATTERN = Pattern.compile(
            "(?:query|mutation)\\s*(?:\\w+)?\\s*\\{(.+)\\}",
            Pattern.DOTALL);

    static List<String> extractOperationNames(String document) {
        Matcher rootMatcher = ROOT_FIELD_PATTERN.matcher(document.trim());
        if (!rootMatcher.find()) {
            return List.of();
        }

        String body = rootMatcher.group(1);
        List<String> names = new ArrayList<>();
        int depth = 0;
        int parenDepth = 0;
        int i = 0;

        while (i < body.length()) {
            char c = body.charAt(i);
            if (c == '(') {
                parenDepth++;
            } else if (c == ')') {
                parenDepth--;
            } else if (c == '{') {
                depth++;
            } else if (c == '}') {
                depth--;
            } else if (depth == 0 && parenDepth == 0 && Character.isLetter(c)) {
                int start = i;
                while (i < body.length() && (Character.isLetterOrDigit(body.charAt(i)) || body.charAt(i) == '_')) {
                    i++;
                }
                names.add(body.substring(start, i));
                continue;
            }
            i++;
        }
        return names;
    }

    private String resolveResource(ProtectedOperation permission, Map<String, Object> variables) {
        String resourceIdParam = permission.resourceIdParam();
        if (resourceIdParam.isEmpty()) {
            return permission.resource();
        }

        String resourceId = resolveVariable(variables, resourceIdParam);

        if (resourceId == null) {
            log.warn("could not resolve resourceIdParam '{}', falling back to type-level check",
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
