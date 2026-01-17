package com.aura.core.usecases.lessons;

import com.aura.core.entities.lessons.Lesson;
import com.aura.core.repositories.LessonRepository;
import com.aura.core.repositories.OutletRepository;
import java.util.Optional;

public class CreateLessonUseCaseImpl implements CreateLessonUseCase {
    private final LessonRepository lessonRepository;
    private final OutletRepository outletRepository;

    public CreateLessonUseCaseImpl(LessonRepository lessonRepository, OutletRepository outletRepository) {
        this.lessonRepository = lessonRepository;
        this.outletRepository = outletRepository;
    }

    // TODO: add educators to lesson
    // TODO: add students to lesson
    // TODO: add lesson level
    // TODO: add lesson subjects
    // TODO: attach materials
    // TODO: attach lesson plan
    // TODO: attach lesson review
    @Override
    public Lesson createStandaloneLesson(Lesson lesson, String outletId) {
        return Optional.ofNullable(outletRepository.findById(outletId))
                .map(outlet -> {
                    lesson.setOutlet(outlet);
                    return lessonRepository.save(lesson);
                }).orElseThrow(() -> new RuntimeException("outlet not found"));
    }
}
