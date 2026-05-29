package com.aura.details.email;

import com.aura.core.entities.email.EmailMessage;
import com.aura.core.exceptions.EmailDeliveryException;
import jakarta.mail.Session;
import jakarta.mail.internet.MimeMessage;
import java.util.Properties;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mail.MailSendException;
import org.springframework.mail.javamail.JavaMailSender;


import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@DisplayName("SES email gateway")
@ExtendWith(MockitoExtension.class)
class SesEmailGatewayTest {

    @Mock
    private JavaMailSender mailSender;
    private SesEmailGateway gateway;

    @BeforeEach
    void setUp() {
        gateway = new SesEmailGateway(mailSender, "no-reply@aura.com", "Aura", 1L);
    }

    private EmailMessage sampleMessage() {
        return EmailMessage.builder()
                .recipientEmail("alice@example.com")
                .subject("Hello")
                .htmlBody("<p>Hi</p>")
                .textBody("Hi")
                .dedupKey("test-dedup")
                .templateName("test")
                .recipientUserId("user-1")
                .tenantId("tenant-1")
                .build();
    }

    private MimeMessage realMimeMessage() {
        return new MimeMessage(Session.getInstance(new Properties()));
    }

    @DisplayName("Should send via JavaMailSender and return a non-null provider id")
    @Test
    void sendsAndReturnsProviderId() {
        when(mailSender.createMimeMessage()).thenReturn(realMimeMessage());
        doNothing().when(mailSender).send(any(MimeMessage.class));

        String id = gateway.send(sampleMessage());

        assertThat(id).isNotBlank();
        verify(mailSender, times(1)).send(any(MimeMessage.class));
    }

    @DisplayName("Should retry once on transient failure and succeed")
    @Test
    void retriesOnceAndSucceeds() {
        when(mailSender.createMimeMessage()).thenAnswer(inv -> realMimeMessage());
        doThrow(new MailSendException("smtp down"))
                .doNothing()
                .when(mailSender).send(any(MimeMessage.class));

        String id = gateway.send(sampleMessage());

        assertThat(id).isNotBlank();
        verify(mailSender, times(2)).send(any(MimeMessage.class));
    }

    @DisplayName("Should throw EmailDeliveryException after second failure")
    @Test
    void throwsAfterSecondFailure() {
        when(mailSender.createMimeMessage()).thenAnswer(inv -> realMimeMessage());
        doThrow(new MailSendException("smtp down"))
                .when(mailSender).send(any(MimeMessage.class));

        assertThatThrownBy(() -> gateway.send(sampleMessage()))
                .isInstanceOf(EmailDeliveryException.class)
                .hasMessageContaining("test-dedup");
        verify(mailSender, times(2)).send(any(MimeMessage.class));
    }
}
