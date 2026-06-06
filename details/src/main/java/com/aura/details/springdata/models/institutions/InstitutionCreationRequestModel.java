package com.aura.details.springdata.models.institutions;

import com.aura.details.springdata.models.base.AuditMetadata;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import java.util.UUID;
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
@EqualsAndHashCode(of = {"id"})
@Table(name = "InstitutionCreationRequests")
public class InstitutionCreationRequestModel extends AuditMetadata {

    @Id
    private UUID id;

    @Column(nullable = false)
    private String institutionName;

    @Email
    @Column(nullable = false)
    private String institutionEmail;

    @Column(nullable = false)
    private String institutionContactNumber;

    @Column(nullable = false)
    private String institutionAddress;

    @Column(nullable = false)
    private String institutionUen;

    @Column(nullable = false)
    private String requesterFirstName;

    @Column(nullable = false)
    private String requesterLastName;

    @Email
    @Column(nullable = false)
    private String requesterEmail;

    @Column(nullable = false)
    private String requesterContactNumber;

    @Column(nullable = false, length = 50)
    @Enumerated(EnumType.STRING)
    private STATE state;

    public enum STATE {
        REQUESTED,
        FULFILLED,
        REJECTED
    }
}
