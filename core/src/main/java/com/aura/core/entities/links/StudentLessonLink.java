package com.aura.core.entities.links;

import com.aura.core.entities.lessons.LessonPlan;
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
public class StudentLessonLink {

    private String id;
    private Long version;
    private Instant createdAt;
    private String createdBy;
    private Instant updatedAt;
    private String updatedBy;
    private String tenantId;
    private String studentId;

    private String lessonId;

    private LessonPlan lessonPlan;

    private STATE state;

    private ATTENDANCE_STATE attendanceState;

    public enum STATE {
        REGISTERED,
        UNREGISTERED
    }

    public enum ATTENDANCE_STATE {
        UNFILLED,
        ATTENDED,
        NOT_ATTENDED
    }
}
