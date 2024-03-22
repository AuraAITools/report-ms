package com.reportai.www.reportapi.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

@Entity
@Table(name = "Users")
public class User extends BaseEntitySupport {

    private String email;


    // TODO: this is supposed to be an enum
    private String role;

    private String name;
}
