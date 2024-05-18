package com.reportai.www.reportapi.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
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

import java.sql.Time;
import java.time.LocalDateTime;
import java.util.List;
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

    @OneToOne
    @JsonBackReference
    private User user;

    @ManyToMany
    @JsonIgnore
    private List<Student> students;

    @ManyToMany
    @JsonIgnore
    private List<Parent> parents;

    @ManyToMany
    @JsonIgnore
    private List<Educator> educators;

    @OneToMany(cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Class> classes;

    @OneToMany
    @JsonIgnore
    private List<Topic> topics;

    @OneToMany
    @JsonIgnore
    private List<Invoice> invoices;

    @OneToMany
    @JsonIgnore
    private List<TestGroup> testGroups;

    @OneToMany
    @JsonIgnore
    private List<Material> materials;

    @OneToMany(cascade = CascadeType.ALL)
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
