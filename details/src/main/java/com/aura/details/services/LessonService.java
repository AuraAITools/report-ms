package com.aura.details.services;

import com.aura.core.clients.RelationshipAuthorizationService;
import com.aura.core.entities.lessons.Lesson;
import com.aura.core.exceptions.ResourceNotFoundException;
import com.aura.core.entities.lessons.LessonGroup;
import com.aura.core.entities.lessons.LessonGroupPlan;
import com.aura.core.entities.lessons.LessonOutletRoomBooking;
import com.aura.core.exceptions.LessonConflictException;
import com.aura.core.repositories.EducatorRepository;
import com.aura.core.repositories.LessonOutletRoomBookingRepository;
import com.aura.core.repositories.LessonRepository;
import com.aura.core.repositories.StudentRepository;
import com.aura.core.usecases.lessons.AddEducatorsToLessonUseCase;
import com.aura.core.usecases.lessons.AddLevelsToLessonUseCase;
import com.aura.core.usecases.lessons.AddStudentsToLessonUseCase;
import com.aura.core.usecases.lessons.AddSubjectsToLessonUseCase;
import com.aura.core.usecases.lessons.AddTopicsToLessonUseCase;
import com.aura.core.usecases.lessons.ChangeLessonOutletRoomUseCase;
import com.aura.core.usecases.lessons.CreateLessonUseCase;
import com.aura.core.usecases.lessons.DeleteLessonUseCase;
import com.aura.core.usecases.lessons.DetectLessonConflictsUseCase;
import com.aura.core.usecases.lessons.GetLessonsInOutletByDateRangeUseCase;
import com.aura.core.usecases.lessons.LessonQueryUseCase;
import jakarta.transaction.Transactional;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.Nullable;

@RequiredArgsConstructor
public class LessonService {

    private final CreateLessonUseCase createLessonUseCase;
    private final DetectLessonConflictsUseCase detectLessonConflictsUseCase;
    private final AddEducatorsToLessonUseCase addEducatorsToLessonUseCase;
    private final AddStudentsToLessonUseCase addStudentsToLessonUseCase;
    private final AddSubjectsToLessonUseCase addSubjectsToLessonUseCase;
    private final AddLevelsToLessonUseCase addLevelsToLessonUseCase;
    private final AddTopicsToLessonUseCase addTopicsToLessonUseCase;
    private final LessonQueryUseCase lessonQueryUseCase;
    private final GetLessonsInOutletByDateRangeUseCase getLessonsInOutletByDateRangeUseCase;
    private final DeleteLessonUseCase deleteLessonUseCase;
    private final ChangeLessonOutletRoomUseCase changeLessonOutletRoomUseCase;
    private final LessonRepository lessonRepository;
    private final LessonOutletRoomBookingRepository lessonOutletRoomBookingRepository;
    private final RelationshipAuthorizationService relationshipService;
    private final EducatorRepository educatorRepository;
    private final StudentRepository studentRepository;
    private final LessonGroupService lessonGroupService;
    private final LessonGroupPlanService lessonGroupPlanService;
    private final StudentLessonReportService studentLessonReportService;

    public @Nullable Lesson findById(UUID id) {
        return lessonQueryUseCase.findById(id);
    }

    public List<Lesson> findAllByCourseId(String courseId) {
        return lessonQueryUseCase.findAllByCourseId(courseId);
    }

    public List<Lesson> findAllByOutletId(String outletId) {
        return lessonQueryUseCase.findAllByOutletId(outletId);
    }

    @org.springframework.transaction.annotation.Transactional(readOnly = true)
    public List<Lesson> getLessonsInOutletByDateRange(String tenantId,
                                                     String outletId,
                                                     Instant startInclusive,
                                                     Instant endExclusive) {
        return getLessonsInOutletByDateRangeUseCase.getLessonsInOutletByDateRange(
                tenantId, outletId, startInclusive, endExclusive);
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
                               List<NewLessonGroup> lessonGroups) {
        Lesson created = createLessonUseCase.createLessonInCourse(lesson, courseId);

        String lessonId = created.getId();

        String tenantId = created.getTenantId();
        Instant lessonStart = created.getLessonStartTimestamptz();
        Instant lessonEnd = created.getLessonEndTimestamptz();

        if (outletRoomId != null) {
            List<String> roomConflicts = detectLessonConflictsUseCase.detectRoomConflicts(
                    tenantId, outletRoomId, lessonStart, lessonEnd, null);
            if (!roomConflicts.isEmpty()) {
                throw new LessonConflictException(
                        LessonConflictException.ConflictType.ROOM, roomConflicts, null);
            }
        }

        for (String studentId : studentIds) {
            List<String> studentConflicts = detectLessonConflictsUseCase.detectStudentConflicts(
                    tenantId, studentId, lessonStart, lessonEnd, null);
            if (!studentConflicts.isEmpty()) {
                throw new LessonConflictException(
                        LessonConflictException.ConflictType.STUDENT, studentConflicts, studentId);
            }
        }

        for (String educatorId : educatorIds) {
            List<String> educatorConflicts = detectLessonConflictsUseCase.detectEducatorConflicts(
                    tenantId, educatorId, lessonStart, lessonEnd, null);
            if (!educatorConflicts.isEmpty()) {
                throw new LessonConflictException(
                        LessonConflictException.ConflictType.EDUCATOR, educatorConflicts, educatorId);
            }
        }

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
                        "lesson_user_group", lessonId, "parent", "course_user_group", courseId),
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

        for (NewLessonGroup newGroup : lessonGroups) {
            LessonGroup group = lessonGroupService.createLessonGroup(
                    LessonGroup.builder().name(newGroup.name()).build(),
                    lessonId);
            if (!newGroup.studentIds().isEmpty()) {
                lessonGroupService.addStudentsToLessonGroup(group.getId(), newGroup.studentIds(), created.getTenantId());
            }
            lessonGroupPlanService.createLessonGroupPlan(
                    newGroup.plan(), group.getId(),
                    newGroup.topicIds(), newGroup.materialIds(), newGroup.lessonObjectives());
            if (!newGroup.studentIds().isEmpty()) {
                studentLessonReportService.createReportsForStudentsInGroup(
                        lessonId, newGroup.studentIds(), newGroup.topicIds(), created.getTenantId());
            }
        }

        return created;
    }

    public record NewLessonGroup(String name,
                                 LessonGroupPlan plan,
                                 List<String> topicIds,
                                 List<String> studentIds,
                                 List<String> materialIds,
                                 List<LessonGroupPlanService.NewLessonObjective> lessonObjectives) {
    }

    @Transactional
    public Lesson assignStudentsToLesson(String lessonId, List<String> studentIds) {
        Lesson lesson = lessonRepository.findById(UUID.fromString(lessonId));
        if (lesson == null) {
            throw new ResourceNotFoundException("Lesson", lessonId);
        }
        Instant start = lesson.getLessonStartTimestamptz();
        Instant end = lesson.getLessonEndTimestamptz();
        String tenantId = lesson.getTenantId();

        for (String studentId : studentIds) {
            List<String> conflicts = detectLessonConflictsUseCase.detectStudentConflicts(
                    tenantId, studentId, start, end, lessonId);
            if (!conflicts.isEmpty()) {
                throw new LessonConflictException(
                        LessonConflictException.ConflictType.STUDENT, conflicts, studentId);
            }
        }

        addStudentsToLessonUseCase.addStudentsToLesson(lessonId, studentIds, tenantId);

        var userIds = studentRepository.findUserIdsByStudentIds(studentIds).values().stream().toList();
        List<RelationshipAuthorizationService.Relationship> relationships = new ArrayList<>();
        userIds.forEach(userId -> relationships.add(
                new RelationshipAuthorizationService.Relationship(
                        "lesson_user_group", lessonId, "viewer", "user", userId)));
        if (!relationships.isEmpty()) {
            relationshipService.writeRelationships(relationships);
        }
        return lesson;
    }

    @Transactional
    public Lesson assignEducatorsToLesson(String lessonId, List<String> educatorIds) {
        Lesson lesson = lessonRepository.findById(UUID.fromString(lessonId));
        if (lesson == null) {
            throw new ResourceNotFoundException("Lesson", lessonId);
        }
        Instant start = lesson.getLessonStartTimestamptz();
        Instant end = lesson.getLessonEndTimestamptz();
        String tenantId = lesson.getTenantId();

        for (String educatorId : educatorIds) {
            List<String> conflicts = detectLessonConflictsUseCase.detectEducatorConflicts(
                    tenantId, educatorId, start, end, lessonId);
            if (!conflicts.isEmpty()) {
                throw new LessonConflictException(
                        LessonConflictException.ConflictType.EDUCATOR, conflicts, educatorId);
            }
        }

        addEducatorsToLessonUseCase.addEducatorsToLessonUseCase(lessonId, educatorIds, tenantId);

        var userIds = educatorRepository.findUserIdsByEducatorIds(educatorIds).values().stream().toList();
        List<RelationshipAuthorizationService.Relationship> relationships = new ArrayList<>();
        userIds.forEach(userId -> {
            relationships.add(new RelationshipAuthorizationService.Relationship(
                    "lesson_user_group", lessonId, "admin", "user", userId));
            relationships.add(new RelationshipAuthorizationService.Relationship(
                    "lesson_user_group", lessonId, "group_admin", "user", userId));
        });
        if (!relationships.isEmpty()) {
            relationshipService.writeRelationships(relationships);
        }
        return lesson;
    }

    @Transactional
    public Lesson changeLessonOutletRoom(String lessonId, @Nullable String outletRoomId) {
        return changeLessonOutletRoomUseCase.changeLessonOutletRoom(lessonId, outletRoomId);
    }

    @Transactional
    public void deleteById(String id) {
        lessonRepository.deleteAllLessonLinksByLessonId(id);
        deleteLessonUseCase.deleteById(id);
        relationshipService.deleteAllRelationshipsForResource("lesson", id);
        relationshipService.deleteAllRelationshipsForResource("lesson_user_group", id);
    }
}
