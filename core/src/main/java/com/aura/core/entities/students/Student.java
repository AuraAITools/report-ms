package com.aura.core.entities.students;

import com.aura.core.entities.levels.Level;
import com.aura.core.entities.links.AccountStudentLink;
import com.aura.core.entities.links.StudentCourseLink;
import com.aura.core.entities.links.StudentLessonLink;
import com.aura.core.entities.links.StudentOutletLink;
import com.aura.core.entities.links.SubjectStudentLink;
import com.aura.core.entities.schools.School;
import com.aura.core.entities.tests.TestResult;
import java.time.Instant;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Student {

    private String id;
    private Long version;
    private Instant createdAt;
    private String createdBy;
    private Instant updatedAt;
    private String updatedBy;
    private String tenantId;
    private String name;

    private String email;

    private LocalDate dateOfBirth;

    private School school;

    private Level level;
    private Set<SubjectStudentLink> subjectStudentLinks = new HashSet<>();
    private Set<TestResult> testResults = new HashSet<>();
    private Set<StudentLessonLink> studentLessonLinks = new HashSet<>();
    private Set<AccountStudentLink> accountStudentLinks = new HashSet<>();
    private Set<StudentCourseLink> studentCourseLinks = new HashSet<>();
    private Set<StudentOutletLink> studentOutletLinks = new HashSet<>();
}
