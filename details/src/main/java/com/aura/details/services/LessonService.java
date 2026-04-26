package com.aura.details.services;

import com.aura.core.clients.RelationshipAuthorizationService;
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
    private final RelationshipAuthorizationService relationshipService;

    public Lesson createStandaloneLesson(Lesson lesson, String outletId) {
        Lesson created = createLessonUseCase.createStandaloneLesson(lesson, outletId);
        relationshipService.writeRelationship("lesson", created.getId(),
                "parent_outlet", "outlet", outletId);
        return created;
    }

    public List<EducatorLessonLink> addEducatorsToLesson(String lessonId, List<String> educatorIds, String tenantId) {
        return addEducatorsToLessonUseCase.addEducatorsToLessonUseCase(lessonId, educatorIds, tenantId);
    }

    @Transactional
    public Lesson createLesson(Lesson lesson, String outletId, String tenantId, @Nullable String courseId, List<String> educatorIds) {
        Lesson createdLesson = createLessonUseCase.createStandaloneLesson(lesson, outletId);

        ofNullable(courseId).ifPresent(cid -> this.linkLessonToCourse(List.of(createdLesson.getId()), cid, tenantId));

        if (!educatorIds.isEmpty()) {
            List<EducatorLessonLink> educatorLessonLinks = addEducatorsToLessonUseCase.addEducatorsToLessonUseCase(createdLesson.getId(), educatorIds, tenantId);
            createdLesson.setEducatorLessonLinks(educatorLessonLinks.stream().collect(Collectors.toUnmodifiableSet()));
        }

        if (courseId != null) {
            relationshipService.writeRelationship("lesson", createdLesson.getId(),
                    "parent_course", "course", courseId);
        } else {
            relationshipService.writeRelationship("lesson", createdLesson.getId(),
                    "parent_outlet", "outlet", outletId);
        }

        return createdLesson;
    }

    @Transactional
    public List<Lesson> linkLessonToCourse(List<String> lessonIds, String courseId, String tenantId) {
        return linkLessonsToCourseUseCase.linkLessonsToCourseUseCase(lessonIds, courseId, tenantId);
    }
}
