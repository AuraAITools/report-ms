package com.aura.core.entities.links;

import com.aura.core.entities.lessons.LessonObjective;
import com.aura.core.entities.topics.Topic;
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
public class LessonObjectiveTopicLink {

    private String id;
    private Long version;
    private Instant createdAt;
    private String createdBy;
    private Instant updatedAt;
    private String updatedBy;
    private String tenantId;
    private Topic topic;

    private LessonObjective lessonObjective;

}
