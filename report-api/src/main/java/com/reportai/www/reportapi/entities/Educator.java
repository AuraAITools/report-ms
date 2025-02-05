package com.reportai.www.reportapi.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

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

    @ManyToOne
    @ToString.Exclude
    private Institution institution;

    @Email
    @Column(nullable = false)
    private String email;

    @ManyToMany(mappedBy = "educators", fetch = FetchType.LAZY)
    private List<Outlet> outlets;

    @ManyToMany(mappedBy = "educators", fetch = FetchType.EAGER)
    private List<Subject> subjects;

    @ManyToMany(mappedBy = "educators", fetch = FetchType.LAZY)
    private List<Account> accounts;

    @ManyToMany(mappedBy = "educators", fetch = FetchType.LAZY)
    private List<Course> courses;

    @Column(nullable = false)
    private String tenantId;

}
