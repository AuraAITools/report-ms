package com.reportai.www.reportapi.config.converters;

import com.reportai.www.reportapi.dtos.auth.KeycloakUserPrincipal;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.AbstractOAuth2TokenAuthenticationToken;

/**
 * custom auth token used by aura
 */
public class AuraAuthenticationToken extends AbstractOAuth2TokenAuthenticationToken<Jwt> {

    private static final String CLIENT_NAME = "aura-application-client";

    public AuraAuthenticationToken(KeycloakUserPrincipal keycloakUserPrincipal, Jwt jwt) {
        super(jwt, keycloakUserPrincipal, jwt, getRolesFromClaim(jwt));
        setAuthenticated(true);
    }

    private static Collection<GrantedAuthority> getRolesFromClaim(Jwt jwt) {
        Map<String, Object> resourceAccess = jwt.getClaimAsMap("resource_access");
        if (resourceAccess == null) {
            return Collections.emptyList();
        }


        Map<String, Object> clientAccess = (Map<String, Object>) resourceAccess.get(CLIENT_NAME);
        if (clientAccess == null) {
            return Collections.emptyList();
        }

        List<String> roles = (List<String>) clientAccess.get("roles");
        if (roles == null) {
            return Collections.emptyList();
        }

        return roles.stream()
                .map(role -> new SimpleGrantedAuthority("ROLE_" + role))
                .collect(Collectors.toList());
    }

    // TODO: implement?
    @Override
    public Map<String, Object> getTokenAttributes() {
        return Map.of();
    }

}
