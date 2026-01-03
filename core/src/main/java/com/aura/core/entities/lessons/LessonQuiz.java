package com.aura.core.entities.lessons;

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
public class LessonQuiz {

    private String id;
    private Long version;
    private Instant createdAt;
    private String createdBy;
    private Instant updatedAt;
    private String updatedBy;
    private String tenantId;
    private int score;

    private int maxScore = 100;

    private Lesson lesson;

    private Set<LessonQuizStudentLessonLink> lessonQuizStudentLessonLinks = new HashSet<>();

}
