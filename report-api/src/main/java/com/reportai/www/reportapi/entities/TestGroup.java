package com.reportai.www.reportapi.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

import java.util.List;

@Entity
@Table(name = "TestGroups")
public class TestGroup extends BaseEntitySupport{

    @ManyToMany
    private List<Class> classes;

    @OneToMany
    private List<Test> tests;

    @OneToOne
    private Institution institution;
}
