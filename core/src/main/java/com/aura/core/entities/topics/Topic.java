package com.aura.core.entities.topics;

import com.aura.core.entities.links.LessonObjectiveTopicLink;
import com.aura.core.entities.links.LessonPlanTopicLink;
import com.aura.core.entities.links.MaterialTopicLink;
import com.aura.core.entities.links.TopicSubjectLink;
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
public class Topic {

    private String id;
    private Long version;
    private Instant createdAt;
    private String createdBy;
    private Instant updatedAt;
    private String updatedBy;
    private String tenantId;
    private String name;

    private Set<MaterialTopicLink> materialTopicLinks = new HashSet<>();

    private Set<LessonObjectiveTopicLink> lessonObjectiveTopicLinks = new HashSet<>();

    private Set<TopicSubjectLink> topicSubjectLinks = new HashSet<>();

    private Set<LessonPlanTopicLink> lessonPlanTopicLinks = new HashSet<>();

}
