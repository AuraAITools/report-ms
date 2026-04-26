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
        String scope = (String) permission;

        // Type-level checks (null targetId) are routed to the system:global resource.
        // This grants access to system admins for create/manage operations on any resource type.
        // Instance-level checks use the actual resource type and ID.
        String effectiveResourceType = targetId != null ? targetType : "system";
        String effectiveResourceId = targetId != null ? targetId.toString() : "global";

        boolean granted = relationshipService.checkPermission(
            effectiveResourceType, effectiveResourceId, scope, "user", userId);
        log.debug("SpiceDB permission check: {}:{}#{} for user {} = {}",
                  effectiveResourceType, effectiveResourceId, scope, userId, granted);
        return granted;
    }
}
