package com.aura.details.springdata.models.tests;

import com.aura.details.springdata.models.base.TenantBaseModel;
import com.aura.details.springdata.models.links.SubjectTestGroupLinkModel;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
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
@Table(name = "TestGroups")
public class TestGroupModel extends TenantBaseModel {

    @OneToMany(mappedBy = "testGroup", fetch = FetchType.LAZY)
    @Builder.Default
    @ToString.Exclude
    private Set<TestModel> tests = new HashSet<>();


    @OneToMany(mappedBy = "testGroup", fetch = FetchType.LAZY)
    @ToString.Exclude
    @Builder.Default
    private Set<SubjectTestGroupLinkModel> subjectTestGroupLinks = new HashSet<>();


}
