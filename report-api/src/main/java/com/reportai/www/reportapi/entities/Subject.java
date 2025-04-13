package com.reportai.www.reportapi.entities;

import com.reportai.www.reportapi.entities.attachments.SubjectCourseAttachment;
import com.reportai.www.reportapi.entities.attachments.SubjectEducatorAttachment;
import com.reportai.www.reportapi.entities.attachments.SubjectLevelAttachment;
import com.reportai.www.reportapi.entities.attachments.SubjectStudentAttachment;
import com.reportai.www.reportapi.entities.attachments.SubjectTestGroupAttachment;
import com.reportai.www.reportapi.entities.base.TenantAwareBaseEntity;
import com.reportai.www.reportapi.entities.lessons.Lesson;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import java.util.HashSet;
import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

@Entity
@Getter
@Setter
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "Subjects", uniqueConstraints = {
        @UniqueConstraint(
                name = "uk_subject_name",
                columnNames = {"tenant_id", "name"}
        )
})
public class Subject extends TenantAwareBaseEntity {

    private String name;

    @OneToMany(mappedBy = "subject", fetch = FetchType.LAZY)
    @Builder.Default
    private Set<SubjectCourseAttachment> subjectCourseAttachments = new HashSet<>();

    // NOTE: deleting Subject shouldn't delete all the lessons
    @OneToMany(mappedBy = "subject", fetch = FetchType.LAZY)
    @Builder.Default
    @ToString.Exclude
    private Set<Lesson> lessons = new HashSet<>();

    @OneToMany(mappedBy = "subject", fetch = FetchType.LAZY)
    @Builder.Default
    private Set<SubjectStudentAttachment> subjectStudentAttachments = new HashSet<>();

    @OneToMany(mappedBy = "subject", fetch = FetchType.LAZY)
    @Builder.Default
    private Set<SubjectEducatorAttachment> subjectEducatorAttachments = new HashSet<>();

    @OneToMany(mappedBy = "subject", fetch = FetchType.LAZY)
    @Builder.Default
    private Set<SubjectTestGroupAttachment> subjectTestGroupAttachments = new HashSet<>();

    @OneToMany(mappedBy = "subject", fetch = FetchType.LAZY)
    @ToString.Exclude
    @Builder.Default
    private Set<TestResult> testResults = new HashSet<>();

    @OneToMany(mappedBy = "subject", fetch = FetchType.LAZY)
    @ToString.Exclude
    @Builder.Default
    private Set<SubjectLevelAttachment> subjectLevelAttachments = new HashSet<>();

}
