package com.aura.details.tables.institutions;

import com.aura.details.tables.base.AuditMetadata;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import java.util.UUID;
import lombok.AllArgsConstructor;
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
@Table(name = "Institutions")
public class InstitutionTable extends AuditMetadata {

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

    @Column(nullable = false)
    private String tenantId;

    private String random;

}
