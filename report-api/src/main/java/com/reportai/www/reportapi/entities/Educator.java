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
import java.util.List;
import lombok.AllArgsConstructor;
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
@Table(name = "Educators")
public class Educator extends BaseEntity {

    public enum EMPLOYMENT_TYPE {
        FULL_TIME,
        PART_TIME
    }

    @Column(nullable = false)
    private String name;

    private LocalDate startDate;

    @ManyToOne
    @ToString.Exclude
    private Institution institution;

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
    private List<Outlet> outlets;

    @ManyToMany(mappedBy = "educators", fetch = FetchType.EAGER)
    private List<Subject> subjects;

    @OneToOne(fetch = FetchType.EAGER)
    private EducatorClientPersona educatorClientPersona;

    @ManyToMany(mappedBy = "educators", fetch = FetchType.LAZY)
    private List<Course> courses;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            joinColumns = @JoinColumn(name = "educator_id"),
            inverseJoinColumns = @JoinColumn(name = "level_id")
    )
    private List<Level> levels;

    @Column(nullable = false)
    private String tenantId;

}
