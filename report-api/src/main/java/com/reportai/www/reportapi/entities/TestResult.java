package com.reportai.www.reportapi.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "TestResults")
public class TestResult extends BaseEntitySupport{
    @OneToOne
    private Test test;

    @OneToOne
    private Student student;

    @OneToOne
    private Subject subject;
}
