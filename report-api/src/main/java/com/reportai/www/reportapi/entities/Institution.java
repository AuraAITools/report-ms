package com.reportai.www.reportapi.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

import java.util.List;

@Entity
@Table(name = "Institutions")
public class Institution extends BaseEntitySupport {
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
}
