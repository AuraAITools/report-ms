package com.reportai.www.reportapi.dtos.auth;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;

@Data
@Builder
@AllArgsConstructor
@RequiredArgsConstructor
@Slf4j
public class KeycloakUserPrincipal {

    @NotEmpty
    @Email
    private String email;

    private List<String> tenantIds;

    @NotNull
    private boolean emailVerified;

    @NotEmpty
    private String userId;

    @NotEmpty
    private String name;

    @NotEmpty
    private String givenName;

    @NotEmpty
    private String familyName;

    private List<SimpleGrantedAuthority> authorities;

    public static KeycloakUserPrincipal from(Jwt jwt, String clientName) {

        Map<String, Object> extendedAttributes = jwt.getClaimAsMap("ext_attrs");

        List<String> tenants = Optional.ofNullable(extendedAttributes)
                .map(attrs -> {
                    Object tenantIds = attrs.get("tenant_ids");
                    if (tenantIds != null && !(tenantIds instanceof List<?>)) {
                        log.warn("tenant_ids is not a List: {}", tenantIds.getClass());
                        throw new RuntimeException("incorrect token format");
                    }
                    return tenantIds;
                })
                .map(object -> (List<?>) object)
                .map(list -> list
                        .stream()
                        .filter(item -> item instanceof String)
                        .map(String.class::cast)
                        .toList())
                .orElse(Collections.emptyList());

        Map<String, Object> resourceAccess = jwt.getClaimAsMap("resource_access");
        Map<String, Object> clientAccess = (Map<String, Object>) resourceAccess.get(clientName);
        List<String> roles = (List<String>) clientAccess.getOrDefault("roles", Collections.emptyList());
        List<SimpleGrantedAuthority> grantedAuthorities = roles.stream().map(role -> new SimpleGrantedAuthority("ROLE_" + role)).toList();
        KeycloakUserPrincipal keycloakUserPrincipal = KeycloakUserPrincipal
                .builder()
                .userId(jwt.getClaimAsString("sub"))
                .name(jwt.getClaimAsString("name"))
                .email(jwt.getClaimAsString("email"))
                .emailVerified(jwt.getClaimAsBoolean("email_verified"))
                .givenName(jwt.getClaimAsString("given_name"))
                .familyName(jwt.getClaimAsString("family_name"))
                .authorities(grantedAuthorities)
                .tenantIds(tenants)
                .build();
        return keycloakUserPrincipal;

    }
}
