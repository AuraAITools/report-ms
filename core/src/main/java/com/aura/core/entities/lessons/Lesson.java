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
public class Lesson {

    private String id;

    private Long version;

    private Instant createdAt;

    private String createdBy;

    private Instant updatedAt;

    private String updatedBy;

    private String tenantId;

    private String name;

    private Instant lessonStartTimestamptz;

    private Instant lessonEndTimestamptz;

    private String description = "";

    private LessonOutletRoomBooking lessonOutletRoomBooking;

    private String courseId;

    private String outletId;

    private LESSON_STATE state;

    public enum LESSON_STATE {
        CREATED,
        ENDED,
        CANCELLED
    }

    public enum LESSON_REVIEW_STATUS {
        REVIEWED,
        NOT_REVIEWED,
    }

}
