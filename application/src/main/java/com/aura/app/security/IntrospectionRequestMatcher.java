package com.aura.app.security;

import jakarta.servlet.http.HttpServletRequest;
import java.nio.charset.StandardCharsets;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.web.util.matcher.RequestMatcher;

@Slf4j
public class IntrospectionRequestMatcher implements RequestMatcher {

    private static final String GRAPHQL_PATH = "/graphql";

    @Override
    public boolean matches(HttpServletRequest request) {
        if (!"POST".equalsIgnoreCase(request.getMethod()) || !GRAPHQL_PATH.equals(request.getRequestURI())) {
            return false;
        }
        Object cachedBody = request.getAttribute(GraphqlBodyCachingFilter.CACHED_BODY_ATTR);
        if (!(cachedBody instanceof byte[] bytes)) {
            log.debug("introspection matcher: cached body attribute missing on /graphql request");
            return false;
        }
        String body = new String(bytes, StandardCharsets.UTF_8);
        boolean isIntrospection = body.contains("__schema") || body.contains("__type");
        log.debug("introspection matcher: isIntrospection={}", isIntrospection);
        return isIntrospection;
    }
}
