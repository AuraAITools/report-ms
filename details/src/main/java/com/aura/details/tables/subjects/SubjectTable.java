package com.aura.details.tables.subjects;

import com.aura.details.tables.base.TenantBaseTable;
import com.aura.details.tables.lessons.LessonTable;
import com.aura.details.tables.links.SubjectCourseLinkTable;
import com.aura.details.tables.links.SubjectEducatorLinkTable;
import com.aura.details.tables.links.SubjectLevelLinkTable;
import com.aura.details.tables.links.SubjectStudentLinkTable;
import com.aura.details.tables.links.SubjectTestGroupLinkTable;
import com.aura.details.tables.links.TopicSubjectLinkTable;
import com.aura.details.tables.tests.TestResultTable;
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
import org.hibernate.envers.Audited;

@Entity
@Audited
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
public class SubjectTable extends TenantBaseTable {

    private String name;

    @OneToMany(mappedBy = "subject", fetch = FetchType.LAZY)
    @Builder.Default
    private Set<SubjectCourseLinkTable> subjectCourseLinks = new HashSet<>();

    //     NOTE: deleting Subject shouldn't delete all the lessons
    @OneToMany(mappedBy = "subject", fetch = FetchType.LAZY)
    @Builder.Default
    @ToString.Exclude
    private Set<LessonTable> lessons = new HashSet<>();

    @OneToMany(mappedBy = "subject", fetch = FetchType.LAZY)
    @Builder.Default
    private Set<SubjectStudentLinkTable> subjectStudentLinks = new HashSet<>();

    @OneToMany(mappedBy = "subject", fetch = FetchType.LAZY)
    @Builder.Default
    private Set<SubjectEducatorLinkTable> subjectEducatorLinks = new HashSet<>();

    @OneToMany(mappedBy = "subject", fetch = FetchType.LAZY)
    @Builder.Default
    private Set<SubjectTestGroupLinkTable> subjectTestGroupLinks = new HashSet<>();

    @OneToMany(mappedBy = "subject", fetch = FetchType.LAZY)
    @ToString.Exclude
    @Builder.Default
    private Set<TestResultTable> testResults = new HashSet<>();

    @OneToMany(mappedBy = "subject", fetch = FetchType.LAZY)
    @ToString.Exclude
    @Builder.Default
    private Set<SubjectLevelLinkTable> subjectLevelLinks = new HashSet<>();

    @OneToMany(mappedBy = "subject", fetch = FetchType.LAZY)
    @ToString.Exclude
    @Builder.Default
    private Set<TopicSubjectLinkTable> topicSubjectLinks = new HashSet<>();

}
