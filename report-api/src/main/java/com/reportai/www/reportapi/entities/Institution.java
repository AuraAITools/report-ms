package com.reportai.www.reportapi.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
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

    @jakarta.persistence.Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID Id;

    @ManyToMany
    @JsonIgnore
    private Set<Student> students;

    @ManyToMany
    @JsonIgnore
    private Set<Educator> educators;

    @OneToMany(mappedBy = "institution", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Course> courses;

    @OneToMany(mappedBy = "institution", fetch = FetchType.EAGER)
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

    @OneToMany(mappedBy = "institution", cascade = CascadeType.ALL)
    private List<Timeline> timelines;

    @ManyToOne
    private Account account;

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
