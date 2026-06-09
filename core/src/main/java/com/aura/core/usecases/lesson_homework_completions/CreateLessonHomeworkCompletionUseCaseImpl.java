package com.aura.core.usecases.lesson_homework_completions;

import com.aura.core.entities.lessons.LessonHomeworkCompletion;
import com.aura.core.repositories.LessonHomeworkCompletionRepository;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.Nullable;

@RequiredArgsConstructor
public class CreateLessonHomeworkCompletionUseCaseImpl implements CreateLessonHomeworkCompletionUseCase {
    private final LessonHomeworkCompletionRepository lessonHomeworkCompletionRepository;

    @Override
    public LessonHomeworkCompletion create(String studentLessonReportId, @Nullable Integer completion, String tenantId) {
        LessonHomeworkCompletion entity = LessonHomeworkCompletion.builder()
                .tenantId(tenantId)
                .studentLessonReportId(studentLessonReportId)
                .completion(completion)
                .build();
        return lessonHomeworkCompletionRepository.save(entity);
    }
}
