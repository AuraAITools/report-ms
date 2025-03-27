package com.reportai.www.reportapi.entities;

import com.reportai.www.reportapi.entities.analytics.LessonHomeworkCompletion;
import com.reportai.www.reportapi.entities.base.BaseEntity;
import com.reportai.www.reportapi.entities.personas.StudentClientPersona;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

@Entity
@Data
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "Students")
public class Student extends BaseEntity {

    @Column(nullable = false)
    private String name;

    @Email
    @Column(nullable = false, unique = true)
    private String email;

    private LocalDate dateOfBirth;

    private String currentSchool;

    @ManyToOne(fetch = FetchType.EAGER)
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private Level level;

    public Student addLevel(@NonNull Level level) {
        if (this.getLevel() != null) {
            throw new IllegalArgumentException("level already exists");
        }
        this.setLevel(level);
        level.getStudents().add(this);
        return this;
    }

    @ManyToMany(mappedBy = "students", fetch = FetchType.EAGER)
    @EqualsAndHashCode.Exclude
    @Builder.Default
    @ToString.Exclude
    private List<Subject> subjects = new ArrayList<>();

    /**
     * TODO: dont know if it will actually create the subject-student mapping
     *
     * @param subjects
     * @return
     */
    public Student addSubjects(@NonNull List<Subject> subjects) {
        assert Collections.disjoint(subjects, this.getSubjects());
        this.getSubjects().addAll(subjects);
        subjects.forEach(subject -> subject.getStudents().add(this));
        return this;
    }

    @OneToMany(mappedBy = "student", fetch = FetchType.LAZY)
    @Builder.Default
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private List<TestResult> testResults = new ArrayList<>();

    @OneToOne(mappedBy = "student", fetch = FetchType.LAZY)
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private StudentReport studentReport;

    @ManyToMany(mappedBy = "students", fetch = FetchType.LAZY)
    @Builder.Default
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private List<Lesson> lessons = new ArrayList<>();


    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(nullable = false)
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private StudentClientPersona studentClientPersona;

    public Student addStudentClientPersona(@NonNull StudentClientPersona studentClientPersona) {
        if (this.getStudentClientPersona() != null) {
            throw new IllegalArgumentException("existing student client persona exists");
        }
        this.setStudentClientPersona(studentClientPersona);
        studentClientPersona.getStudents().add(this);
        return this;
    }

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            joinColumns = @JoinColumn(name = "student_id"),
            inverseJoinColumns = @JoinColumn(name = "course_id")
    )
    @Builder.Default
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private List<Course> courses = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private Institution institution;

    public Student addInstitution(@NonNull Institution institution) {
        if (this.getInstitution() != null) {
            throw new IllegalArgumentException("student already has existing institution");
        }

        this.setInstitution(institution);
        institution.getStudents().add(this);
        return this;
    }

    @OneToMany(mappedBy = "student", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @Builder.Default
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private List<LessonHomeworkCompletion> lessonHomeworkCompletions = new ArrayList<>();

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            joinColumns = @JoinColumn(name = "student_id"),
            inverseJoinColumns = @JoinColumn(name = "outlet_id")
    )

    @Builder.Default
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private List<Outlet> outlets = new ArrayList<>();

    public Student addOutlet(@NonNull Outlet outlet) {
        this.getOutlets().add(outlet);
        outlet.getStudents().add(this);
        return this;
    }

    public Student addOutlets(@NonNull List<Outlet> outlets) {
        assert Collections.disjoint(outlets, this.getOutlets());

        this.getOutlets().addAll(outlets);
        outlets.forEach(outlet -> outlet.getStudents().add(this));
        return this;
    }

    @Column(nullable = false)
    private String tenantId;
}
