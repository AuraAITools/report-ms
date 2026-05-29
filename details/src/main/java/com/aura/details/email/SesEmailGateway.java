package com.aura.details.email;

import com.aura.core.entities.email.EmailMessage;
import com.aura.core.exceptions.EmailDeliveryException;
import com.aura.core.repositories.email.EmailGateway;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;

@Slf4j
@RequiredArgsConstructor
public class SesEmailGateway implements EmailGateway {

    private final JavaMailSender mailSender;
    private final String fromAddress;
    private final String fromDisplayName;
    private final long retryDelayMillis;

    @Override
    public String send(EmailMessage message) {
        try {
            return doSend(message);
        } catch (MailException | MessagingException firstError) {
            log.warn("First email send attempt failed for dedupKey={} to {}: {}",
                    message.dedupKey(), message.recipientEmail(), firstError.getMessage());
            sleep(retryDelayMillis);
            try {
                return doSend(message);
            } catch (MailException | MessagingException retryError) {
                String errorMsg = String.format("email send failed twice for dedupKey=%s: %s",
                        message.dedupKey(), retryError.getMessage());
                log.error(errorMsg, retryError);
                throw new EmailDeliveryException(errorMsg, retryError);
            }
        }
    }

    private String doSend(EmailMessage message) throws MessagingException {
        MimeMessage mime = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mime, true, StandardCharsets.UTF_8.name());
        try {
            helper.setFrom(new InternetAddress(fromAddress, fromDisplayName, StandardCharsets.UTF_8.name()));
        } catch (UnsupportedEncodingException e) {
            throw new MessagingException("invalid from-address encoding", e);
        }
        helper.setTo(message.recipientEmail());
        helper.setSubject(message.subject());
        helper.setText(message.textBody(), message.htmlBody());
        mailSender.send(mime);
        String providerId = mime.getMessageID();
        return providerId != null ? providerId : message.dedupKey();
    }

    private void sleep(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException ie) {
            Thread.currentThread().interrupt();
        }
    }
}
