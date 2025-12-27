package com.aura.core.entities.materials;

import com.aura.core.entities.links.LessonPlanMaterialLink;
import com.aura.core.entities.links.MaterialLessonLink;
import com.aura.core.entities.links.MaterialTopicLink;
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
public class Material {

    private UUID id;
    private Long version;
    private Instant createdAt;
    private String createdBy;
    private Instant updatedAt;
    private String updatedBy;
    private String tenantId;
    private String name;

    private String fileUrl;
    private Set<MaterialTopicLink> materialTopicLinks = new HashSet<>();
    private Set<MaterialLessonLink> materialLessonLinks = new HashSet<>();
    private Set<LessonPlanMaterialLink> lessonPlanMaterialsLinks = new HashSet<>();

}
