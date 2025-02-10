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
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

@Entity
@Data
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "Levels")
public class Level extends BaseEntity {
    private String name;

    @ManyToMany(fetch = FetchType.LAZY)
    @ToString.Exclude
    private List<Subject> subjects;

    @ManyToMany(fetch = FetchType.LAZY, mappedBy = "levels")
    @ToString.Exclude
    private List<Educator> educators;

    @OneToMany(mappedBy = "level", fetch = FetchType.LAZY)
    @ToString.Exclude
    private List<Course> courses;

    @OneToMany(mappedBy = "level", fetch = FetchType.LAZY)
    @ToString.Exclude
    private List<Student> students;

    @ManyToOne(fetch = FetchType.LAZY)
    @ToString.Exclude
    private Institution institution;

    @Column(nullable = false)
    private String tenantId;
}
