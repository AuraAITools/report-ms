package com.aura.app.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.springframework.web.filter.OncePerRequestFilter;

public class GraphqlBodyCachingFilter extends OncePerRequestFilter {

    public static final String CACHED_BODY_ATTR = "com.aura.app.security.GRAPHQL_CACHED_BODY";
    private static final String GRAPHQL_PATH = "/graphql";

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {
        if ("POST".equalsIgnoreCase(request.getMethod()) && GRAPHQL_PATH.equals(request.getRequestURI())) {
            CachedBodyHttpServletRequest wrapped = new CachedBodyHttpServletRequest(request);
            wrapped.setAttribute(CACHED_BODY_ATTR, wrapped.getCachedBody());
            chain.doFilter(wrapped, response);
        } else {
            chain.doFilter(request, response);
        }
    }
}
