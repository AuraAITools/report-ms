package com.aura.details.springdata.models.accounts;

import com.aura.details.springdata.models.base.TenantBaseModel;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
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
@Table(name = "Parents")
public class ParentModel extends TenantBaseModel {

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "account_id")
    @ToString.Exclude
    private AccountModel account;

    @Enumerated(EnumType.STRING)
    private RELATIONSHIP relationship;

    @Column(length = 512)
    private String profileImageKey;

    public enum RELATIONSHIP {
        PARENT,
        SELF
    }
}
