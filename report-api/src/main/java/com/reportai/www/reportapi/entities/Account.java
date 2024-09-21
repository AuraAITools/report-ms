package com.reportai.www.reportapi.entities;

import jakarta.persistence.*;

import java.util.Set;
import java.util.UUID;

@Entity
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID Id;

    @Column(unique = true)
    private UUID userId;

    @OneToMany(mappedBy = "account", cascade = CascadeType.ALL)
    private Set<Student> students;

    @OneToMany(mappedBy = "account", cascade = CascadeType.ALL)
    private Set<Institution> institutions;

    @OneToMany(mappedBy = "account", cascade = CascadeType.ALL)
    private Set<Educator> educators;
}
