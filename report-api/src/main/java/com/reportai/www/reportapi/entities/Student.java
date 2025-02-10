package com.reportai.www.reportapi.entities;

import com.reportai.www.reportapi.entities.personas.StudentClientPersona;
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
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
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
    private Level level;

    @ManyToMany(mappedBy = "students", fetch = FetchType.LAZY)
    private List<Subject> subjects;

    @OneToMany(mappedBy = "student", fetch = FetchType.LAZY)
    private List<TestResult> testResults;

    @OneToOne(mappedBy = "student", fetch = FetchType.LAZY)
    private StudentReport studentReport;

    @ManyToMany(mappedBy = "students", fetch = FetchType.LAZY)
    private List<Lesson> lessons;

//    @ManyToMany(mappedBy = "students", fetch = FetchType.LAZY)
//    private List<Account> accounts;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(nullable = false)
    private StudentClientPersona studentClientPersona;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            joinColumns = @JoinColumn(name = "student_id"),
            inverseJoinColumns = @JoinColumn(name = "course_id")
    )
    private List<Course> courses;

    @ManyToOne(fetch = FetchType.LAZY)
    private Institution institution;

    @ManyToMany(mappedBy = "students", fetch = FetchType.LAZY)
    private List<Outlet> outlets;

    @Column(nullable = false)
    private String tenantId;
}
