package com.reportai.www.reportapi.entities;

import com.reportai.www.reportapi.entities.personas.EducatorClientPersona;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

@Entity
@Data
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "Educators")
public class Educator extends BaseEntity {

    public enum EMPLOYMENT_TYPE {
        FULL_TIME,
        PART_TIME
    }

    @Column(nullable = false)
    private String name;

    private LocalDate startDate;

    private LocalDate dateOfBirth;

    @ManyToOne
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private Institution institution;

    public Educator addInstitution(@NonNull Institution institution) {
        if (this.getInstitution() != null) {
            throw new IllegalArgumentException("educator already part of a institution");
        }
        this.setInstitution(institution);
        institution.getEducators().add(this);
        return this;
    }

    @Email
    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private EMPLOYMENT_TYPE employmentType;

    @ManyToMany
    @JoinTable(
            joinColumns = @JoinColumn(name = "educator_id"),
            inverseJoinColumns = @JoinColumn(name = "outlet_id")
    )
    @Builder.Default
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private List<Outlet> outlets = new ArrayList<>();

    public Educator addOutlets(@NonNull List<Outlet> outlets) {
        assert Collections.disjoint(outlets, this.getOutlets());

        this.getOutlets().addAll(outlets);
        outlets.forEach(outlet -> outlet.getEducators().add(this));
        return this;
    }

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            joinColumns = @JoinColumn(name = "educator_id"),
            inverseJoinColumns = @JoinColumn(name = "subject_id")
    )
    @Builder.Default
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private List<Subject> subjects = new ArrayList<>();

    @OneToOne(fetch = FetchType.EAGER)
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private EducatorClientPersona educatorClientPersona;

    public Educator addEducatorClientPersona(@NonNull EducatorClientPersona educatorClientPersona) {
        if (this.getEducatorClientPersona() != null) {
            throw new IllegalArgumentException("there is an existing educator client persona");
        }
        this.setEducatorClientPersona(educatorClientPersona);
        educatorClientPersona.setEducator(this);
        return this;
    }

    @ManyToMany(mappedBy = "educators", fetch = FetchType.LAZY)
    @Builder.Default
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private List<Course> courses = new ArrayList<>();

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            joinColumns = @JoinColumn(name = "educator_id"),
            inverseJoinColumns = @JoinColumn(name = "level_id")
    )
    @Builder.Default
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private List<Level> levels = new ArrayList<>();

    @ManyToMany(mappedBy = "educators", fetch = FetchType.EAGER)
    @Builder.Default
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private List<Lesson> lessons = new ArrayList<>();

    @Column(nullable = false)
    private String tenantId;

}
