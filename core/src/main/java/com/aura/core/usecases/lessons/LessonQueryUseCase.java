package com.aura.core.usecases.lessons;

import com.aura.core.entities.lessons.Lesson;
import java.util.List;
import java.util.UUID;
import org.jspecify.annotations.Nullable;

public interface LessonQueryUseCase {
    @Nullable Lesson findById(UUID id);

    List<Lesson> findAllByCourseId(String courseId);

    List<Lesson> findAllByOutletId(String outletId);

    List<Lesson> findAllByEducatorId(String educatorId);

    List<Lesson> findAllByStudentId(String studentId);
}
