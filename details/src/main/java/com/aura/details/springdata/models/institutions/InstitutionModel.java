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
@Table(name = "Institutions")
public class InstitutionModel extends AuditMetadata {

    // ID must remain non-generated
    @Id
    private UUID id;

    @Column(nullable = false, unique = true)
    private String name;

    @Email
    @Column(nullable = false, unique = true)
    private String email;

    private String uen;

    private String address;

    private String contactNumber;

    @Column(length = 512)
    private String logoKey;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private STATE state;

    @Column(nullable = false)
    private String tenantId;

    public enum STATE {
        PENDING,
        ACTIVE,
        INACTIVE
    }
}
