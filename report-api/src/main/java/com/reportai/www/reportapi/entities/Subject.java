package com.reportai.www.reportapi.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
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
@Table(name = "Subjects")
public class Subject extends BaseEntity {

    private String name;

    @ManyToOne(fetch = FetchType.EAGER)
    private Course course;

    // NOTE: deleting Subject shouldn't delete all the lessons
    @OneToMany(mappedBy = "subject", fetch = FetchType.LAZY)
    private List<Lesson> lessons;

    @ManyToMany(fetch = FetchType.LAZY)
    private List<Student> students;

    @ManyToMany(fetch = FetchType.LAZY)
    private List<Educator> educators;

    @ManyToMany(mappedBy = "subjects", fetch = FetchType.LAZY)
    private List<TestGroup> testGroups;

    @OneToMany(mappedBy = "subject", fetch = FetchType.LAZY)
    private List<TestResult> testResults;

    @ManyToMany(mappedBy = "subjects", fetch = FetchType.LAZY)
    private List<Level> levels;

    @ManyToOne(fetch = FetchType.LAZY)
    private Institution institution;

    @Column(nullable = false)
    private String tenantId;

}
