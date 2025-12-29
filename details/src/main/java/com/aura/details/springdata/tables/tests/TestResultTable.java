package com.aura.details.springdata.tables.tests;

import com.aura.details.springdata.tables.base.TenantBaseTable;
import com.aura.details.springdata.tables.students.StudentTable;
import com.aura.details.springdata.tables.subjects.SubjectTable;
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
import org.hibernate.envers.Audited;

@Entity
@Audited
@Getter
@Setter
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "TestResults")
public class TestResultTable extends TenantBaseTable {

    @ManyToOne(fetch = FetchType.LAZY)
    @ToString.Exclude
    private TestTable test;

    @ManyToOne(fetch = FetchType.LAZY)
    @ToString.Exclude
    private StudentTable student;

    @ManyToOne(fetch = FetchType.LAZY)
    @ToString.Exclude
    private SubjectTable subject;


}
