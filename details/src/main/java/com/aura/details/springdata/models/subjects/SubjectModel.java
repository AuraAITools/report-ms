package com.aura.details.springdata.models.subjects;

import com.aura.details.springdata.models.base.TenantBaseModel;
import com.aura.details.springdata.models.links.SubjectCourseLinkModel;
import com.aura.details.springdata.models.links.SubjectEducatorLinkModel;
import com.aura.details.springdata.models.links.SubjectLevelLinkModel;
import com.aura.details.springdata.models.links.SubjectStudentLinkModel;
import com.aura.details.springdata.models.links.SubjectTestGroupLinkModel;
import com.aura.details.springdata.models.links.TopicSubjectLinkModel;
import com.aura.details.springdata.models.tests.TestResultModel;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
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
@Table(name = "Subjects", uniqueConstraints = {
        @UniqueConstraint(
                name = "uk_subject_name",
                columnNames = {"tenant_id", "name"}
        )
})
public class SubjectModel extends TenantBaseModel {

    private String name;

    @OneToMany(mappedBy = "subject", fetch = FetchType.LAZY)
    @ToString.Exclude
    @Builder.Default
    private Set<SubjectCourseLinkModel> subjectCourseLinks = new HashSet<>();

    @OneToMany(mappedBy = "subject", fetch = FetchType.LAZY)
    @ToString.Exclude
    @Builder.Default
    private Set<SubjectStudentLinkModel> subjectStudentLinks = new HashSet<>();

    @OneToMany(mappedBy = "subject", fetch = FetchType.LAZY)
    @ToString.Exclude
    @Builder.Default
    private Set<SubjectEducatorLinkModel> subjectEducatorLinks = new HashSet<>();

    @OneToMany(mappedBy = "subject", fetch = FetchType.LAZY)
    @ToString.Exclude
    @Builder.Default
    private Set<SubjectTestGroupLinkModel> subjectTestGroupLinks = new HashSet<>();

    @OneToMany(mappedBy = "subject", fetch = FetchType.LAZY)
    @ToString.Exclude
    @Builder.Default
    private Set<TestResultModel> testResults = new HashSet<>();

    @OneToMany(mappedBy = "subject", fetch = FetchType.LAZY)
    @ToString.Exclude
    @Builder.Default
    private Set<SubjectLevelLinkModel> subjectLevelLinks = new HashSet<>();

    @OneToMany(mappedBy = "subject", fetch = FetchType.LAZY)
    @ToString.Exclude
    @Builder.Default
    private Set<TopicSubjectLinkModel> topicSubjectLinks = new HashSet<>();

}
