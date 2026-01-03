package com.aura.core.usecases.lessons;

import com.aura.core.entities.lessons.Lesson;
import com.aura.core.repositories.LessonRepository;
import com.aura.core.repositories.OutletRepository;

public class CreateLessonUseCaseImpl implements CreateLessonUseCase {
    private final LessonRepository lessonRepository;
    private final OutletRepository outletRepository;

    public CreateLessonUseCaseImpl(LessonRepository lessonRepository, OutletRepository outletRepository) {
        this.lessonRepository = lessonRepository;
        this.outletRepository = outletRepository;
    }

    @Override
    public Lesson createStandaloneLesson(Lesson lesson, String outletId) {
        return outletRepository.findById(outletId).map(outlet -> {
            lesson.setOutlet(outlet);
            return lessonRepository.save(lesson);
        }).orElseThrow(() -> new RuntimeException("outlet not found"));
    }
}
