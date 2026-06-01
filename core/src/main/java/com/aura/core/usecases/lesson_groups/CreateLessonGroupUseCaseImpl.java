package com.aura.core.usecases.lesson_groups;

import com.aura.core.entities.lessons.Lesson;
import com.aura.core.entities.lessons.LessonGroup;
import com.aura.core.repositories.LessonGroupRepository;
import com.aura.core.repositories.LessonRepository;
import java.util.UUID;
import lombok.RequiredArgsConstructor;


import static java.util.Optional.ofNullable;

@RequiredArgsConstructor
public class CreateLessonGroupUseCaseImpl implements CreateLessonGroupUseCase {
    private final LessonGroupRepository lessonGroupRepository;
    private final LessonRepository lessonRepository;

    @Override
    public LessonGroup createLessonGroup(LessonGroup lessonGroup, String lessonId) {
        Lesson lesson = ofNullable(lessonRepository.findById(UUID.fromString(lessonId)))
                .orElseThrow(() -> new RuntimeException("lesson not found"));

        lessonGroup.setLessonId(lessonId);
        lessonGroup.setTenantId(lesson.getTenantId());
        return lessonGroupRepository.save(lessonGroup);
    }
}
