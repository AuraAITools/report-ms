package com.aura.details.springdata.repositories.impl;

import com.aura.core.entities.email.EmailLog;
import com.aura.core.entities.email.EmailLogStatus;
import com.aura.details.springdata.models.email.EmailLogModel;
import com.aura.details.springdata.repositories.jpa.email.JpaEmailLogRepository;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;


import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@DisplayName("Email log repository impl")
@ExtendWith(MockitoExtension.class)
class EmailLogRepositoryImplTest {

    @Mock
    private JpaEmailLogRepository jpaRepository;
    @InjectMocks
    private EmailLogRepositoryImpl repository;

    @DisplayName("Should map domain to model and persist via JPA")
    @Test
    void saveMapsAndPersists() {
        EmailLogModel saved = new EmailLogModel();
        saved.setId(UUID.randomUUID());
        when(jpaRepository.save(any(EmailLogModel.class))).thenReturn(saved);

        EmailLog domain = EmailLog.builder()
                .tenantId("tenant")
                .recipientEmail("a@b.com")
                .templateName("educator-welcome")
                .subject("Welcome")
                .dedupKey("educator-welcome:user-1")
                .status(EmailLogStatus.PENDING)
                .build();
        EmailLog result = repository.save(domain);

        assertThat(result).isNotNull();
        verify(jpaRepository).save(any(EmailLogModel.class));
    }

    @DisplayName("Should query JPA repo with PENDING + SENT statuses for active dedup lookup")
    @Test
    void findActiveByTenantAndDedupKeyPassesActiveStatuses() {
        when(jpaRepository.findFirstByTenantIdAndDedupKeyAndStatusInOrderByCreatedAtDesc(
                eq("tenant"),
                eq("dedup"),
                any()
        )).thenReturn(Optional.empty());

        var result = repository.findActiveByTenantAndDedupKey("tenant", "dedup");

        assertThat(result).isEmpty();
        ArgumentCaptor<List<EmailLogStatus>> statusesCaptor = ArgumentCaptor.forClass(List.class);
        verify(jpaRepository).findFirstByTenantIdAndDedupKeyAndStatusInOrderByCreatedAtDesc(
                eq("tenant"), eq("dedup"), statusesCaptor.capture());
        assertThat(statusesCaptor.getValue()).containsExactlyInAnyOrder(EmailLogStatus.PENDING, EmailLogStatus.SENT);
    }

    @DisplayName("Should fetch row, mutate fields, and re-save on updateStatus")
    @Test
    void updateStatusUpdatesAndSaves() {
        UUID id = UUID.randomUUID();
        EmailLogModel existing = new EmailLogModel();
        existing.setId(id);
        existing.setStatus(EmailLogStatus.PENDING);
        when(jpaRepository.findById(id)).thenReturn(Optional.of(existing));
        when(jpaRepository.save(any(EmailLogModel.class))).thenAnswer(inv -> inv.getArgument(0));

        Instant now = Instant.now();
        EmailLog updated = repository.updateStatus(id.toString(), EmailLogStatus.SENT, "msg-1", null, now);

        assertThat(updated).isNotNull();
        assertThat(existing.getStatus()).isEqualTo(EmailLogStatus.SENT);
        assertThat(existing.getProviderMessageId()).isEqualTo("msg-1");
        assertThat(existing.getSentAt()).isEqualTo(now);
        verify(jpaRepository).save(existing);
    }

    @DisplayName("Should throw when updating a non-existent EmailLog")
    @Test
    void updateStatusThrowsWhenMissing() {
        UUID id = UUID.randomUUID();
        when(jpaRepository.findById(id)).thenReturn(Optional.empty());

        var thrown = org.junit.jupiter.api.Assertions.assertThrows(
                IllegalStateException.class,
                () -> repository.updateStatus(id.toString(), EmailLogStatus.SENT, null, null, null));
        assertThat(thrown).hasMessageContaining(id.toString());
    }

    private static <T> T eq(T value) {
        return org.mockito.ArgumentMatchers.eq(value);
    }
}
