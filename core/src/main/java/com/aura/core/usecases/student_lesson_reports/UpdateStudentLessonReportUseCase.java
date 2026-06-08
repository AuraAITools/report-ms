package com.aura.core.usecases.student_lesson_reports;

import com.aura.core.entities.lessons.LessonParticipationReview;
import com.aura.core.entities.links.StudentLessonReport;
import java.util.List;
import org.jspecify.annotations.Nullable;

public interface UpdateStudentLessonReportUseCase {

    @Nullable StudentLessonReport update(String id,
                                         @Nullable Integer homeworkCompletion,
                                         LessonParticipationReview.@Nullable PARTICIPATION_TYPE participationType,
                                         @Nullable List<TopicProficiencyUpdate> topicProficiencyUpdates);

    record TopicProficiencyUpdate(String id, Integer rating) {
    }
}
