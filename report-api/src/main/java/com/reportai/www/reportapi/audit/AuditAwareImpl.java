package com.reportai.www.reportapi.audit;

import com.reportai.www.reportapi.dtos.auth.KeycloakUserPrincipal;
import java.util.Objects;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component("auditAwareImpl")
@Slf4j
public class AuditAwareImpl implements AuditorAware<String> {

    @Override
    public @NotNull Optional<String> getCurrentAuditor() {
        KeycloakUserPrincipal userPrincipal = (KeycloakUserPrincipal) SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getPrincipal();
        // ensure user identity is always present when mutating resources
        Objects.requireNonNull(userPrincipal.getUserId());
        log.info("audit log for user {} with userId {}", userPrincipal.getName(), userPrincipal.getUserId());
        return Optional.of(userPrincipal.getUserId());
    }
}
