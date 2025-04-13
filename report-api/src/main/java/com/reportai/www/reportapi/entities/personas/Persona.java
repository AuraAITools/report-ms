package com.reportai.www.reportapi.entities.personas;

import com.reportai.www.reportapi.entities.Account;
import com.reportai.www.reportapi.entities.base.TenantAwareBaseEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

@Entity
@Getter
@Setter
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "Personas")
@Inheritance(strategy = InheritanceType.JOINED)
public class Persona extends TenantAwareBaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @ToString.Exclude
    private Account account;


    public Persona addToAccount(Account account) {
        if (account == null) {
            throw new IllegalArgumentException("Account cannot be null");
        }

        if (this.getAccount() != null) {
            throw new IllegalArgumentException("Account for persona already exists");
        }

        this.setAccount(account);
        account.addPersona(this);
        return this;
    }
}
