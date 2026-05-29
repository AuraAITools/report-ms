package com.aura.core.entities.lessons;

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
public class LessonPostponementRequest {

    private String id;
    private Long version;
    private Instant createdAt;
    private String createdBy;
    private Instant updatedAt;
    private String updatedBy;
    private String tenantId;
    private String reasoning = "";
    private String approvedBy;
    private APPROVAL_STATUS status = APPROVAL_STATUS.PENDING;
    private Instant postponedLessonStartTimestamptz;
    private Instant postponedLessonEndTimestamptz;
    private String lessonId;

    public enum APPROVAL_STATUS {
        APPROVED,
        REJECTED,
        PENDING
    }

}
