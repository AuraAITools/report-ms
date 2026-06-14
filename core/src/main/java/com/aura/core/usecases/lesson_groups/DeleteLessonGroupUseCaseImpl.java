package com.aura.core.usecases.lesson_groups;

import com.aura.core.repositories.LessonGroupRepository;
import java.util.UUID;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class DeleteLessonGroupUseCaseImpl implements DeleteLessonGroupUseCase {
    private final LessonGroupRepository lessonGroupRepository;

    @Override
    public void deleteById(String id) {
        lessonGroupRepository.deleteById(UUID.fromString(id));
    }
}
