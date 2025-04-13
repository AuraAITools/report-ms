package com.reportai.www.reportapi.entities.attachments;

import com.reportai.www.reportapi.entities.Material;
import com.reportai.www.reportapi.entities.base.AttachmentTenantAwareBaseEntityTemplate;
import com.reportai.www.reportapi.entities.lessons.Lesson;
import com.reportai.www.reportapi.entities.lessons.LessonPlan;
import jakarta.persistence.Entity;
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
@Getter
@Setter
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@Table(
        name = "MaterialLessonAttachments",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_material_lesson",
                        columnNames = {"material_id", "lesson_id"}
                )
        }
)
public class MaterialLessonAttachment extends AttachmentTenantAwareBaseEntityTemplate<Material, Lesson, MaterialLessonAttachment> {

    @ManyToOne(fetch = FetchType.LAZY)
    @ToString.Exclude
    @JoinColumn(name = "material_id")
    private Material material;

    @ManyToOne(fetch = FetchType.LAZY)
    @ToString.Exclude
    @JoinColumn(name = "lesson_id")
    private Lesson lesson;

    @ManyToOne(fetch = FetchType.LAZY)
    @ToString.Exclude
    private LessonPlan lessonPlan;

    @Override
    public Material getFirstEntity() {
        return material;
    }

    @Override
    public Lesson getSecondEntity() {
        return lesson;
    }

    @Override
    public Collection<MaterialLessonAttachment> getFirstEntityAttachments() {
        return material != null ? material.getMaterialLessonAttachments() : null;
    }

    @Override
    public Collection<MaterialLessonAttachment> getSecondEntityAttachments() {
        return lesson != null ? lesson.getMaterialLessonAttachments() : null;
    }

    @Override
    public MaterialLessonAttachment create(Material entity1, Lesson entity2) {
        return MaterialLessonAttachment
                .builder()
                .material(entity1)
                .lesson(entity2)
                .build();
    }
}
