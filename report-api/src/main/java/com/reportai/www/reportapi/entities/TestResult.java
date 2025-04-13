package com.reportai.www.reportapi.entities;

import com.reportai.www.reportapi.entities.base.TenantAwareBaseEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

@Entity
@Getter
@Setter
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "TestResults")
public class TestResult extends TenantAwareBaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @ToString.Exclude
    private Test test;

    @ManyToOne(fetch = FetchType.LAZY)
    @ToString.Exclude
    private Student student;

    @ManyToOne(fetch = FetchType.LAZY)
    @ToString.Exclude
    private Subject subject;


}
