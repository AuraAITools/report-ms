package com.aura.details.tables.students;

import com.aura.details.tables.base.TenantBaseTable;
import com.aura.details.tables.levels.LevelTable;
import com.aura.details.tables.links.AccountStudentLinkTable;
import com.aura.details.tables.links.StudentCourseLinkTable;
import com.aura.details.tables.links.StudentLessonLinkTable;
import com.aura.details.tables.links.StudentOutletLinkTable;
import com.aura.details.tables.links.SubjectStudentLinkTable;
import com.aura.details.tables.schools.SchoolTable;
import com.aura.details.tables.tests.TestResultTable;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ManyToOne;
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
@Table(name = "Students")
public class StudentTable extends TenantBaseTable {

    @Column(nullable = false)
    private String name;

    @Email
    @Column(nullable = false)
    private String email;

    private LocalDate dateOfBirth;

    @ManyToOne(fetch = FetchType.LAZY)
    @ToString.Exclude
    private SchoolTable school;

    @ManyToOne(fetch = FetchType.LAZY)
    @ToString.Exclude
    private LevelTable level;

    @OneToMany(mappedBy = "student", fetch = FetchType.LAZY)
    @Builder.Default
    @ToString.Exclude
    private Set<SubjectStudentLinkTable> subjectStudentLinks = new HashSet<>();

    @OneToMany(mappedBy = "student", fetch = FetchType.LAZY)
    @Builder.Default
    @ToString.Exclude
    private Set<TestResultTable> testResults = new HashSet<>();

    @OneToMany(mappedBy = "student", fetch = FetchType.LAZY)
    @Builder.Default
    @ToString.Exclude
    private Set<StudentLessonLinkTable> studentLessonLinks = new HashSet<>();

    @OneToMany(mappedBy = "student", fetch = FetchType.LAZY)
    @Builder.Default
    @ToString.Exclude
    private Set<AccountStudentLinkTable> accountStudentLinks = new HashSet<>();

    @OneToMany(mappedBy = "student", fetch = FetchType.LAZY)
    @Builder.Default
    @ToString.Exclude
    private Set<StudentCourseLinkTable> studentCourseLinks = new HashSet<>();

    @OneToMany(mappedBy = "student", fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    @Builder.Default
    @ToString.Exclude
    private Set<StudentOutletLinkTable> studentOutletLinks = new HashSet<>();


}
