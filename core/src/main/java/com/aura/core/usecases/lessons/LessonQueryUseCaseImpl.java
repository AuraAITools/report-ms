package com.aura.core.usecases.lessons;

import com.aura.core.entities.lessons.Lesson;
import com.aura.core.repositories.LessonRepository;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.Nullable;

@RequiredArgsConstructor
public class LessonQueryUseCaseImpl implements LessonQueryUseCase {
    private final LessonRepository lessonRepository;

    @Override
    public @Nullable Lesson findById(UUID id) {
        return lessonRepository.findById(id);
    }

    @Override
    public List<Lesson> findAllByCourseId(String courseId) {
        return lessonRepository.findAllByCourseId(courseId);
    }

    @Override
    public List<Lesson> findAllByOutletId(String outletId) {
        return lessonRepository.findAllByOutletId(outletId);
    }

    @Override
    public List<Lesson> findAllByEducatorId(String educatorId) {
        return lessonRepository.findAllByEducatorId(educatorId);
    }

    @Override
    public List<Lesson> findAllByStudentId(String studentId) {
        return lessonRepository.findAllByStudentId(studentId);
    }
}
