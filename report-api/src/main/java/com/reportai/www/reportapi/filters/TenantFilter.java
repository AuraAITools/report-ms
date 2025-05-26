package com.reportai.www.reportapi.filters;

import com.reportai.www.reportapi.contexts.requests.TenantContext;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

/**
 * This filter checks the URL resource the user is trying to access and extracts the target tenant by the institution_id
 * The tenant_id is then stored in a TenantContext which is ThreadLocal (request-scoped)
 */
@Slf4j
@Component
public class TenantFilter extends OncePerRequestFilter {
    private static final Pattern TENANT_RESOURCES_PATTERN = Pattern.compile("/api/v1/institutions/([^/]+)");
    private static final Pattern INSTITUTION_CREATION_RESOURCE = Pattern.compile("/api/v1/institutions");

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {
            String path = request.getRequestURI();
            Matcher tenantResourceMatcher = TENANT_RESOURCES_PATTERN.matcher(path);
            Matcher institutionCreationResourceMatcher = INSTITUTION_CREATION_RESOURCE.matcher(path);

            if (tenantResourceMatcher.find()) {
                String tenantId = tenantResourceMatcher.group(1); // its ok to ignore if this method returns null, later on in the Controllers this will throw error
                TenantContext.setTenantId(tenantId);
            } else if (institutionCreationResourceMatcher.matches()) {
                String tentativeTenantId = UUID.randomUUID().toString();
                TenantContext.setTenantId(tentativeTenantId);
                log.info("institution context fired");
            } else {
                log.info("no tenant context fired");
//                throw new RuntimeException("no target tenant id found");
            }
            filterChain.doFilter(request, response);
        } finally {
            // clear request context at the end of the request
            TenantContext.clear();
        }
    }

}
