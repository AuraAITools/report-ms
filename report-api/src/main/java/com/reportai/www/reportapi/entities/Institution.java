package com.reportai.www.reportapi.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Entity
@Builder
@Table(name = "Institutions")
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Institution {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID Id;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "modified_at")
    private LocalDateTime modifiedAt;

    @OneToOne
    private User user;

    @ManyToMany
    private List<Student> students;

    @ManyToMany
    private List<Parent> parents;

    @ManyToMany
    private List<Educator> educators;

    @OneToMany
    private List<Class> classes;

    @OneToMany
    private List<Subject> subjects;

    @OneToMany
    private List<Invoice> invoices;

    @OneToMany
    private List<TestGroup> testGroups;

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
