package com.reportai.www.reportapi.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "Clients")
public class Client extends BaseEntity {
    public enum RELATIONSHIP {
        PARENT,
        SELF
    }

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private RELATIONSHIP relationship;

    @Column(nullable = false)
    private String tenantId;

    @OneToOne
    private Account account;
}
