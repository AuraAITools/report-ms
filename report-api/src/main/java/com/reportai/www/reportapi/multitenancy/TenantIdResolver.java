package com.reportai.www.reportapi.multitenancy;

import com.reportai.www.reportapi.contexts.requests.TenantContext;
import java.util.Map;
import org.hibernate.cfg.AvailableSettings;
import org.hibernate.context.spi.CurrentTenantIdentifierResolver;
import org.springframework.boot.autoconfigure.orm.jpa.HibernatePropertiesCustomizer;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

/**
 * TenantIdResolver resolves the tenant id to be used in Hibernate's multitenancy feature
 * During INSERT,UPDATE operations, tenantId is inserted into the SQL for all entities tagged with @TenantId
 * During SELECT statements, a WHERE = tenantId is inserted into the SQL for all entities tagged with @TenantId
 */
@Component
public class TenantIdResolver implements CurrentTenantIdentifierResolver<String>, HibernatePropertiesCustomizer {
    private final String DEFAULT_TENANT = "";
    private final String SUPERUSER_ROLE = "ROLE_AURA_ADMIN";
    private final String TECHNICAL_SUPERUSER_TENANT = "*";

    @Override
    public String resolveCurrentTenantIdentifier() {
        String tenantId = TenantContext.getTenantId();

        // resolve tenant id for multitenancy feature
        if (tenantId != null && !tenantId.isEmpty()) {
            return tenantId; // TODO: think whether setting empty tenant as default will have repercussions
        }

        // can read everything but cannot create
        if (isTechnicalSuperUser()) {
            return TECHNICAL_SUPERUSER_TENANT;
        }

        // initial startup
        return DEFAULT_TENANT;
    }


    /**
     * identifies if user is a superuser (AURA_ADMIN)
     *
     * @return
     */
    private boolean isTechnicalSuperUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            return authentication.getAuthorities().stream().anyMatch(auth -> auth.getAuthority().equals(SUPERUSER_ROLE));
        }
        return false;
    }

    /**
     * false: telling Hibernate "don't bother checking existing sessions when the tenant changes
     * true: sessions are validated when tenant context changes
     *
     * @return
     */
    @Override
    public boolean validateExistingCurrentSessions() {
        return true;
    }

    @Override
    public void customize(Map<String, Object> hibernateProperties) {
        hibernateProperties.put(AvailableSettings.MULTI_TENANT_IDENTIFIER_RESOLVER, this);
    }
}
