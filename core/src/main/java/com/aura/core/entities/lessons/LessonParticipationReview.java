package com.aura.core.entities.lessons;

import com.aura.core.entities.links.StudentLessonLink;
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
public class LessonParticipationReview {

    private String id;
    private Long version;
    private Instant createdAt;
    private String createdBy;
    private Instant updatedAt;
    private String updatedBy;
    private String tenantId;
    private PARTICIPATION_TYPE participationType;
    private StudentLessonLink studentLessonLink;

    // TODO: TO fill
    public enum PARTICIPATION_TYPE {
        ATTENTIVE,
        QUIET,
        CURIOUS
    }

}
