package com.reportai.www.reportapi.entities;

import com.reportai.www.reportapi.entities.personas.InstitutionAdminPersona;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

@Entity
@Data
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "Institutions")
public class Institution extends BaseEntity {

    @Column(nullable = false, unique = true)
    private String name;

    @Email
    @Column(nullable = false, unique = true)
    private String email;

    private String uen;

    private String address;

    private String contactNumber;

    @OneToMany(mappedBy = "institution", fetch = FetchType.LAZY)
    @Builder.Default
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private List<Student> students = new ArrayList<>();

    @OneToMany(mappedBy = "institution", fetch = FetchType.LAZY)
    @Builder.Default
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private List<Educator> educators = new ArrayList<>();

    // NOTE: deleting institution will delete all its courses too
    @OneToMany(mappedBy = "institution", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @Builder.Default
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private List<Course> courses = new ArrayList<>();

    // NOTE: deleting institution will delete all its topics too
    @OneToMany(mappedBy = "institution", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @Builder.Default
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private List<Topic> topics = new ArrayList<>();

    // NOTE: deleting institution will not delete all its invoices
    @OneToMany(mappedBy = "institution", fetch = FetchType.LAZY, cascade = {CascadeType.MERGE, CascadeType.PERSIST})
    @Builder.Default
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private List<Invoice> invoices = new ArrayList<>();

    // NOTE: deleting institution will delete all its testGroups
    @OneToMany(mappedBy = "institution", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @Builder.Default
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private List<TestGroup> testGroups = new ArrayList<>();

    // NOTE: deleting institution will delete all its materials
    @OneToMany(mappedBy = "institution", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @Builder.Default
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private List<Material> materials = new ArrayList<>();

    @OneToMany(mappedBy = "institution", fetch = FetchType.LAZY)
    @Builder.Default
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private List<Account> accounts = new ArrayList<>();

    @OneToMany(mappedBy = "institution", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @Builder.Default
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private List<Outlet> outlets = new ArrayList<>();

    @OneToMany(mappedBy = "institution", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @Builder.Default
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private List<Level> levels = new ArrayList<>();

    @OneToMany(mappedBy = "institution", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @Builder.Default
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private List<Subject> subjects = new ArrayList<>();

    @OneToMany(mappedBy = "institution", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @ToString.Exclude
    @Builder.Default
    @EqualsAndHashCode.Exclude
    private List<InstitutionAdminPersona> institutionAdmins = new ArrayList<>();

    @Column(nullable = false)
    private String tenantId;

    @PrePersist
    public void updateTenantIdOnCreation() {
        if (this.tenantId == null) {
            this.tenantId = this.getId().toString();
        }
    }

}
