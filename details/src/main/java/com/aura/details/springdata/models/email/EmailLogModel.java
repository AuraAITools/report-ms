package com.aura.details.springdata.models.email;

import com.aura.core.entities.email.EmailLogStatus;
import com.aura.details.springdata.models.base.TenantBaseModel;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;
import java.time.Instant;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.hibernate.envers.Audited;

@Entity
@Audited
@Getter
@Setter
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(of = {}, callSuper = true)
@Table(name = "email_log")
public class EmailLogModel extends TenantBaseModel {

    @Column(nullable = false)
    private String recipientEmail;

    private String recipientUserId;

    @Column(nullable = false)
    private String templateName;

    @Column(nullable = false)
    private String subject;

    @Column(nullable = false)
    private String dedupKey;

    private String providerMessageId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EmailLogStatus status;

    @Column(columnDefinition = "TEXT")
    private String error;

    private Instant sentAt;
}
