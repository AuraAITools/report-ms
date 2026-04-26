package com.aura.app.security;

import com.aura.core.clients.RelationshipAuthorizationService;
import java.io.Serializable;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.PermissionEvaluator;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.jwt.Jwt;

@Slf4j
@RequiredArgsConstructor
public class SpiceDbPermissionEvaluator implements PermissionEvaluator {

    private final RelationshipAuthorizationService relationshipService;

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

        String userId = jwt.getSubject();
        String resourceId = targetId != null ? targetId.toString() : "";
        String scope = (String) permission;

        boolean granted = relationshipService.checkPermission(
            targetType, resourceId, scope, "user", userId);
        log.debug("SpiceDB permission check: {}:{}#{} for user {} = {}",
                  targetType, resourceId, scope, userId, granted);
        return granted;
    }
}
