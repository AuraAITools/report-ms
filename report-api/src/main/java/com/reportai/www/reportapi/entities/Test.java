package com.reportai.www.reportapi.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "Tests")
@Builder
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Test extends BaseEntity {

    private String name;

    @ManyToOne(fetch = FetchType.LAZY)
    private TestGroup testGroup;

    @OneToMany(mappedBy = "test", fetch = FetchType.LAZY)
    private List<TestResult> testResults;

}
