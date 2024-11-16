package com.reportai.www.reportapi.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "Subjects")
@Builder
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Subject extends BaseEntity {
    private String name;
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID Id;

    @ManyToOne
    @JsonIgnore
    private Course course;

    @OneToMany(mappedBy = "subject" ,cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Lesson> lessons;


    @JsonIgnore
    @ManyToMany
    private Set<Student> students;

    @JsonIgnore
    @ManyToMany
    private List<Educator> educators;

    @ManyToMany(mappedBy = "subjects")
    @JsonIgnore
    private List<TestGroup> testGroups;

    @OneToMany(mappedBy = "subject")
    private List<TestResult> testResults;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "modified_at")
    private LocalDateTime modifiedAt;
    @PrePersist
    private void onCreate() {
        LocalDateTime now = LocalDateTime.now();
        this.createdAt = now;
        this.modifiedAt = now;
    }

    @PreUpdate
    private void onUpdate() {
        this.modifiedAt = LocalDateTime.now();
    }
}
