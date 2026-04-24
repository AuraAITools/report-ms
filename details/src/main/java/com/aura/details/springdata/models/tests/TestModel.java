package com.aura.details.springdata.models.tests;

import com.aura.details.springdata.models.base.TenantBaseModel;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.util.HashSet;
import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.Builder;
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
@Table(name = "Tests")
public class TestModel extends TenantBaseModel {

    private String name;

    @ManyToOne(fetch = FetchType.LAZY)
    @ToString.Exclude
    private TestGroupModel testGroup;

    @OneToMany(mappedBy = "test", fetch = FetchType.LAZY)
    @Builder.Default
    @ToString.Exclude
    private Set<TestResultModel> testResults = new HashSet<>();


}
