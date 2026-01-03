package com.aura.core.entities.links;

import com.aura.core.entities.lessons.LessonPlan;
import com.aura.core.entities.materials.Material;
import java.time.Instant;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.Builder;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LessonPlanMaterialLink {
    private String id;
    private Long version;
    private Instant createdAt;
    private String createdBy;
    private Instant updatedAt;
    private String updatedBy;
    private String tenantId;
    private LessonPlan lessonPlan;

    private Material material;
}
