package com.aura.core.usecases.lessons;

import com.aura.core.repositories.LessonRepository;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class DeleteLessonUseCaseImpl implements DeleteLessonUseCase {
    private final LessonRepository lessonRepository;

    @Override
    public void deleteById(String id) {
        lessonRepository.deleteById(id);
    }
}
