package com.reportai.www.reportapi.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "Educators")
@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Educator extends BaseEntity {

    @Column(nullable = false)
    private String name;

    @Email
    @Column(nullable = false, unique = true)
    private String email;

    @ManyToMany(mappedBy = "educators", fetch = FetchType.LAZY)
    private Set<Institution> institutions;

    @ManyToMany(mappedBy = "educators", fetch = FetchType.EAGER)
    private Set<Subject> subjects;

    @ManyToMany(mappedBy = "educators", fetch = FetchType.LAZY)
    private Set<Account> accounts;

}
