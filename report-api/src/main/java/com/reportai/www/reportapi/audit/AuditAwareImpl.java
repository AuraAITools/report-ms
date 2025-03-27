package com.reportai.www.reportapi.audit;

import com.reportai.www.reportapi.dtos.auth.KeycloakUserPrincipal;
import java.util.Optional;
import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

/**
 * TODO: refactor
 */
@Component("auditAwareImpl")
public class AuditAwareImpl implements AuditorAware<String> {

    @Override
    public Optional<String> getCurrentAuditor() {
        KeycloakUserPrincipal userPrincipal = (KeycloakUserPrincipal) SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getPrincipal();
        // ensure user identity is always present when mutating resources
        assert userPrincipal.getUserId() != null;
        return Optional.of(userPrincipal.getUserId());
    }
}
