package com.aura.details.springdata.models.tests;

import com.aura.details.springdata.models.base.TenantBaseModel;
import com.aura.details.springdata.models.students.StudentModel;
import com.aura.details.springdata.models.subjects.SubjectModel;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
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
@EqualsAndHashCode(of = {}, callSuper = true)
@Table(name = "TestResults")
public class TestResultModel extends TenantBaseModel {

    @ManyToOne(fetch = FetchType.LAZY)
    @ToString.Exclude
    private TestModel test;

    @ManyToOne(fetch = FetchType.LAZY)
    @ToString.Exclude
    private StudentModel student;

    @ManyToOne(fetch = FetchType.LAZY)
    @ToString.Exclude
    private SubjectModel subject;


}
