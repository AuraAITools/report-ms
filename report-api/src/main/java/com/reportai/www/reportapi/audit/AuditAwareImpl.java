package com.reportai.www.reportapi.audit;

import com.reportai.www.reportapi.dtos.auth.KeycloakUserPrincipal;
import java.util.Optional;
import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component("auditAwareImpl")
public class AuditAwareImpl implements AuditorAware<String> {

    @Override
    public Optional<String> getCurrentAuditor() {
        KeycloakUserPrincipal userPrincipal = (KeycloakUserPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return Optional.of(userPrincipal.getUserId());
    }
}
