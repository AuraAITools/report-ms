package com.aura.details.springdata.tables.lessons;

import com.aura.details.springdata.tables.base.TenantBaseTable;
import com.aura.details.springdata.tables.links.StudentLessonLinkTable;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.hibernate.envers.Audited;

@Entity
@Audited
@Getter
@Setter
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "StudentLessonAttendances")
public class StudentLessonAttendanceTable extends TenantBaseTable {

    @OneToOne(fetch = FetchType.LAZY)
    private StudentLessonLinkTable studentLessonLink;

    private boolean attended;
}
