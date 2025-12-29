package com.aura.details.springdata.tables.educators;

import com.aura.details.springdata.tables.base.TenantBaseTable;
import com.aura.details.springdata.tables.links.AccountEducatorLinkTable;
import com.aura.details.springdata.tables.links.EducatorCourseLinkTable;
import com.aura.details.springdata.tables.links.EducatorLessonLinkTable;
import com.aura.details.springdata.tables.links.LevelEducatorLinkTable;
import com.aura.details.springdata.tables.links.OutletEducatorLinkTable;
import com.aura.details.springdata.tables.links.SubjectEducatorLinkTable;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.Builder;
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
@Table(name = "Educators")
public class EducatorTable extends TenantBaseTable {

    @Column(nullable = false)
    private String name;

    private LocalDate startDate;

    private LocalDate dateOfBirth;

    @Email
    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private EMPLOYMENT_TYPE employmentType;

    @OneToMany(mappedBy = "educator", fetch = FetchType.LAZY)
    @Builder.Default
    private Set<EducatorCourseLinkTable> educatorCourseLinks = new HashSet<>();

    @OneToMany(mappedBy = "educator", fetch = FetchType.LAZY)
    @Builder.Default
    private Set<SubjectEducatorLinkTable> subjectEducatorLinks = new HashSet<>();

    @OneToMany(mappedBy = "educator", fetch = FetchType.LAZY)
    @ToString.Exclude
    @Builder.Default
    private Set<OutletEducatorLinkTable> outletEducatorLinks = new HashSet<>();

    @OneToMany(mappedBy = "educator", fetch = FetchType.LAZY)
    @Builder.Default
    @ToString.Exclude
    private Set<LevelEducatorLinkTable> levelEducatorLinks = new HashSet<>();

    @OneToMany(mappedBy = "educator", fetch = FetchType.LAZY)
    @Builder.Default
    @ToString.Exclude
    private Set<EducatorLessonLinkTable> educatorLessonLinks = new HashSet<>();

    @OneToMany(mappedBy = "educator", fetch = FetchType.LAZY)
    @Builder.Default
    @ToString.Exclude
    private Set<AccountEducatorLinkTable> accountEducatorLinks = new HashSet<>();

    public enum EMPLOYMENT_TYPE {
        FULL_TIME, PART_TIME
    }

}
