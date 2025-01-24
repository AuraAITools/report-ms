package com.reportai.www.reportapi.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity
@Data
@EqualsAndHashCode(callSuper = true)
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "Educators")
public class Educator extends BaseEntity {

    @Column(nullable = false)
    private String name;

    @Email
    @Column(nullable = false, unique = true)
    private String email;

    @ManyToMany(mappedBy = "educators", fetch = FetchType.LAZY)
    private List<Institution> institutions;

    @ManyToMany(mappedBy = "educators", fetch = FetchType.EAGER)
    private List<Subject> subjects;

    @ManyToMany(mappedBy = "educators", fetch = FetchType.LAZY)
    private List<Account> accounts;

    @Column(nullable = false)
    private String tenantId;

}
