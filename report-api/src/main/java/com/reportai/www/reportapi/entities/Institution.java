package com.reportai.www.reportapi.entities;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import java.util.List;
import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "Institutions")
@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Institution extends BaseEntity {

    @Column(nullable = false)
    private String name;

    @Email
    @Column(nullable = false, unique = true)
    private String email;

    @ManyToMany(fetch = FetchType.LAZY)
    private Set<Student> students;

    @ManyToMany(fetch = FetchType.LAZY)
    private Set<Educator> educators;

    // NOTE: deleting institution will delete all its courses too
    @OneToMany(mappedBy = "institution", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Course> courses;

    // NOTE: deleting institution will delete all its topics too
    @OneToMany(mappedBy = "institution", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Topic> topics;

    // NOTE: deleting institution will not delete all its invoices
    @OneToMany(mappedBy = "institution", fetch = FetchType.LAZY, cascade = {CascadeType.MERGE, CascadeType.PERSIST})
    private List<Invoice> invoices;

    // NOTE: deleting institution will delete all its testGroups
    @OneToMany(mappedBy = "institution", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<TestGroup> testGroups;

    // NOTE: deleting institution will delete all its materials
    @OneToMany(mappedBy = "institution", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Material> materials;

    @ManyToOne(fetch = FetchType.LAZY)
    private Account account;

}
