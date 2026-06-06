package com.aura.app.security;

import jakarta.servlet.http.HttpServletRequest;
import java.nio.charset.StandardCharsets;
import java.util.Set;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.web.util.matcher.RequestMatcher;

/**
 * Permits unauthenticated POST /graphql calls when the body invokes one of the
 * mutations listed in {@link #PUBLIC_MUTATIONS}. Mutation routing in GraphQL is
 * by operation name, so we permit at the body level rather than per-path.
 */
@Slf4j
public class PublicGraphqlMutationRequestMatcher implements RequestMatcher {

    private static final String GRAPHQL_PATH = "/graphql";

    /**
     * Operation names in the GraphQL document. Endpoints listed here MUST NOT
     * carry an {@code @PreAuthorize}; see {@code PreAuthorizeConventionTest}'s
     * exemption list for the matching controller class.
     */
    private static final Set<String> PUBLIC_MUTATIONS = Set.of(
            "requestInstitutionCreation"
    );

    @Override
    public boolean matches(HttpServletRequest request) {
        if (!"POST".equalsIgnoreCase(request.getMethod()) || !GRAPHQL_PATH.equals(request.getRequestURI())) {
            return false;
        }
        Object cachedBody = request.getAttribute(GraphqlBodyCachingFilter.CACHED_BODY_ATTR);
        if (!(cachedBody instanceof byte[] bytes)) {
            log.debug("public mutation matcher: cached body attribute missing on /graphql request");
            return false;
        }
        String body = new String(bytes, StandardCharsets.UTF_8);
        return PUBLIC_MUTATIONS.stream().anyMatch(body::contains);
    }
}
