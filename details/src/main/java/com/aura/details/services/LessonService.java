package com.aura.details.services;

import com.aura.core.entities.lessons.Lesson;
import com.aura.core.entities.links.EducatorLessonLink;
import com.aura.core.usecases.lessons.AddEducatorsToLessonUseCase;
import com.aura.core.usecases.lessons.CreateLessonUseCase;
import com.aura.core.usecases.lessons.LinkLessonsToCourseUseCase;
import jakarta.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.Nullable;


import static java.util.Optional.ofNullable;

@RequiredArgsConstructor
public class LessonService {
    private final CreateLessonUseCase createLessonUseCase;
    private final AddEducatorsToLessonUseCase addEducatorsToLessonUseCase;
    private final LinkLessonsToCourseUseCase linkLessonsToCourseUseCase;


    /**
     * Create a standalone lesson without a parent course or a roombooking
     *
     * @param lesson
     * @return
     */
//    @Transactional(isolation = Isolation.SERIALIZABLE)
    public Lesson createStandaloneLesson(Lesson lesson, String outletId) {
        return createLessonUseCase.createStandaloneLesson(lesson, outletId);
    }

    public List<EducatorLessonLink> addEducatorsToLesson(String lessonId, List<String> educatorIds, String tenantId) {
        return addEducatorsToLessonUseCase.addEducatorsToLessonUseCase(lessonId, educatorIds, tenantId);
    }

    @Transactional
    public Lesson createLesson(Lesson lesson, String outletId, String tenantId, @Nullable String courseId, List<String> educatorIds) {
        Lesson createdLesson = createLessonUseCase.createStandaloneLesson(lesson, outletId);
        // TODO: link lesson to course
        ofNullable(courseId).ifPresent(cid -> this.linkLessonToCourse(List.of(createdLesson.getId()), cid, tenantId));
        // TODO: add educators to lesson
        if (!educatorIds.isEmpty()) {
            List<EducatorLessonLink> educatorLessonLinks = addEducatorsToLessonUseCase.addEducatorsToLessonUseCase(createdLesson.getId(), educatorIds, tenantId);
            createdLesson.setEducatorLessonLinks(educatorLessonLinks.stream().collect(Collectors.toUnmodifiableSet()));
        }
        // TODO: add students to lesson
        // TODO: add lesson level
        // TODO: add lesson subjects
        // TODO: attach materials
        // TODO: attach lesson plan
        // TODO: attach lesson review
        // TODO: add many to many relationship for lesson and level
        // TODO: add many to many relationship for lesson and subject

        return createdLesson;
    }

    @Transactional
    public List<Lesson> linkLessonToCourse(List<String> lessonIds, String courseId, String tenantId) {
        return linkLessonsToCourseUseCase.linkLessonsToCourseUseCase(lessonIds, courseId, tenantId);
    }

}
