package com.aura.details.springdata.repositories.impl;

import com.aura.core.entities.email.EmailLog;
import com.aura.core.entities.email.EmailLogStatus;
import com.aura.core.repositories.email.EmailLogRepository;
import com.aura.details.springdata.mappers.email.EmailLogMapper;
import com.aura.details.springdata.models.email.EmailLogModel;
import com.aura.details.springdata.repositories.jpa.email.JpaEmailLogRepository;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.Nullable;

@RequiredArgsConstructor
public class EmailLogRepositoryImpl implements EmailLogRepository {

    private static final List<EmailLogStatus> ACTIVE_STATUSES = List.of(EmailLogStatus.PENDING, EmailLogStatus.SENT);

    private final JpaEmailLogRepository jpaEmailLogRepository;

    @Override
    public EmailLog save(EmailLog log) {
        EmailLogModel model = EmailLogMapper.INSTANCE.toModelEntity(log);
        EmailLogModel saved = jpaEmailLogRepository.save(model);
        return EmailLogMapper.INSTANCE.toDomainEntity(saved);
    }

    @Override
    public Optional<EmailLog> findActiveByTenantAndDedupKey(String tenantId, String dedupKey) {
        return jpaEmailLogRepository
                .findFirstByTenantIdAndDedupKeyAndStatusInOrderByCreatedAtDesc(tenantId, dedupKey, ACTIVE_STATUSES)
                .map(EmailLogMapper.INSTANCE::toDomainEntity);
    }

    @Override
    public EmailLog updateStatus(String id,
                                 EmailLogStatus status,
                                 @Nullable String providerMessageId,
                                 @Nullable String error,
                                 @Nullable Instant sentAt) {
        EmailLogModel model = jpaEmailLogRepository.findById(UUID.fromString(id))
                .orElseThrow(() -> new IllegalStateException("EmailLog not found: " + id));
        model.setStatus(status);
        model.setProviderMessageId(providerMessageId);
        model.setError(error);
        model.setSentAt(sentAt);
        return EmailLogMapper.INSTANCE.toDomainEntity(jpaEmailLogRepository.save(model));
    }
}
