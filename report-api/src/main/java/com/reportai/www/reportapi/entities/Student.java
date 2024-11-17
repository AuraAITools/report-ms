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
import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "Students")
@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Student extends BaseEntity {

    @Column(nullable = false)
    private String name;

    @Email
    @Column(nullable = false, unique = true)
    private String email;

    private LocalDateTime dateOfBirth;

    private String currentSchool;

    private String currentLevel;

    @ManyToMany(mappedBy = "students", fetch = FetchType.EAGER)
    private Set<Institution> institutions;

    @ManyToMany(mappedBy = "students", fetch = FetchType.LAZY)
    private Set<Subject> subjects;

    @OneToMany(mappedBy = "student", fetch = FetchType.LAZY)
    private List<TestResult> testResults;

    @OneToOne(mappedBy = "student", fetch = FetchType.LAZY)
    private StudentReport studentReport;

    @ManyToMany(mappedBy = "students", fetch = FetchType.LAZY)
    private Set<Lesson> lessons;

    @ManyToOne
    private Account account;

}
