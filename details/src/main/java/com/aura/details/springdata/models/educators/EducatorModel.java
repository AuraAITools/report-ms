package com.aura.details.springdata.models.educators;

import com.aura.details.springdata.models.base.TenantBaseModel;
import com.aura.details.springdata.models.links.AccountEducatorLinkModel;
import com.aura.details.springdata.models.links.EducatorCourseLinkModel;
import com.aura.details.springdata.models.links.EducatorLessonLinkModel;
import com.aura.details.springdata.models.links.LevelEducatorLinkModel;
import com.aura.details.springdata.models.links.OutletEducatorLinkModel;
import com.aura.details.springdata.models.links.SubjectEducatorLinkModel;
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
import lombok.EqualsAndHashCode;
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
@EqualsAndHashCode(of = {}, callSuper = true)
@Table(name = "Educators")
public class EducatorModel extends TenantBaseModel {

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
    @ToString.Exclude
    @Builder.Default
    private Set<EducatorCourseLinkModel> educatorCourseLinks = new HashSet<>();

    @OneToMany(mappedBy = "educator", fetch = FetchType.LAZY)
    @ToString.Exclude
    @Builder.Default
    private Set<SubjectEducatorLinkModel> subjectEducatorLinks = new HashSet<>();

    @OneToMany(mappedBy = "educator", fetch = FetchType.LAZY)
    @ToString.Exclude
    @Builder.Default
    private Set<OutletEducatorLinkModel> outletEducatorLinks = new HashSet<>();

    @OneToMany(mappedBy = "educator", fetch = FetchType.LAZY)
    @Builder.Default
    @ToString.Exclude
    private Set<LevelEducatorLinkModel> levelEducatorLinks = new HashSet<>();

    @OneToMany(mappedBy = "educator", fetch = FetchType.LAZY)
    @Builder.Default
    @ToString.Exclude
    private Set<EducatorLessonLinkModel> educatorLessonLinks = new HashSet<>();

    @OneToMany(mappedBy = "educator", fetch = FetchType.LAZY)
    @Builder.Default
    @ToString.Exclude
    private Set<AccountEducatorLinkModel> accountEducatorLinks = new HashSet<>();

    public enum EMPLOYMENT_TYPE {
        FULL_TIME, PART_TIME
    }

}
