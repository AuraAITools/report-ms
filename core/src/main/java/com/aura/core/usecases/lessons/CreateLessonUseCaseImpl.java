package com.aura.core.usecases.lessons;

import com.aura.core.entities.lessons.Lesson;
import com.aura.core.repositories.LessonRepository;
import com.aura.core.repositories.OutletRepository;
import java.util.Optional;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class CreateLessonUseCaseImpl implements CreateLessonUseCase {
    private final LessonRepository lessonRepository;
    private final OutletRepository outletRepository;

    @Override
    public Lesson createStandaloneLesson(Lesson lesson, String outletId) {
        return Optional.ofNullable(outletRepository.findById(outletId))
                .map(outlet -> {
                    lesson.setOutlet(outlet);
                    return lessonRepository.save(lesson);
                }).orElseThrow(() -> new RuntimeException("outlet not found"));
    }
}
