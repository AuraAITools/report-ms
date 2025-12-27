package com.aura.core.entities.subjects;

import com.aura.core.entities.lessons.Lesson;
import com.aura.core.entities.links.SubjectCourseLink;
import com.aura.core.entities.links.SubjectEducatorLink;
import com.aura.core.entities.links.SubjectLevelLink;
import com.aura.core.entities.links.SubjectStudentLink;
import com.aura.core.entities.links.SubjectTestGroupLink;
import com.aura.core.entities.links.TopicSubjectLink;
import com.aura.core.entities.tests.TestResult;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class Subject {

    private UUID id;
    private Long version;
    private Instant createdAt;
    private String createdBy;
    private Instant updatedAt;
    private String updatedBy;
    private String tenantId;
    private String name;

    private Set<SubjectCourseLink> subjectCourseLinks = new HashSet<>();

    private Set<Lesson> lessons = new HashSet<>();

    private Set<SubjectStudentLink> subjectStudentLinks = new HashSet<>();

    private Set<SubjectEducatorLink> subjectEducatorLinks = new HashSet<>();

    private Set<SubjectTestGroupLink> subjectTestGroupLinks = new HashSet<>();

    private Set<TestResult> testResults = new HashSet<>();

    private Set<SubjectLevelLink> subjectLevelLinks = new HashSet<>();

    private Set<TopicSubjectLink> topicSubjectLinks = new HashSet<>();

}
