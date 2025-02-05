package com.reportai.www.reportapi.config.converters;

import com.reportai.www.reportapi.dtos.auth.KeycloakUserPrincipal;
import com.reportai.www.reportapi.entities.Institution;
import com.reportai.www.reportapi.entities.Outlet;
import com.reportai.www.reportapi.exceptions.lib.ResourceNotFoundException;
import com.reportai.www.reportapi.repositories.InstitutionRepository;
import jakarta.transaction.Transactional;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import org.springframework.security.access.hierarchicalroles.RoleHierarchyImpl;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.AbstractOAuth2TokenAuthenticationToken;


import static java.util.Collections.emptyList;

/**
 * Custom OAuth2Token class that does the following
 * This class is aware of the Aura's custom access token's shape
 * - extract the client roles from the token
 * - implicitly expand all tenant aware roles
 * i.e. institution admin of tenant 1 should be outlet-admin for all tenant 1's outlets
 * - map these roles to resource permissions
 * i.e. outlet-admin of outlet 1 should be able to edit outlet details of outlet 1 but not outlet 2
 * The resource permissions follow this structure
 * i.e. resource1::{id}:resource2::resource3:action-allowed-on-resource-3
 * NOTE: the prefix for an action is a ':' instead of a '::'
 * Also id is not mandatory if allowing an action to be performed on ALL resources
 */
public class AuraAuthenticationToken extends AbstractOAuth2TokenAuthenticationToken<Jwt> {

    private static final String CLIENT_NAME = "aura-application-client";

    private InstitutionRepository institutionRepository;


    public AuraAuthenticationToken(KeycloakUserPrincipal keycloakUserPrincipal, Jwt jwt, InstitutionRepository institutionRepository) {
        super(jwt, keycloakUserPrincipal, jwt, getAuthoritiesFromClaim(jwt, institutionRepository));
        setAuthenticated(true);
    }

    @Transactional
    private static List<? extends GrantedAuthority> getAuthoritiesFromClaim(Jwt jwt, InstitutionRepository institutionRepository) {
        Map<String, Object> resourceAccess = jwt.getClaimAsMap("resource_access");
        if (resourceAccess == null) {
            return emptyList();
        }

        Object clientAccessObject = resourceAccess.getOrDefault(CLIENT_NAME, null);

        // if application client field is not a map, means somehow wrong format in auth token, return empty list
        if (!(clientAccessObject instanceof Map<?, ?>)) {
            return emptyList();
        }

        // the client access object is assumed to have this structure
        // {
        //      "roles": [...roleStrings]
        // }
        @SuppressWarnings("unchecked")
        Map<String, List<String>> clientAccess = (Map<String, List<String>>) clientAccessObject;

        List<String> roles = clientAccess.get("roles");

        if (roles == null || roles.isEmpty()) {
            return emptyList();
        }

        Collection<SimpleGrantedAuthority> initialRoles = roles
                .stream()
                .map(role -> new SimpleGrantedAuthority("ROLE_" + role))
                .collect(Collectors.toList());

        // INFO: reason for not using default prefix ROLE_ is because we want to convert from ROLE_ to resurce permissions too which cannot have ROLE_
        RoleHierarchyImpl.Builder roleHierarchyBuilder = RoleHierarchyImpl.withRolePrefix("");

        Map<String, Object> extendedAttributes = jwt.getClaimAsMap("ext_attrs");

        Set<String> serviceAccountRoles = new HashSet<>();
        serviceAccountRoles.add("aura-admin");
        // service accounts do not have tenant-ids, so just return before processing tenant ids
        serviceAccountRoles.retainAll(roles);
        if (!serviceAccountRoles.isEmpty() && extendedAttributes == null) {
            return serviceAccountRoles.stream().map(sa -> new SimpleGrantedAuthority("ROLE_" + sa)).toList();
        }
        Object tenantIdsListObject = extendedAttributes.getOrDefault("tenant_ids", emptyList());

        if (!(tenantIdsListObject instanceof List<?>)) {
            return emptyList();
        }

        // allow unchecked casting of tenantIds list, it is expected that the list contains string type
        @SuppressWarnings("unchecked")
        List<String> tenantIds = (List<String>) tenantIdsListObject;

        // expand roles to all implied roles, an institution-admin should is implicitly outlet-admin for all his outlets
        tenantIds.forEach(tenantId -> {
            Institution institution = institutionRepository
                    .findById(UUID.fromString(tenantId))
                    .orElseThrow(() -> new ResourceNotFoundException("institution in token absent"));

            List<Outlet> outlets = institution.getOutlets();

            // if outlets exist imply outlet roles
            if (!outlets.isEmpty()) {
                List<String> impliedOutletAdminRoles = outlets
                        .stream()
                        .map(outlet -> String.format("ROLE_%s_%s_outlet-admin", tenantId, outlet.getId()))
                        .toList();

                roleHierarchyBuilder
                        .role(String.format("ROLE_%s_institution-admin", tenantId))
                        .implies(impliedOutletAdminRoles.toArray(String[]::new));

                // give outlet resource permissions for all implied roles
                outlets.forEach(o -> roleHierarchyBuilder
                        .role(String.format("ROLE_%s_%s_outlet-admin", tenantId, o.getId()))
                        .implies(outletAdminRoleResourcePermission(tenantId, o.getId().toString())));
            }
            // give implied institution permissions
            roleHierarchyBuilder
                    .role(String.format("ROLE_%s_institution-admin", tenantId))
                    .implies(institutionAdminRoleResourcePermissions(tenantId));

        });

        return roleHierarchyBuilder
                .build()
                .getReachableGrantedAuthorities(initialRoles)
                .stream()
                .toList();
    }

    /**
     * List of resources accessible to a tenant aware institution admin roles
     * The convention is resource1::{id}::resource2:{allowed-action}
     *
     * @param tenantId
     * @return
     */
    private static String[] institutionAdminRoleResourcePermissions(String tenantId) {
        return List.of(
                String.format("institutions::%s:update", tenantId),
                String.format("institutions::%s::outlets:read", tenantId),
                String.format("institutions::%s::outlets:create", tenantId),
                String.format("institutions::%s::accounts:link-educator", tenantId),
                String.format("institutions::%s::accounts:link-student", tenantId),
                String.format("institutions::%s::accounts:create", tenantId),
                String.format("institutions::%s::accounts:create-link-outlet-admin", tenantId),
                String.format("institutions::%s::levels:create", tenantId),
                String.format("institutions::%s::levels:read", tenantId),
                String.format("institutions::%s::subjects:read", tenantId),
                String.format("institutions::%s::subjects:create", tenantId)
        ).toArray(String[]::new);
    }

    /**
     * List of resources accessible to a tenant aware outlet admin roles
     * The convention is resource1::{id}::resource2:{allowed-action}
     *
     * @param tenantId
     * @return
     */
    private static String[] outletAdminRoleResourcePermission(String tenantId, String outletId) {
        return List.of(
                String.format("institutions::%s:read", tenantId),
                String.format("institutions::%s::outlets::%s:create", tenantId, outletId),
                String.format("institutions::%s::outlets::%s:read", tenantId, outletId),
                String.format("institutions::%s::outlets:read", tenantId),
                String.format("institutions::%s::outlets::%s:read", tenantId, outletId),
                String.format("institutions::%s::outlets::%s:update", tenantId, outletId),
                String.format("institutions::%s::outlets::%s::courses:create", tenantId, outletId),
                String.format("institutions::%s::outlets::%s::courses:read", tenantId, outletId),
                String.format("institutions::%s::outlets::%s::educators:read", tenantId, outletId),
                String.format("institutions::%s::outlets::%s::students:read", tenantId, outletId),
                String.format("institutions::%s::outlets::%s:add-student", tenantId, outletId),
                String.format("institutions::%s::levels:read", tenantId),
                String.format("institutions::%s::subjects:read", tenantId),
                String.format("institutions::%s::accounts:link-client", tenantId)
        ).toArray(String[]::new);
    }

    // TODO: implement for convenience
    @Override
    public Map<String, Object> getTokenAttributes() {
        return Map.of();
    }

}
