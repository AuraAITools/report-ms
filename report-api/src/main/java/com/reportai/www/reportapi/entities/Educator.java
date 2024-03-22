package com.reportai.www.reportapi.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

import java.util.List;

@Entity
@Table(name = "Educators")
public class Educator extends BaseEntitySupport{
    @OneToOne
    private User user;

    @ManyToMany
    private List<Institution> institutions;

    @ManyToMany
    private List<Class> classes;
}
