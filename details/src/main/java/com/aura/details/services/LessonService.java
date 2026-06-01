package com.aura.details.services;

import com.aura.core.clients.RelationshipAuthorizationService;
import com.aura.core.entities.lessons.Lesson;
import com.aura.core.entities.lessons.LessonOutletRoomBooking;
import com.aura.core.entities.lessons.LessonPlan;
import com.aura.core.repositories.EducatorRepository;
import com.aura.core.repositories.LessonOutletRoomBookingRepository;
import com.aura.core.repositories.LessonRepository;
import com.aura.core.repositories.StudentRepository;
import com.aura.core.usecases.lessons.AddEducatorsToLessonUseCase;
import com.aura.core.usecases.lessons.AddLevelsToLessonUseCase;
import com.aura.core.usecases.lessons.AddStudentsToLessonUseCase;
import com.aura.core.usecases.lessons.AddSubjectsToLessonUseCase;
import com.aura.core.usecases.lessons.AddTopicsToLessonUseCase;
import com.aura.core.usecases.lessons.CreateLessonUseCase;
import com.aura.core.usecases.lessons.DeleteLessonUseCase;
import com.aura.core.usecases.lessons.LessonQueryUseCase;
import jakarta.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.Nullable;

@RequiredArgsConstructor
public class LessonService {

    private final CreateLessonUseCase createLessonUseCase;
    private final AddEducatorsToLessonUseCase addEducatorsToLessonUseCase;
    private final AddStudentsToLessonUseCase addStudentsToLessonUseCase;
    private final AddSubjectsToLessonUseCase addSubjectsToLessonUseCase;
    private final AddLevelsToLessonUseCase addLevelsToLessonUseCase;
    private final AddTopicsToLessonUseCase addTopicsToLessonUseCase;
    private final LessonQueryUseCase lessonQueryUseCase;
    private final DeleteLessonUseCase deleteLessonUseCase;
    private final LessonRepository lessonRepository;
    private final LessonOutletRoomBookingRepository lessonOutletRoomBookingRepository;
    private final RelationshipAuthorizationService relationshipService;
    private final EducatorRepository educatorRepository;
    private final StudentRepository studentRepository;
    private final LessonPlanService lessonPlanService;

    public @Nullable Lesson findById(UUID id) {
        return lessonQueryUseCase.findById(id);
    }

    public List<Lesson> findAllByCourseId(String courseId) {
        return lessonQueryUseCase.findAllByCourseId(courseId);
    }

    public List<Lesson> findAllByOutletId(String outletId) {
        return lessonQueryUseCase.findAllByOutletId(outletId);
    }

    public List<Lesson> findAllByEducatorId(String educatorId) {
        return lessonQueryUseCase.findAllByEducatorId(educatorId);
    }

    public List<Lesson> findAllByStudentId(String studentId) {
        return lessonQueryUseCase.findAllByStudentId(studentId);
    }

    @Transactional
    public Lesson createLesson(Lesson lesson,
                               String courseId,
                               @Nullable String outletRoomId,
                               List<String> educatorIds, List<String> studentIds,
                               List<String> subjectIds, List<String> levelIds,
                               List<String> topicIds,
                               List<NewLessonPlan> lessonPlans) {
        Lesson created = createLessonUseCase.createLessonInCourse(lesson, courseId);

        String lessonId = created.getId();

        if (outletRoomId != null) {
            LessonOutletRoomBooking booking = LessonOutletRoomBooking.builder()
                    .tenantId(created.getTenantId())
                    .lessonId(lessonId)
                    .outletRoomId(outletRoomId)
                    .startTimestampz(created.getLessonStartTimestamptz())
                    .endTimestampz(created.getLessonEndTimestamptz())
                    .build();
            LessonOutletRoomBooking savedBooking = lessonOutletRoomBookingRepository.save(booking);
            created.setLessonOutletRoomBooking(savedBooking);
        }

        List<RelationshipAuthorizationService.Relationship> relationships = new ArrayList<>(List.of(
                new RelationshipAuthorizationService.Relationship(
                        "lesson", lessonId, "user_group", "lesson_user_group", lessonId),
                new RelationshipAuthorizationService.Relationship(
                        "lesson_user_group", lessonId, "parent", "system_user_group", "aura"),
                new RelationshipAuthorizationService.Relationship(
                        "lesson", lessonId, "parent", "course", courseId)));

        if (!educatorIds.isEmpty()) {
            addEducatorsToLessonUseCase.addEducatorsToLessonUseCase(lessonId, educatorIds, lesson.getTenantId());
            var userIds = educatorRepository.findUserIdsByEducatorIds(educatorIds).values().stream().toList();
            userIds.forEach(userId -> {
                relationships.add(new RelationshipAuthorizationService.Relationship("lesson_user_group", lessonId, "admin", "user", userId));
                relationships.add(new RelationshipAuthorizationService.Relationship("lesson_user_group", lessonId, "group_admin", "user", userId));
            });
        }

        if (!studentIds.isEmpty()) {
            addStudentsToLessonUseCase.addStudentsToLesson(lessonId, studentIds, lesson.getTenantId());
            var userIds = studentRepository.findUserIdsByStudentIds(studentIds).values().stream().toList();
            userIds.forEach(userId -> relationships.add(new RelationshipAuthorizationService.Relationship("lesson_user_group", lessonId, "viewer", "user", userId)));
        }

        if (!subjectIds.isEmpty()) {
            addSubjectsToLessonUseCase.addSubjectsToLesson(lessonId, subjectIds, lesson.getTenantId());
        }

        if (!levelIds.isEmpty()) {
            addLevelsToLessonUseCase.addLevelsToLesson(lessonId, levelIds, lesson.getTenantId());
        }

        if (!topicIds.isEmpty()) {
            addTopicsToLessonUseCase.addTopicsToLesson(lessonId, topicIds, lesson.getTenantId());
        }

        relationshipService.writeRelationships(relationships);

        for (NewLessonPlan newPlan : lessonPlans) {
            lessonPlanService.createLessonPlan(
                    newPlan.plan(), lessonId, newPlan.topicIds(),
                    newPlan.studentIds(), newPlan.materialIds(), newPlan.lessonObjectives());
        }

        return created;
    }

    public record NewLessonPlan(LessonPlan plan,
                                List<String> topicIds,
                                List<String> studentIds,
                                List<String> materialIds,
                                List<LessonPlanService.NewLessonObjective> lessonObjectives) {
    }

    @Transactional
    public void deleteById(String id) {
        lessonRepository.deleteAllLessonLinksByLessonId(id);
        deleteLessonUseCase.deleteById(id);
        relationshipService.deleteAllRelationshipsForResource("lesson", id);
        relationshipService.deleteAllRelationshipsForResource("lesson_user_group", id);
    }
}
