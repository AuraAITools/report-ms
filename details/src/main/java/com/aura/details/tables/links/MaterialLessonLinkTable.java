package com.aura.details.tables.links;

import com.aura.details.tables.base.TenantBaseTable;
import com.aura.details.tables.lessons.LessonPlanTable;
import com.aura.details.tables.lessons.LessonTable;
import com.aura.details.tables.materials.MaterialTable;
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
@Table(
        name = "MaterialLessonLinks",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_material_lesson",
                        columnNames = {"material_id", "lesson_id"}
                )
        }
)
public class MaterialLessonLinkTable extends TenantBaseTable {

    @ManyToOne(fetch = FetchType.LAZY)
    @ToString.Exclude
    @JoinColumn(name = "material_id")
    private MaterialTable material;

    @ManyToOne(fetch = FetchType.LAZY)
    @ToString.Exclude
    @JoinColumn(name = "lesson_id")
    private LessonTable lesson;

    @ManyToOne(fetch = FetchType.LAZY)
    @ToString.Exclude
    private LessonPlanTable lessonPlan;

}
