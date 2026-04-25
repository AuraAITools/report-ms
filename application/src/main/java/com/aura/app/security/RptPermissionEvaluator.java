package com.aura.app.security;

import java.io.Serializable;
import java.util.List;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.PermissionEvaluator;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.jwt.Jwt;

@Slf4j
public class RptPermissionEvaluator implements PermissionEvaluator {

    @Override
    public boolean hasPermission(Authentication authentication, Object targetDomainObject,
                                 Object permission) {
        log.warn("unsupported hasPermission(Object, Object) called");
        return false;
    }

    @Override
    public boolean hasPermission(Authentication authentication, Serializable targetId,
                                 String targetType, Object permission) {
        if (!(authentication.getPrincipal() instanceof Jwt jwt)) {
            return false;
        }

        Map<String, Object> authorization = jwt.getClaim("authorization");
        String resource = targetId != null
                ? targetType + ":" + targetId
                : targetType;
        String scope = (String) permission;

        boolean granted = hasRptPermission(authorization, resource, scope);
        log.debug("permission check: {}#{} = {}", resource, scope, granted);
        return granted;
    }

    @SuppressWarnings("unchecked")
    private boolean hasRptPermission(Map<String, Object> authorization,
                                     String resource, String scope) {
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
}
