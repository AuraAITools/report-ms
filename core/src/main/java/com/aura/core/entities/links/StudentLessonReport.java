package com.aura.core.entities.links;

import com.aura.core.entities.lessons.LessonHomeworkCompletion;
import com.aura.core.entities.lessons.LessonParticipationReview;
import com.aura.core.entities.lessons.LessonTopicProficiency;
import java.time.Instant;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class StudentLessonReport {

    private String id;
    private Long version;
    private Instant createdAt;
    private String createdBy;
    private Instant updatedAt;
    private String updatedBy;
    private String tenantId;

    private String studentLessonLinkId;

    private LessonHomeworkCompletion lessonHomeworkCompletion;

    private LessonParticipationReview lessonParticipationReview;

    private List<LessonTopicProficiency> lessonTopicProficiencies;
}
