package com.aura.details.tables.tests;

import com.aura.details.tables.base.TenantBaseTable;
import com.aura.details.tables.links.SubjectTestGroupLinkTable;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
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
@Table(name = "TestGroups")
public class TestGroupTable extends TenantBaseTable {

    @OneToMany(mappedBy = "testGroup", fetch = FetchType.LAZY)
    @Builder.Default
    @ToString.Exclude
    private Set<TestTable> tests = new HashSet<>();


    @OneToMany(mappedBy = "testGroup", fetch = FetchType.LAZY)
    @Builder.Default
    private Set<SubjectTestGroupLinkTable> subjectTestGroupLinks = new HashSet<>();


}
