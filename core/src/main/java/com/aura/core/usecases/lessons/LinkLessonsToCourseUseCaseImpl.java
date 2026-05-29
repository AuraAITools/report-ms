package com.aura.core.usecases.lessons;

import com.aura.core.entities.courses.Course;
import com.aura.core.entities.lessons.Lesson;
import com.aura.core.repositories.CourseRepository;
import com.aura.core.repositories.LessonRepository;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;


import static java.util.Optional.ofNullable;

@RequiredArgsConstructor
public class LinkLessonsToCourseUseCaseImpl implements LinkLessonsToCourseUseCase {
    private final LessonRepository lessonRepository;
    private final CourseRepository courseRepository;

    @Override
    public List<Lesson> linkLessonsToCourseUseCase(List<String> lessonIds, String courseId, String tenantId) {
        // TODO: check what happens when one of the ids cannot be found? does it throw exception?
        List<Lesson> lessons = lessonRepository.findAllById(lessonIds.stream().map(UUID::fromString).toList());
        Course course = ofNullable(courseRepository.findById(courseId)).orElseThrow(() -> new RuntimeException(" course cannot be found"));
        lessons.forEach(lesson -> lesson.setCourseId(course.getId()));
        return lessonRepository.updateLessons(lessons);
    }
}
