package com.aura.details.tables.subjects;

import com.aura.details.tables.base.BaseTable;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
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
@Table(name = "SubjectLevelLinkTemplates", uniqueConstraints = {
        @UniqueConstraint(
                name = "uk_subject_level_name",
                columnNames = {"subject_name", "level_name"}
        )
})
public class SubjectLevelLinkTemplateTable extends BaseTable {

    @Column(nullable = false)
    private String subjectName;

    @Column(nullable = false)
    private String levelName;
}
