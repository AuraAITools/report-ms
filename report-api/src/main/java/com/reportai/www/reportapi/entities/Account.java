package com.reportai.www.reportapi.entities;

import com.reportai.www.reportapi.entities.base.TenantAwareBaseEntity;
import com.reportai.www.reportapi.entities.personas.Persona;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import jakarta.validation.constraints.Email;
import java.util.Collection;
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

    @Column(nullable = false)
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

    @OneToMany(mappedBy = "account", fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH})
    @Builder.Default
    @ToString.Exclude
    private Set<Persona> personas = new HashSet<>();


    /**
     * If persona is transient entity, creates persona under account
     * If persona is not transient entity, associate persona with account
     *
     * @param persona entity
     * @return account entity
     */
    public Account addPersona(Persona persona) {
        if (persona == null) {
            throw new IllegalArgumentException("input persona cannot be null");
        }
        // INFO: have to update both sides of the entity
        this.getPersonas().add(persona);
        return this;
    }

    /**
     * Creates and links persona to account if it doesn't already exist,
     * links persona to account if it does
     *
     * @param personas entity
     * @return account entity
     */
    public Account addPersonas(Collection<Persona> personas) {
        assert personas != null;
        assert !personas.isEmpty();

        personas.forEach(this::addPersona);
        return this;
    }
}
