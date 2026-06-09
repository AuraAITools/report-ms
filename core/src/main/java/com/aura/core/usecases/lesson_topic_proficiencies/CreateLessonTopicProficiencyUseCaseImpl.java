package com.aura.core.usecases.lesson_topic_proficiencies;

import com.aura.core.entities.lessons.LessonTopicProficiency;
import com.aura.core.repositories.LessonTopicProficiencyRepository;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.Nullable;

@RequiredArgsConstructor
public class CreateLessonTopicProficiencyUseCaseImpl implements CreateLessonTopicProficiencyUseCase {
    private final LessonTopicProficiencyRepository lessonTopicProficiencyRepository;

    @Override
    public LessonTopicProficiency create(String studentLessonReportId, String topicId, @Nullable Integer rating, String tenantId) {
        LessonTopicProficiency entity = LessonTopicProficiency.builder()
                .tenantId(tenantId)
                .studentLessonReportId(studentLessonReportId)
                .topicId(topicId)
                .rating(rating)
                .build();
        return lessonTopicProficiencyRepository.save(entity);
    }
}
