package com.aura.app.utils;

import com.aura.core.auth.User;
import com.aura.core.entities.accounts.TenantAwareRole;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.jwt.Jwt;


import static java.util.Collections.emptyList;
import static java.util.Collections.emptySet;
import static java.util.Optional.ofNullable;

// TODO: refactor some day
@Slf4j
public class UserMapper {

    public static User from(Jwt jwt) {

        Map<String, Object> extendedAttributes = jwt.getClaimAsMap("ext_attrs");

        Set<String> tenants = ofNullable(extendedAttributes)
                .map(UserMapper::extractTenantIdsFromExtAttrs)
                .orElse(emptySet());
        String clientId = ofNullable(jwt.getClaimAsString("client_id")).orElseThrow(() -> new RuntimeException("missing client id"));
        Map<String, Object> resourceAccess = ofNullable(jwt.getClaimAsMap("resource_access")).orElseThrow(() -> new RuntimeException("missing resource_access"));
        Map<String, Object> clientAccess = (Map<String, Object>) resourceAccess.get(clientId);
        List<String> roles = (List<String>) clientAccess.getOrDefault("roles", emptyList());
        Set<TenantAwareRole> tenantAwareRoles = roles
                .stream()
                .map(role -> {
                    String[] roleArray = role.split("_");
                    // outlet role
                    if (roleArray.length == 3) {
                        return TenantAwareRole
                                .builder()
                                .tenantId(roleArray[0])
                                .roleType(TenantAwareRole.ROLE_TYPE.OUTLET_LEVEL)
                                .institutionId(roleArray[0])
                                .outletId(roleArray[1])
                                .name(roleArray[2])
                                .build();
                    } else if (roleArray.length == 2) {
                        // institution role
                        return TenantAwareRole
                                .builder()
                                .tenantId(roleArray[0])
                                .roleType(TenantAwareRole.ROLE_TYPE.INSTITUTION_LEVEL)
                                .institutionId(roleArray[0])
                                .outletId(null)
                                .name(roleArray[1])
                                .build();
                    } else if (roleArray.length == 1) {
                        return TenantAwareRole
                                .builder()
                                .tenantId(null)
                                .roleType(TenantAwareRole.ROLE_TYPE.TECHNICAL_USER)
                                .institutionId(null)
                                .outletId(null)
                                .name(roleArray[0])
                                .build();
                    } else {
                        log.info("invalid token");
                        throw new RuntimeException("invalid token");
                    }
                }).collect(Collectors.toSet());
        return User
                .builder()
                .userId(jwt.getClaimAsString("sub"))
                .name(jwt.getClaimAsString("name"))
                .email(jwt.getClaimAsString("email"))
                .emailVerified(jwt.getClaimAsBoolean("email_verified"))
                .givenName(jwt.getClaimAsString("given_name"))
                .familyName(jwt.getClaimAsString("family_name"))
                .tenantRoles(tenantAwareRoles)
                .tenantIds(tenants)
                .build();
    }

    private static Set<String> extractTenantIdsFromExtAttrs(Map<String, Object> attrs) {
        Object tenantIds = ofNullable(attrs.get("tenant_ids")).orElseThrow(() -> new RuntimeException("incorrect token format"));
        if (!(tenantIds instanceof List<?> tenantIdsList)) {
            log.warn("tenant_ids is not a List: {}", tenantIds.getClass());
            throw new RuntimeException("incorrect token format");
        }

        return tenantIdsList.stream()
                .filter(item -> item instanceof String)
                .map(String.class::cast)
                .collect(Collectors.toSet());
    }
}
