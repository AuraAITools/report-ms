package com.reportai.www.reportapi.entities;

import com.reportai.www.reportapi.entities.attachments.AccountEducatorAttachment;
import com.reportai.www.reportapi.entities.attachments.AccountStudentAttachment;
import com.reportai.www.reportapi.entities.base.TenantAwareBaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import jakarta.validation.constraints.Email;
import java.util.HashSet;
import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import org.hibernate.envers.Audited;

/**
 * This account is tenant aware, an account belongs to an institution
 * This means a single keycloak user account can be mapped to multiple TenantAwareAccounts
 */
@Entity
@Audited
@Getter
@Setter
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "Accounts", uniqueConstraints = {
        @UniqueConstraint(
                name = "uk_tenant_email",
                columnNames = {"tenant_id", "email"}
        ),
        @UniqueConstraint(
                name = "uk_tenant_user",
                columnNames = {"tenant_id", "user_id"}
        )
})
public class Account extends TenantAwareBaseEntity {

    private String userId;

    @Email
    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private String firstName;

    @Column(nullable = false)
    private String lastName;

    @Column(nullable = false)
    private String contact;

    public enum RELATIONSHIP {
        PARENT,
        SELF
    }

    @Enumerated(EnumType.STRING)
    private RELATIONSHIP relationship;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "account")
    @ToString.Exclude
    @Builder.Default
    private Set<AccountStudentAttachment> accountStudentAttachments = new HashSet<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "account")
    @ToString.Exclude
    @Builder.Default
    private Set<AccountEducatorAttachment> accountEducatorAttachments = new HashSet<>();

}
