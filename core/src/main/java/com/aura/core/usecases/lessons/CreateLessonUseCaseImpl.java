package com.aura.core.usecases.lessons;

import com.aura.core.entities.lessons.Lesson;
import com.aura.core.exceptions.ResourceNotFoundException;
import com.aura.core.repositories.CourseRepository;
import com.aura.core.repositories.LessonRepository;
import lombok.RequiredArgsConstructor;


import static java.util.Optional.ofNullable;

@RequiredArgsConstructor
public class CreateLessonUseCaseImpl implements CreateLessonUseCase {
    private final LessonRepository lessonRepository;
    private final CourseRepository courseRepository;

    @Override
    public Lesson createLessonInCourse(Lesson lesson, String courseId) {
        return ofNullable(courseRepository.findById(courseId))
                .map(course -> {
                    lesson.setOutletId(course.getOutletId());
                    lesson.setCourseId(course.getId());
                    lesson.setState(Lesson.LESSON_STATE.CREATED);
                    return lessonRepository.save(lesson);
                })
                .orElseThrow(() -> new ResourceNotFoundException("Course", courseId));
    }
}
