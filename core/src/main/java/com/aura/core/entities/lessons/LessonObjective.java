package com.aura.core.entities.lessons;

import com.aura.core.entities.links.LessonObjectiveTopicLink;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;
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
public class LessonObjective {

    private String id;
    private Long version;
    private Instant createdAt;
    private String createdBy;
    private Instant updatedAt;
    private String updatedBy;
    private String tenantId;
    private Lesson lesson;
    private Set<LessonObjectiveTopicLink> lessonObjectiveTopicLinks = new HashSet<>();

}
