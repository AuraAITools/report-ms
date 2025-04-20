package com.reportai.www.reportapi.entities.attachments;

import com.reportai.www.reportapi.entities.Material;
import com.reportai.www.reportapi.entities.base.AttachmentTenantAwareBaseEntityTemplate;
import com.reportai.www.reportapi.entities.lessons.LessonPlan;
import jakarta.persistence.Entity;
import org.hibernate.envers.Audited;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import java.util.Collection;
import lombok.AllArgsConstructor;
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
@Table(name = "LessonPlanMaterialsAttachments", uniqueConstraints = {
        @UniqueConstraint(
                name = "uk_lesson_plan_material",
                columnNames = {"lesson_plan_id", "material_id"}
        )
})
public class LessonPlanMaterialsAttachment extends AttachmentTenantAwareBaseEntityTemplate<LessonPlan, Material, LessonPlanMaterialsAttachment> {
    @ManyToOne(fetch = FetchType.LAZY)
    @ToString.Exclude
    @JoinColumn(name = "lesson_plan_id")
    private LessonPlan lessonPlan;

    @ManyToOne(fetch = FetchType.LAZY)
    @ToString.Exclude
    @JoinColumn(name = "material_id")
    private Material material;

    @Override
    public LessonPlan getFirstEntity() {
        return lessonPlan;
    }

    @Override
    public Material getSecondEntity() {
        return material;
    }

    @Override
    public Collection<LessonPlanMaterialsAttachment> getFirstEntityAttachments() {
        return lessonPlan.getLessonPlanMaterialsAttachments() != null ? lessonPlan.getLessonPlanMaterialsAttachments() : null;
    }

    @Override
    public Collection<LessonPlanMaterialsAttachment> getSecondEntityAttachments() {
        return material.getLessonPlanMaterialsAttachments() != null ? material.getLessonPlanMaterialsAttachments() : null;
    }

    @Override
    public LessonPlanMaterialsAttachment create(LessonPlan entity1, Material entity2) {
        return LessonPlanMaterialsAttachment
                .builder()
                .lessonPlan(entity1)
                .material(entity2)
                .build();
    }
}
