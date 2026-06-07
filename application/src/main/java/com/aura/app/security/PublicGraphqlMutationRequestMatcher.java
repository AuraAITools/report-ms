package com.aura.app.security;

import jakarta.servlet.http.HttpServletRequest;
import java.nio.charset.StandardCharsets;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.web.util.matcher.RequestMatcher;

/**
 * Permits unauthenticated POST /graphql calls when the body invokes one of the
 * mutations listed in {@link PublicGraphqlMutations}. Mutation routing in GraphQL is
 * by operation name, so we permit at the body level rather than per-path.
 */
@Slf4j
public class PublicGraphqlMutationRequestMatcher implements RequestMatcher {

    private static final String GRAPHQL_PATH = "/graphql";

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
        return PublicGraphqlMutations.NAMES.stream().anyMatch(body::contains);
    }
}
