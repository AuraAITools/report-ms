package com.aura.app.audit;

import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

@Slf4j
public class AuditorAwareImpl implements AuditorAware<String> {
    public AuditorAwareImpl() {
    }

    @Override
    public Optional<String> getCurrentAuditor() {
        return Optional.ofNullable(SecurityContextHolder
                .getContext()
                .getAuthentication()).map(Authentication::getName);
    }
}
