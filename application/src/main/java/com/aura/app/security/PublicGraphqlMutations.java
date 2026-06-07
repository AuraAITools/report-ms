package com.aura.app.security;

import java.util.Set;

/**
 * Single source of truth for GraphQL mutations that do not require an authenticated
 * principal. Consumed by both {@link PublicGraphqlMutationRequestMatcher} (Spring
 * Security filter chain) and {@code UserRequestContextInterceptor} (GraphQL interceptor
 * that otherwise demands a JWT principal).
 *
 * <p>Operations listed here MUST NOT carry {@code @PreAuthorize} on their controller
 * method; see {@code PreAuthorizeConventionTest}'s exemption list for the matching
 * controller class.
 */
public final class PublicGraphqlMutations {

    public static final Set<String> NAMES = Set.of(
            "requestInstitutionCreation"
    );

    private PublicGraphqlMutations() {
    }
}
