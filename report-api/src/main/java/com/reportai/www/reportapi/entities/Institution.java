package com.reportai.www.reportapi.entities;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrePersist;
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

    @ManyToMany(fetch = FetchType.LAZY)
    private List<Student> students;

    @ManyToMany(fetch = FetchType.LAZY)
    private List<Educator> educators;

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

    @ManyToMany(mappedBy = "institutions", fetch = FetchType.LAZY)
    private List<Account> accounts;

    @OneToMany(mappedBy = "institution", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Outlet> outlets;

    @OneToMany(mappedBy = "institution", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Level> levels;

    @OneToMany(mappedBy = "institution", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Subject> subjects;
    
    @Column(nullable = false)
    private String tenantId;

    @PrePersist
    public void updateTenantIdOnCreation() {
        if (this.tenantId == null) {
            this.tenantId = this.getId().toString();
        }
    }

}
