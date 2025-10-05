package com.aura.details.tables.tests;

import com.aura.details.tables.base.TenantBaseTable;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.util.HashSet;
import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.Builder;
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
@Table(name = "Tests")
public class TestTable extends TenantBaseTable {

    private String name;

    @ManyToOne(fetch = FetchType.LAZY)
    @ToString.Exclude
    private TestGroupTable testGroup;

    @OneToMany(mappedBy = "test", fetch = FetchType.LAZY)
    @Builder.Default
    @ToString.Exclude
    private Set<TestResultTable> testResults = new HashSet<>();


}
