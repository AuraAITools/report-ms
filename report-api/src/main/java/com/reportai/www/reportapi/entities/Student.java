package com.reportai.www.reportapi.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import java.time.LocalDateTime;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity
@Data
@EqualsAndHashCode(callSuper = true)
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "Students")
public class Student extends BaseEntity {

    @Column(nullable = false)
    private String name;

    @Email
    @Column(nullable = false, unique = true)
    private String email;

    private LocalDateTime dateOfBirth;

    private String currentSchool;

    @ManyToOne(fetch = FetchType.EAGER)
    private Level level;

    @ManyToMany(mappedBy = "students", fetch = FetchType.EAGER)
    private List<Institution> institutions;

    @ManyToMany(mappedBy = "students", fetch = FetchType.LAZY)
    private List<Subject> subjects;

    @OneToMany(mappedBy = "student", fetch = FetchType.LAZY)
    private List<TestResult> testResults;

    @OneToOne(mappedBy = "student", fetch = FetchType.LAZY)
    private StudentReport studentReport;

    @ManyToMany(mappedBy = "students", fetch = FetchType.LAZY)
    private List<Lesson> lessons;

    @ManyToMany(fetch = FetchType.LAZY, mappedBy = "students")
    private List<Account> accounts;

    @ManyToMany(fetch = FetchType.LAZY)
    private List<Course> courses;

    @ManyToOne(fetch = FetchType.LAZY)
    private Institution institution;

    @Column(nullable = false)
    private String tenantId;
}
