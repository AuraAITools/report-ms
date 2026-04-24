package com.aura.details.springdata.models.students;

import com.aura.details.springdata.models.base.TenantBaseModel;
import com.aura.details.springdata.models.levels.LevelModel;
import com.aura.details.springdata.models.links.AccountStudentLinkModel;
import com.aura.details.springdata.models.links.StudentCourseLinkModel;
import com.aura.details.springdata.models.links.StudentLessonLinkModel;
import com.aura.details.springdata.models.links.StudentOutletLinkModel;
import com.aura.details.springdata.models.links.SubjectStudentLinkModel;
import com.aura.details.springdata.models.schools.SchoolModel;
import com.aura.details.springdata.models.tests.TestResultModel;
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
@Table(name = "Students")
public class StudentModel extends TenantBaseModel {

    @Column(nullable = false)
    private String name;

    @Email
    @Column(nullable = false)
    private String email;

    private LocalDate dateOfBirth;

    @ManyToOne(fetch = FetchType.LAZY)
    @ToString.Exclude
    private SchoolModel school;

    @ManyToOne(fetch = FetchType.LAZY)
    @ToString.Exclude
    private LevelModel level;

    @OneToMany(mappedBy = "student", fetch = FetchType.LAZY)
    @Builder.Default
    @ToString.Exclude
    private Set<SubjectStudentLinkModel> subjectStudentLinks = new HashSet<>();

    @OneToMany(mappedBy = "student", fetch = FetchType.LAZY)
    @Builder.Default
    @ToString.Exclude
    private Set<TestResultModel> testResults = new HashSet<>();

    @OneToMany(mappedBy = "student", fetch = FetchType.LAZY)
    @Builder.Default
    @ToString.Exclude
    private Set<StudentLessonLinkModel> studentLessonLinks = new HashSet<>();

    @OneToMany(mappedBy = "student", fetch = FetchType.LAZY)
    @Builder.Default
    @ToString.Exclude
    private Set<AccountStudentLinkModel> accountStudentLinks = new HashSet<>();

    @OneToMany(mappedBy = "student", fetch = FetchType.LAZY)
    @Builder.Default
    @ToString.Exclude
    private Set<StudentCourseLinkModel> studentCourseLinks = new HashSet<>();

    @OneToMany(mappedBy = "student", fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    @Builder.Default
    @ToString.Exclude
    private Set<StudentOutletLinkModel> studentOutletLinks = new HashSet<>();


}
