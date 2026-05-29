package com.aura.app.security;

import com.aura.core.clients.RelationshipAuthorizationService;
import java.io.Serializable;
import java.util.Collection;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jspecify.annotations.NonNull;
import org.springframework.security.access.PermissionEvaluator;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.jwt.Jwt;

@Slf4j
@RequiredArgsConstructor
public class SpiceDbPermissionEvaluator implements PermissionEvaluator {

    private final RelationshipAuthorizationService relationshipService;

    @Override
    public boolean hasPermission(@NonNull Authentication authentication, @NonNull Object targetDomainObject,
                                 @NonNull Object permission) {
        log.warn("unsupported hasPermission(Object, Object) called");
        return false;
    }

    @Override
    public boolean hasPermission(Authentication authentication, @NonNull Serializable resourceId,
                                 @NonNull String resourceType, @NonNull Object permission) {
        if (!(authentication.getPrincipal() instanceof Jwt jwt)) {
            return false;
        }

        String userId = jwt.getSubject();
        String permissionString = (String) permission;

        if (resourceId instanceof Collection<?> ids) {
            for (Object id : ids) {
                boolean granted = relationshipService.checkPermission(resourceType, id.toString(), permissionString, "user", userId);
                log.info("SpiceDB permission check: {}:{}#{} for user {} = {}",
                        resourceType, id, permissionString, userId, granted);
                if (!granted) {
                    return false;
                }
            }
            return true;
        }

        boolean granted = relationshipService.checkPermission(resourceType, resourceId.toString(), permissionString, "user", userId);
        log.info("SpiceDB permission check: {}:{}#{} for user {} = {}",
                resourceType, resourceId, permissionString, userId, granted);
        return granted;
    }
}
