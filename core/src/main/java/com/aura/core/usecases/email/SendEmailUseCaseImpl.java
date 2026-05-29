package com.aura.core.usecases.email;

import com.aura.core.entities.email.EmailLog;
import com.aura.core.entities.email.EmailLogStatus;
import com.aura.core.entities.email.EmailMessage;
import com.aura.core.exceptions.EmailDeliveryException;
import com.aura.core.repositories.email.EmailGateway;
import com.aura.core.repositories.email.EmailLogRepository;
import com.aura.core.repositories.email.TemplateRenderer;
import java.time.Instant;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
public class SendEmailUseCaseImpl implements SendEmailUseCase {

    private final EmailLogRepository emailLogRepository;
    private final EmailGateway emailGateway;
    private final TemplateRenderer templateRenderer;

    @Override
    public void send(SendEmailRequest request) {
        Optional<EmailLog> existing = emailLogRepository
                .findActiveByTenantAndDedupKey(request.tenantId(), request.dedupKey());
        if (existing.isPresent()) {
            log.info("Skipping send for dedupKey={} — active row already exists ({})",
                    request.dedupKey(), existing.get().getStatus());
            return;
        }

        if (request.recipientOptedOut()) {
            log.info("Recipient {} has opted out — recording SKIPPED row for dedupKey={}",
                    request.recipientEmail(), request.dedupKey());
            emailLogRepository.save(buildLog(request, EmailLogStatus.SKIPPED));
            return;
        }

        EmailLog pending = emailLogRepository.save(buildLog(request, EmailLogStatus.PENDING));

        TemplateRenderer.RenderedEmail rendered;
        try {
            rendered = templateRenderer.render(request.templateName(), request.variables());
        } catch (RuntimeException e) {
            log.error("Template render failure for template={} dedupKey={}",
                    request.templateName(), request.dedupKey(), e);
            emailLogRepository.updateStatus(pending.getId(), EmailLogStatus.FAILED, null,
                    truncate("render: " + e.getMessage()), null);
            return;
        }

        EmailMessage message = EmailMessage.builder()
                .recipientEmail(request.recipientEmail())
                .subject(request.subject())
                .htmlBody(rendered.html())
                .textBody(rendered.text())
                .dedupKey(request.dedupKey())
                .templateName(request.templateName())
                .recipientUserId(request.recipientUserId())
                .tenantId(request.tenantId())
                .build();

        try {
            String providerMessageId = emailGateway.send(message);
            emailLogRepository.updateStatus(pending.getId(), EmailLogStatus.SENT,
                    providerMessageId, null, Instant.now());
        } catch (EmailDeliveryException e) {
            emailLogRepository.updateStatus(pending.getId(), EmailLogStatus.FAILED, null,
                    truncate(e.getMessage()), null);
        }
    }

    private static EmailLog buildLog(SendEmailRequest request, EmailLogStatus status) {
        return EmailLog.builder()
                .tenantId(request.tenantId())
                .recipientEmail(request.recipientEmail())
                .recipientUserId(request.recipientUserId())
                .templateName(request.templateName())
                .subject(request.subject())
                .dedupKey(request.dedupKey())
                .status(status)
                .build();
    }

    private static String truncate(String s) {
        if (s == null) return null;
        return s.length() <= 2000 ? s : s.substring(0, 2000);
    }
}
