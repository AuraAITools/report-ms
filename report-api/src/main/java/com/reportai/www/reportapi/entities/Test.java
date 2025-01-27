package com.reportai.www.reportapi.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity
@Data
@EqualsAndHashCode(callSuper = true)
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "Tests")
public class Test extends BaseEntity {

    private String name;

    @ManyToOne(fetch = FetchType.LAZY)
    private TestGroup testGroup;

    @OneToMany(mappedBy = "test", fetch = FetchType.LAZY)
    private List<TestResult> testResults;

    @ManyToOne(fetch = FetchType.LAZY)
    private Institution institution;

    @Column(nullable = false)
    private String tenantId;
}
