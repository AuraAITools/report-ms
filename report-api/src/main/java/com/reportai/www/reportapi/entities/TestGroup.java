package com.reportai.www.reportapi.entities;

import com.reportai.www.reportapi.entities.attachments.SubjectTestGroupAttachment;
import com.reportai.www.reportapi.entities.base.TenantAwareBaseEntity;
import jakarta.persistence.Entity;
import org.hibernate.envers.Audited;
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

@Entity
@Audited
@Getter
@Setter
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "TestGroups")
public class TestGroup extends TenantAwareBaseEntity {

    @OneToMany(mappedBy = "testGroup", fetch = FetchType.LAZY)
    @Builder.Default
    @ToString.Exclude
    private Set<Test> tests = new HashSet<>();


    @OneToMany(mappedBy = "testGroup", fetch = FetchType.LAZY)
    @Builder.Default
    private Set<SubjectTestGroupAttachment> subjectTestGroupAttachments = new HashSet<>();


}
