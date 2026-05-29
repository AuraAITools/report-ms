package com.aura.core.entities.lessons;

import java.time.Instant;
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
public class LessonParticipationReview {

    private String id;
    private Long version;
    private Instant createdAt;
    private String createdBy;
    private Instant updatedAt;
    private String updatedBy;
    private String tenantId;
    private PARTICIPATION_TYPE participationType;
    private String studentLessonReportId;

    // TODO: TO fill
    public enum PARTICIPATION_TYPE {
        ATTENTIVE,
        QUIET,
        CURIOUS,
        UNFILLED
    }

}
