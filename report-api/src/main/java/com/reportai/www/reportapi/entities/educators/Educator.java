package com.reportai.www.reportapi.entities.educators;

import com.reportai.www.reportapi.entities.attachments.EducatorCourseAttachment;
import com.reportai.www.reportapi.entities.attachments.EducatorLessonAttachment;
import com.reportai.www.reportapi.entities.attachments.LevelEducatorAttachment;
import com.reportai.www.reportapi.entities.attachments.OutletEducatorAttachment;
import com.reportai.www.reportapi.entities.attachments.SubjectEducatorAttachment;
import com.reportai.www.reportapi.entities.base.TenantAwareBaseEntity;
import com.reportai.www.reportapi.entities.helpers.EntityRelationshipUtils;
import com.reportai.www.reportapi.entities.personas.EducatorClientPersona;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

@Entity
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

    @OneToOne(fetch = FetchType.LAZY)
    @ToString.Exclude
    private EducatorClientPersona educatorClientPersona;

    public Educator addEducatorClientPersona(@NonNull EducatorClientPersona educatorClientPersona) {
        return EntityRelationshipUtils.setOneToOne(this, educatorClientPersona, this::setEducatorClientPersona, educatorClientPersona::setEducator);
    }


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


}
