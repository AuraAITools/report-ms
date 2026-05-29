package com.aura.core.repositories.email;

import com.aura.core.entities.email.EmailLog;
import com.aura.core.entities.email.EmailLogStatus;
import java.time.Instant;
import java.util.Optional;
import org.jspecify.annotations.Nullable;

public interface EmailLogRepository {
    EmailLog save(EmailLog log);

    /**
     * Finds the most recent EmailLog for a given (tenantId, dedupKey) whose status
     * is currently active (PENDING or SENT). Used to short-circuit duplicate sends.
     */
    Optional<EmailLog> findActiveByTenantAndDedupKey(String tenantId, String dedupKey);

    EmailLog updateStatus(String id,
                          EmailLogStatus status,
                          @Nullable String providerMessageId,
                          @Nullable String error,
                          @Nullable Instant sentAt);
}
