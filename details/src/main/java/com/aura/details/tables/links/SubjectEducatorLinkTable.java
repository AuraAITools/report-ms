package com.aura.details.tables.links;

import com.aura.details.tables.base.TenantBaseTable;
import com.aura.details.tables.educators.EducatorTable;
import com.aura.details.tables.subjects.SubjectTable;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
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
@Table(name = "SubjectEducatorLinks",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_subject_educator",
                        columnNames = {"subject_id", "educator_id"}
                )
        })
public class SubjectEducatorLinkTable extends TenantBaseTable {

    @ManyToOne(fetch = FetchType.LAZY)
    @ToString.Exclude
    @JoinColumn(name = "educator_id")
    private EducatorTable educator;

    @ManyToOne(fetch = FetchType.LAZY)
    @ToString.Exclude
    @JoinColumn(name = "subject_id")
    private SubjectTable subject;
}
