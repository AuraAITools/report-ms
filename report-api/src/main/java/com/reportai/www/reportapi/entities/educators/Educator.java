package com.reportai.www.reportapi.entities.educators;

import com.reportai.www.reportapi.entities.attachments.AccountEducatorAttachment;
import com.reportai.www.reportapi.entities.attachments.EducatorCourseAttachment;
import com.reportai.www.reportapi.entities.attachments.EducatorLessonAttachment;
import com.reportai.www.reportapi.entities.attachments.LevelEducatorAttachment;
import com.reportai.www.reportapi.entities.attachments.OutletEducatorAttachment;
import com.reportai.www.reportapi.entities.attachments.SubjectEducatorAttachment;
import com.reportai.www.reportapi.entities.base.TenantAwareBaseEntity;
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
public class Educator extends TenantAwareBaseEntity {

    public enum EMPLOYMENT_TYPE {
        FULL_TIME,
        PART_TIME
    }

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
    private Set<EducatorCourseAttachment> educatorCourseAttachments = new HashSet<>();

    @OneToMany(mappedBy = "educator", fetch = FetchType.LAZY)
    @Builder.Default
    private Set<SubjectEducatorAttachment> subjectEducatorAttachments = new HashSet<>();

    @OneToMany(mappedBy = "educator", fetch = FetchType.LAZY)
    @ToString.Exclude
    @Builder.Default
    private Set<OutletEducatorAttachment> outletEducatorAttachments = new HashSet<>();

    @OneToMany(mappedBy = "educator", fetch = FetchType.LAZY)
    @Builder.Default
    @ToString.Exclude
    private Set<LevelEducatorAttachment> levelEducatorAttachments = new HashSet<>();

    @OneToMany(mappedBy = "educator", fetch = FetchType.LAZY)
    @Builder.Default
    @ToString.Exclude
    private Set<EducatorLessonAttachment> educatorLessonAttachments = new HashSet<>();

    @OneToMany(mappedBy = "educator", fetch = FetchType.LAZY)
    @Builder.Default
    @ToString.Exclude
    private Set<AccountEducatorAttachment> accountEducatorAttachments = new HashSet<>();

}
