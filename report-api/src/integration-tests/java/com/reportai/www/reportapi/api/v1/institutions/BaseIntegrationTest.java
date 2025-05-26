package com.reportai.www.reportapi.api.v1.institutions;

import com.reportai.www.reportapi.config.converters.AuraAuthenticationToken;
import com.reportai.www.reportapi.contexts.requests.TenantContext;
import com.reportai.www.reportapi.dtos.auth.KeycloakUserPrincipal;
import com.reportai.www.reportapi.filters.TenantFilter;
import com.reportai.www.reportapi.repositories.OutletRepository;
import java.time.Instant;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.keycloak.admin.client.resource.ClientResource;
import org.keycloak.admin.client.resource.RealmResource;
import org.keycloak.admin.client.resource.RolesResource;
import org.keycloak.admin.client.resource.UsersResource;
import org.keycloak.representations.idm.ClientRepresentation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.RequestPostProcessor;
import org.springframework.web.context.WebApplicationContext;

@SpringBootTest
@AutoConfigureMockMvc
@Slf4j
@ActiveProfiles("integration-test")
public class BaseIntegrationTest {
    protected MockMvc mockMvc;

    @MockitoBean
    protected RealmResource realmResource;

    @MockitoBean
    protected ClientResource clientResource;

    @MockitoBean
    protected ClientRepresentation clientRepresentation;

    @MockitoBean
    protected UsersResource usersResource;

    @MockitoBean
    protected RolesResource rolesResource;

    @Autowired
    protected OutletRepository outletRepository;

    @Autowired
    protected WebApplicationContext webApplicationContext;

    @Autowired
    protected TenantFilter tenantFilter;

    @Autowired
    protected JdbcTemplate jdbcTemplate;

    protected static RequestPostProcessor tenant1InstitutionAdminJwt(OutletRepository outletRepository) {
        return tenantAccountJwt("aura-application-client", outletRepository, List.of("00000000-0000-0000-0000-000000000000"), "00000000-0000-0000-0000-000000000000_institution-admin");
    }

    protected static RequestPostProcessor auraAdminJwt(OutletRepository outletRepository) {
        return serviceAccountKeycloakJwt("aura-application-client", outletRepository, "aura-admin");
    }

    protected static RequestPostProcessor serviceAccountKeycloakJwt(String clientName, OutletRepository outletRepository, String... roles) {
        return request -> {
            Jwt jwt = createServiceAccountJwt(clientName, roles);
            KeycloakUserPrincipal principal = KeycloakUserPrincipal.from(jwt, clientName);

            AuraAuthenticationToken authentication = new AuraAuthenticationToken(principal, jwt, outletRepository);

            // Set the authentication in the security context AND the request
            SecurityContextHolder.getContext().setAuthentication(authentication);
            return request;
        };
    }

    protected static RequestPostProcessor tenantAccountJwt(String clientName, OutletRepository outletRepository, List<String> tenantIds, String... roles) {
        return request -> {
            Jwt jwt = createTenantAccountJwt(clientName, tenantIds, roles);
            KeycloakUserPrincipal principal = KeycloakUserPrincipal.from(jwt, clientName);

            AuraAuthenticationToken authentication = new AuraAuthenticationToken(principal, jwt, outletRepository);

            String tenantId = TenantContext.getTenantId();
            // Set the authentication in the security context AND the request
            SecurityContextHolder.getContext().setAuthentication(authentication);
            return request;
        };
    }

    protected static Jwt createServiceAccountJwt(String clientName, String... roles) {
        Map<String, Object> headers = new HashMap<>();
        headers.put("alg", "RS256");
        headers.put("typ", "JWT");

        // Create resource_access structure expected by your principal
        Map<String, Object> clientAccess = new HashMap<>();
        clientAccess.put("roles", Arrays.asList(roles));

        Map<String, Object> resourceAccess = new HashMap<>();
        resourceAccess.put(clientName, clientAccess);

        // Build the JWT with all required claims
        return Jwt.withTokenValue("test-token")
                .headers(h -> h.putAll(headers))
                .claim("sub", "test-user-id")
                .claim("name", "aura-admin-user")
                .claim("email", "test@example.com")
                .claim("email_verified", true)
                .claim("given_name", "Test")
                .claim("family_name", "User")
                .claim("resource_access", resourceAccess)
                .issuedAt(Instant.now())
                .expiresAt(Instant.now().plusSeconds(3600))
                .build();
    }

    protected static Jwt createTenantAccountJwt(String clientName, List<String> tenantIds, String... roles) {
        Map<String, Object> headers = new HashMap<>();
        headers.put("alg", "RS256");
        headers.put("typ", "JWT");

        // Create resource_access structure expected by your principal
        Map<String, Object> clientAccess = new HashMap<>();
        clientAccess.put("roles", Arrays.asList(roles));

        Map<String, Object> resourceAccess = new HashMap<>();
        resourceAccess.put(clientName, clientAccess);

        // Build the JWT with all required claims
        return Jwt.withTokenValue("test-token")
                .headers(h -> h.putAll(headers))
                .claim("sub", "test-user-id")
                .claim("ext_attrs", Map.of("tenant_ids", tenantIds))
                .claim("name", "aura-admin-user")
                .claim("email", "test@example.com")
                .claim("email_verified", true)
                .claim("given_name", "Test")
                .claim("family_name", "User")
                .claim("resource_access", resourceAccess)
                .issuedAt(Instant.now())
                .expiresAt(Instant.now().plusSeconds(3600))
                .build();
    }
}
