package com.aura.core.usecases.email;

import com.aura.core.entities.email.EmailLog;
import com.aura.core.entities.email.EmailLogStatus;
import com.aura.core.entities.email.EmailMessage;
import com.aura.core.exceptions.EmailDeliveryException;
import com.aura.core.repositories.email.EmailGateway;
import com.aura.core.repositories.email.EmailLogRepository;
import com.aura.core.repositories.email.TemplateRenderer;
import java.time.Instant;
import java.util.Map;
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
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.isNull;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@DisplayName("Send email use case")
@ExtendWith(MockitoExtension.class)
class SendEmailUseCaseImplTest {

    @Mock
    private EmailLogRepository emailLogRepository;
    @Mock
    private EmailGateway emailGateway;
    @Mock
    private TemplateRenderer templateRenderer;
    @InjectMocks
    private SendEmailUseCaseImpl useCase;

    private SendEmailRequest request() {
        return SendEmailRequest.builder()
                .templateName("educator-welcome")
                .subject("Welcome")
                .dedupKey("educator-welcome:user-1")
                .recipientEmail("alice@example.com")
                .recipientUserId("user-1")
                .tenantId("tenant-1")
                .variables(Map.of("firstName", "Alice"))
                .recipientOptedOut(false)
                .build();
    }

    private EmailLog pendingLog() {
        return EmailLog.builder().id(UUID.randomUUID().toString()).status(EmailLogStatus.PENDING).build();
    }

    @DisplayName("Should skip when active row already exists for dedupKey")
    @Test
    void skipsWhenActiveRowExists() {
        when(emailLogRepository.findActiveByTenantAndDedupKey("tenant-1", "educator-welcome:user-1"))
                .thenReturn(Optional.of(EmailLog.builder().status(EmailLogStatus.SENT).build()));

        useCase.send(request());

        verify(emailLogRepository, never()).save(any());
        verify(emailGateway, never()).send(any());
        verify(templateRenderer, never()).render(anyString(), any());
    }

    @DisplayName("Should record SKIPPED row when recipient has opted out")
    @Test
    void recordsSkippedWhenOptedOut() {
        when(emailLogRepository.findActiveByTenantAndDedupKey(anyString(), anyString())).thenReturn(Optional.empty());
        when(emailLogRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));

        useCase.send(SendEmailRequest.builder()
                .templateName("t").subject("s").dedupKey("d")
                .recipientEmail("a@b.com").recipientUserId("u").tenantId("t")
                .variables(Map.of()).recipientOptedOut(true).build());

        ArgumentCaptor<EmailLog> savedCaptor = ArgumentCaptor.forClass(EmailLog.class);
        verify(emailLogRepository).save(savedCaptor.capture());
        assertThat(savedCaptor.getValue().getStatus()).isEqualTo(EmailLogStatus.SKIPPED);
        verify(emailGateway, never()).send(any());
    }

    @DisplayName("Should render, dispatch via gateway, and update log to SENT on success")
    @Test
    void sendsAndMarksSent() {
        when(emailLogRepository.findActiveByTenantAndDedupKey(anyString(), anyString())).thenReturn(Optional.empty());
        EmailLog pending = pendingLog();
        when(emailLogRepository.save(any())).thenReturn(pending);
        when(templateRenderer.render(eq("educator-welcome"), any()))
                .thenReturn(new TemplateRenderer.RenderedEmail("<p>Hi</p>", "Hi"));
        when(emailGateway.send(any(EmailMessage.class))).thenReturn("provider-msg-1");

        useCase.send(request());

        verify(emailGateway).send(any(EmailMessage.class));
        verify(emailLogRepository).updateStatus(eq(pending.getId()),
                eq(EmailLogStatus.SENT),
                eq("provider-msg-1"),
                isNull(),
                any(Instant.class));
    }

    @DisplayName("Should mark log FAILED when gateway throws EmailDeliveryException")
    @Test
    void marksFailedOnGatewayFailure() {
        when(emailLogRepository.findActiveByTenantAndDedupKey(anyString(), anyString())).thenReturn(Optional.empty());
        EmailLog pending = pendingLog();
        when(emailLogRepository.save(any())).thenReturn(pending);
        when(templateRenderer.render(anyString(), any()))
                .thenReturn(new TemplateRenderer.RenderedEmail("<p>Hi</p>", "Hi"));
        when(emailGateway.send(any(EmailMessage.class)))
                .thenThrow(new EmailDeliveryException("smtp down"));

        useCase.send(request());

        verify(emailLogRepository).updateStatus(eq(pending.getId()),
                eq(EmailLogStatus.FAILED),
                isNull(),
                eq("smtp down"),
                isNull());
    }

    @DisplayName("Should mark log FAILED when template renderer throws")
    @Test
    void marksFailedOnRenderFailure() {
        when(emailLogRepository.findActiveByTenantAndDedupKey(anyString(), anyString())).thenReturn(Optional.empty());
        EmailLog pending = pendingLog();
        when(emailLogRepository.save(any())).thenReturn(pending);
        when(templateRenderer.render(anyString(), any())).thenThrow(new RuntimeException("template missing"));

        useCase.send(request());

        verify(emailGateway, never()).send(any());
        ArgumentCaptor<String> errorCaptor = ArgumentCaptor.forClass(String.class);
        verify(emailLogRepository).updateStatus(eq(pending.getId()),
                eq(EmailLogStatus.FAILED), isNull(), errorCaptor.capture(), isNull());
        assertThat(errorCaptor.getValue()).contains("render:").contains("template missing");
    }
}
