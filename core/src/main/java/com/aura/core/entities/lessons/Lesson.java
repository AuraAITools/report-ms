package com.aura.core.entities.lessons;

import com.aura.core.entities.subjects.Subject;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;
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
public class Lesson {

    private String name;
    private Instant lessonStartTimestamptz;
    private Instant lessonEndTimestamptz;
    private String description = "";
    private String recap = "";
    private Subject subject;
    private Set<LessonPostponementRequest> lessonPostponementRequestTables = new HashSet<>();
    private Set<LessonCancellationRequest> lessonCancellationRequests = new HashSet<>();
    private Set<LessonQuiz> lessonQuizzTables = new HashSet<>();

    public enum LESSON_STATUS {
        COMPLETED,
        CANCELLED,
        POSTPONED,
        UPCOMING,
        ONGOING
    }

    public enum LESSON_REVIEW_STATUS {
        REVIEWED,
        NOT_REVIEWED,
    }

}
