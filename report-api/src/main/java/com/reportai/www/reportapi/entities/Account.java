package com.reportai.www.reportapi.entities;

import com.reportai.www.reportapi.entities.personas.Persona;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

/**
 * This account is tenant aware, an account belongs to an institution
 * This means a single keycloak user account can be mapped to multiple TenantAwareAccounts
 */
@Entity
@Data
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "Accounts")
public class Account extends BaseEntity {

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

    @OneToMany(mappedBy = "account", fetch = FetchType.EAGER, cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH})
    @Builder.Default
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private List<Persona> personas = new ArrayList<>();

    // institutions managed under this account
    @ManyToOne(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private Institution institution;

    @Column(nullable = false)
    private String tenantId;

    /**
     * If persona is transient entity, creates persona under account
     * If persona is not transient entity, associate persona with account
     *
     * @param persona entity
     * @return account entity
     */
    public Account addPersona(Persona persona) {
        assert persona != null : "input persona cannot be null";
        assert persona.getAccount() == null : "Persona already has an account";
        // INFO: have to update both sides of the entity
        persona.setAccount(this);
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
