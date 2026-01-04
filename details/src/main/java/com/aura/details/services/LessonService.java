package com.aura.details.services;

import com.aura.core.entities.lessons.Lesson;
import com.aura.core.usecases.lessons.CreateLessonUseCase;
import java.util.UUID;

public class LessonService {
    CreateLessonUseCase createLessonUseCase;

    public LessonService(CreateLessonUseCase createLessonUseCase) {
        this.createLessonUseCase = createLessonUseCase;
    }


    /**
     * Create a standalone lesson without a parent course or a roombooking
     *
     * @param lesson
     * @return
     */
//    @Transactional(isolation = Isolation.SERIALIZABLE)
    public Lesson createStandaloneLesson(Lesson lesson, UUID outletId) {
        return createLessonUseCase.createStandaloneLesson(lesson, outletId.toString());
    }
}
