package com.aura.details.springdata.repositories.impl;

import com.aura.core.entities.lessons.Lesson;
import com.aura.core.repositories.LessonRepository;
import com.aura.details.springdata.mappers.lessons.LessonMapper;
import com.aura.details.springdata.models.courses.CourseModel;
import com.aura.details.springdata.models.lessons.LessonModel;
import com.aura.details.springdata.models.outlets.OutletModel;
import com.aura.details.springdata.repositories.jpa.lessons.JpaLessonRepository;
import com.aura.details.springdata.repositories.jpa.links.JpaEducatorLessonLinkRepository;
import com.aura.details.springdata.repositories.jpa.links.JpaLevelLessonLinkRepository;
import com.aura.details.springdata.repositories.jpa.links.JpaStudentLessonLinkRepository;
import com.aura.details.springdata.repositories.jpa.links.JpaSubjectLessonLinkRepository;
import com.aura.details.springdata.repositories.jpa.links.JpaTopicLessonLinkRepository;
import jakarta.persistence.EntityManager;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.Nullable;

@RequiredArgsConstructor
public class LessonRepositoryImpl implements LessonRepository {
    private final JpaLessonRepository jpaLessonRepository;
    private final JpaLevelLessonLinkRepository jpaLevelLessonLinkRepository;
    private final JpaSubjectLessonLinkRepository jpaSubjectLessonLinkRepository;
    private final JpaTopicLessonLinkRepository jpaTopicLessonLinkRepository;
    private final JpaStudentLessonLinkRepository jpaStudentLessonLinkRepository;
    private final JpaEducatorLessonLinkRepository jpaEducatorLessonLinkRepository;
    private final EntityManager entityManager;

    @Override
    public Lesson save(Lesson lesson) {
        LessonModel model = toAttachedModel(lesson);
        LessonModel createdLessonModel = jpaLessonRepository.save(model);
        return LessonMapper.INSTANCE.toDomainEntity(createdLessonModel);
    }

    @Override
    public Lesson saveAndFlush(Lesson lesson) {
        LessonModel model = toAttachedModel(lesson);
        LessonModel createdLessonModel = jpaLessonRepository.saveAndFlush(model);
        return LessonMapper.INSTANCE.toDomainEntity(createdLessonModel);
    }

    @Override
    public List<Lesson> updateLessons(List<Lesson> lessons) {
        List<LessonModel> lessonModels = lessons.stream().map(this::toAttachedModel).toList();
        return jpaLessonRepository.saveAll(lessonModels).stream().map(LessonMapper.INSTANCE::toDomainEntity).toList();
    }

    @Override
    public @Nullable Lesson findById(UUID id) {
        return jpaLessonRepository.findById(id)
                .map(LessonMapper.INSTANCE::toDomainEntity)
                .orElse(null);
    }

    @Override
    public List<Lesson> findAllById(List<UUID> ids) {
        return jpaLessonRepository.findAllById(ids).stream().map(LessonMapper.INSTANCE::toDomainEntity).toList();
    }

    @Override
    public Map<String, List<Lesson>> findAllByCourseIds(List<String> courseIds) {
        if (courseIds.isEmpty()) return Collections.emptyMap();
        List<UUID> uuids = courseIds.stream().map(UUID::fromString).toList();
        return jpaLessonRepository.findByCourse_IdIn(uuids).stream()
                .map(LessonMapper.INSTANCE::toDomainEntity)
                .collect(Collectors.groupingBy(Lesson::getCourseId));
    }

    @Override
    public List<Lesson> findAllByCourseId(String courseId) {
        return jpaLessonRepository.findByCourse_IdIn(List.of(UUID.fromString(courseId))).stream()
                .map(LessonMapper.INSTANCE::toDomainEntity)
                .toList();
    }

    @Override
    public List<Lesson> findAllByOutletId(String outletId) {
        return jpaLessonRepository.findByOutlet_Id(UUID.fromString(outletId)).stream()
                .map(LessonMapper.INSTANCE::toDomainEntity)
                .toList();
    }

    @Override
    public List<Lesson> findAllByTenantId(String tenantId) {
        return jpaLessonRepository.findByTenantId(tenantId).stream()
                .map(LessonMapper.INSTANCE::toDomainEntity)
                .toList();
    }

    @Override
    public List<Lesson> findAllByEducatorId(String educatorId) {
        return jpaEducatorLessonLinkRepository.findLessonsByEducatorId(UUID.fromString(educatorId)).stream()
                .map(LessonMapper.INSTANCE::toDomainEntity)
                .toList();
    }

    @Override
    public List<Lesson> findAllByStudentId(String studentId) {
        return jpaStudentLessonLinkRepository.findLessonsByStudentId(UUID.fromString(studentId)).stream()
                .map(LessonMapper.INSTANCE::toDomainEntity)
                .toList();
    }

    @Override
    public void deleteById(String id) {
        jpaLessonRepository.deleteById(UUID.fromString(id));
    }

    @Override
    public void deleteAllLessonLinksByLessonId(String lessonId) {
        UUID uuid = UUID.fromString(lessonId);
        jpaLevelLessonLinkRepository.deleteAllByLessonId(uuid);
        jpaSubjectLessonLinkRepository.deleteAllByLessonId(uuid);
        jpaTopicLessonLinkRepository.deleteAllByLessonId(uuid);
        jpaStudentLessonLinkRepository.deleteAllByLessonId(uuid);
        jpaEducatorLessonLinkRepository.deleteAllByLessonId(uuid);
    }

    @Override
    public List<Lesson> findActiveOverlappingForStudent(
            String tenantId,
            String studentId,
            java.time.Instant start,
            java.time.Instant end,
            @org.jspecify.annotations.Nullable String excludeLessonId) {
        UUID studentUuid = UUID.fromString(studentId);
        UUID excludeUuid = excludeLessonId == null ? null : UUID.fromString(excludeLessonId);
        return jpaStudentLessonLinkRepository
                .findActiveOverlappingLessonsByStudent(tenantId, studentUuid, start, end, excludeUuid)
                .stream()
                .map(LessonMapper.INSTANCE::toDomainEntity)
                .toList();
    }

    private LessonModel toAttachedModel(Lesson lesson) {
        LessonModel model = LessonMapper.INSTANCE.toModelEntity(lesson);
        model.setCourse(Refs.ref(entityManager, CourseModel.class, lesson.getCourseId()));
        model.setOutlet(Refs.ref(entityManager, OutletModel.class, lesson.getOutletId()));
        return model;
    }
}
