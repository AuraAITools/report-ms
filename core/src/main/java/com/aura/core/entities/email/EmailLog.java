package com.aura.core.entities.email;

import java.time.Instant;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class EmailLog {

    private String id;
    private Long version;
    private String tenantId;
    private String recipientEmail;
    private String recipientUserId;
    private String templateName;
    private String subject;
    private String dedupKey;
    private String providerMessageId;
    private EmailLogStatus status;
    private String error;
    private Instant sentAt;
    private Instant createdAt;
    private String createdBy;
    private Instant updatedAt;
    private String updatedBy;
}
