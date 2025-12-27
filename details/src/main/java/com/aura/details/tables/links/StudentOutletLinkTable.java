package com.aura.details.tables.links;

import com.aura.details.tables.base.TenantBaseTable;
import com.aura.details.tables.outlets.OutletTable;
import com.aura.details.tables.students.StudentTable;
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
@Table(name = "StudentOutletLinks",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_student_outlet",
                        columnNames = {"student_id", "outlet_id"}
                )
        })
public class StudentOutletLinkTable extends TenantBaseTable {
    @ManyToOne(fetch = FetchType.LAZY)
    @ToString.Exclude
    @JoinColumn(name = "student_id")
    private StudentTable student;

    @ManyToOne(fetch = FetchType.LAZY)
    @ToString.Exclude
    @JoinColumn(name = "outlet_id")
    private OutletTable outlet;

}
