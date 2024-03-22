package com.reportai.www.reportapi.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name = "Subjects")
public class Subject extends BaseEntitySupport{
    private String name;

}
