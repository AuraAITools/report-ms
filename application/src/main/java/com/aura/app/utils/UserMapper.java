package com.aura.app.utils;

import com.aura.core.auth.User;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.jwt.Jwt;

import static java.util.Collections.emptyList;
import static java.util.Collections.emptySet;
import static java.util.Optional.ofNullable;

@Slf4j
public class UserMapper {

    public static User from(Jwt jwt) {

        Map<String, Object> extendedAttributes = jwt.getClaimAsMap("ext_attrs");

        Set<String> tenants = ofNullable(extendedAttributes)
                .map(UserMapper::extractTenantIdsFromExtAttrs)
                .orElse(emptySet());

        List<String> groups = ofNullable(jwt.getClaimAsStringList("groups")).orElse(emptyList());

        return User
                .builder()
                .userId(jwt.getClaimAsString("sub"))
                .name(jwt.getClaimAsString("name"))
                .email(jwt.getClaimAsString("email"))
                .emailVerified(jwt.getClaimAsBoolean("email_verified"))
                .givenName(jwt.getClaimAsString("given_name"))
                .familyName(jwt.getClaimAsString("family_name"))
                .groups(groups)
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
