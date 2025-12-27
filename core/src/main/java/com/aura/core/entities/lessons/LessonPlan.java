package com.aura.core.entities.lessons;

import com.aura.core.entities.links.LessonPlanMaterialLink;
import com.aura.core.entities.links.LessonPlanTopicLink;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class LessonPlan {

    private UUID id;
    private Long version;
    private Instant createdAt;
    private String createdBy;
    private Instant updatedAt;
    private String updatedBy;
    private String tenantId;
    private Lesson lesson;
    private boolean isPlanned;
    private Set<LessonPlanMaterialLink> lessonPlanMaterialsLinks = new HashSet<>();
    private Set<LessonPlanTopicLink> lessonPlanTopicLinks = new HashSet<>();

    public enum LESSON_PLAN_STATUS {
        PLANNED,
        UNPLANNED
    }

}
