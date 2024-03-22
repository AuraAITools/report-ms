package com.reportai.www.reportapi.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

import java.util.List;

@Entity
@Table(name = "Classes")
public class Class extends BaseEntitySupport {

    private String name;

    @OneToOne
    private Institution institution;

    @ManyToMany
    private List<Student> students;

    @ManyToMany
    private List<Educator> educators;

    @ManyToMany
    private List<TestGroup> testGroups;

    @OneToMany
    private List<Subject> subjects;
}
