package com.reportai.www.reportapi.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import lombok.*;

import java.util.Set;
import java.util.UUID;

@Entity
@Builder
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Account extends BaseEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID Id;

    @Column(unique = true)
    private String userId;

    @Column(unique = true)
    @Email
    private String email;

    @Column
    private String firstName;

    @Column
    private String lastName;

    @OneToMany(mappedBy = "account", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Set<Student> students;

    @OneToMany(mappedBy = "account", fetch = FetchType.LAZY,  cascade = CascadeType.ALL)
    private Set<Institution> institutions;

    @OneToMany(mappedBy = "account", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Set<Educator> educators;
}
