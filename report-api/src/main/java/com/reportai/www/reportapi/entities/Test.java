package com.reportai.www.reportapi.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

import java.util.List;

@Entity
@Table(name = "Tests")
public class Test extends BaseEntitySupport{

    @OneToOne
    private TestGroup testGroup;

    @OneToOne
    private Subject subject;

    @OneToOne
    private Institution institution;

    @OneToMany
    private List<TestResult> testResults;
}
