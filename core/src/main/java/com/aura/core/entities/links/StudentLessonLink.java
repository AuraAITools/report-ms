package com.aura.core.entities.links;

import com.aura.core.entities.lessons.Lesson;
import com.aura.core.entities.lessons.LessonHomeworkCompletion;
import com.aura.core.entities.lessons.LessonParticipationReview;
import com.aura.core.entities.lessons.LessonPlan;
import com.aura.core.entities.lessons.LessonQuizStudentLessonLink;
import com.aura.core.entities.lessons.LessonTopicProficiency;
import com.aura.core.entities.lessons.StudentLessonAttendance;
import com.aura.core.entities.students.Student;
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
public class StudentLessonLink {

    private String id;
    private Long version;
    private Instant createdAt;
    private String createdBy;
    private Instant updatedAt;
    private String updatedBy;
    private String tenantId;
    private Student student;

    private Lesson lesson;

    private LessonPlan lessonPlan;

    private LessonHomeworkCompletion lessonHomeworkCompletion;

    private StudentLessonAttendance studentLessonAttendance;

    private LessonParticipationReview lessonParticipationReview;

    private Set<LessonQuizStudentLessonLink> lessonQuizStudentLessonLinks = new HashSet<>();

    private LessonTopicProficiency lessonTopicProficiency;

}
