package com.reportai.www.reportapi.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.CascadeType;
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
@Table(name = "Institutions")
@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Institution {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID Id;

    private String name;

    private String email;

    private UUID userId;

    @ManyToMany
    @JsonIgnore
    private Set<Student> students;

    @ManyToMany
    @JsonIgnore
    private Set<Parent> parents;

    @ManyToMany
    @JsonIgnore
    private Set<Educator> educators;

    @OneToMany(mappedBy = "institution", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Course> courses;

    @OneToMany(mappedBy = "institution")
    @JsonIgnore
    private List<Topic> topics;

    @OneToMany(mappedBy = "institution")
    @JsonIgnore
    private List<Invoice> invoices;

    @OneToMany(mappedBy = "institution")
    @JsonIgnore
    private List<TestGroup> testGroups;

    @OneToMany(mappedBy = "institution")
    @JsonIgnore
    private List<Material> materials;

    @OneToMany(mappedBy = "institution",cascade = CascadeType.ALL)
    private List<Timeline> timelines;

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
