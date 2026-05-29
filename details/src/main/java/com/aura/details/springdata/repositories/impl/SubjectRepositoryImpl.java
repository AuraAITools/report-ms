package com.aura.details.springdata.repositories.impl;

import com.aura.core.entities.links.SubjectCourseLink;
import com.aura.core.entities.subjects.Subject;
import com.aura.core.entities.subjects.SubjectWithTopics;
import com.aura.core.entities.topics.Topic;
import com.aura.core.exceptions.TechnicalException;
import com.aura.core.repositories.SubjectRepository;
import com.aura.details.springdata.mappers.links.SubjectCourseLinkMapper;
import com.aura.details.springdata.mappers.subjects.SubjectMapper;
import com.aura.details.springdata.mappers.topics.TopicMapper;
import com.aura.details.springdata.models.courses.CourseModel;
import com.aura.details.springdata.models.links.SubjectCourseLinkModel;
import com.aura.details.springdata.models.links.TopicSubjectLinkModel;
import com.aura.details.springdata.models.subjects.SubjectModel;
import com.aura.details.springdata.models.topics.TopicModel;
import com.aura.details.springdata.repositories.jpa.courses.JpaCourseRepository;
import com.aura.details.springdata.repositories.jpa.links.JpaSubjectCourseLinkRepository;
import com.aura.details.springdata.repositories.jpa.links.JpaSubjectEducatorLinkRepository;
import com.aura.details.springdata.repositories.jpa.links.JpaSubjectLessonLinkRepository;
import com.aura.details.springdata.repositories.jpa.links.JpaTopicSubjectLinkRepository;
import com.aura.details.springdata.repositories.jpa.subjects.JpaSubjectRepository;
import com.aura.details.springdata.repositories.jpa.topics.JpaTopicRepository;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.Nullable;

@RequiredArgsConstructor
public class SubjectRepositoryImpl implements SubjectRepository {
    private final JpaSubjectRepository jpaSubjectRepository;
    private final JpaTopicRepository jpaTopicRepository;
    private final JpaTopicSubjectLinkRepository jpaTopicSubjectLinkRepository;
    private final JpaCourseRepository jpaCourseRepository;
    private final JpaSubjectCourseLinkRepository jpaSubjectCourseLinkRepository;
    private final JpaSubjectLessonLinkRepository jpaSubjectLessonLinkRepository;
    private final JpaSubjectEducatorLinkRepository jpaSubjectEducatorLinkRepository;


    @Override
    public Subject save(Subject subject) {
        SubjectModel createdSubject = jpaSubjectRepository.save(SubjectMapper.INSTANCE.toModelEntity(subject));
        return SubjectMapper.INSTANCE.toDomainEntity(createdSubject);
    }

    @Override
    public @Nullable Subject findById(String id) {
        return jpaSubjectRepository.findById(UUID.fromString(id))
                .map(SubjectMapper.INSTANCE::toDomainEntity)
                .orElse(null);
    }

    @Override
    public List<Subject> findAllByIds(List<UUID> subjectIds) {
        return jpaSubjectRepository.findAllById(subjectIds).stream().map(SubjectMapper.INSTANCE::toDomainEntity).toList();
    }

    @Override
    public List<Subject> findAllByTenantId(String tenantId) {
        return jpaSubjectRepository.findByTenantId(tenantId).stream()
                .map(SubjectMapper.INSTANCE::toDomainEntity)
                .toList();
    }

    @Override
    public Map<String, List<Subject>> findAllByCourseIds(List<String> courseIds) {
        if (courseIds.isEmpty()) return Collections.emptyMap();
        List<UUID> uuids = courseIds.stream().map(UUID::fromString).toList();
        Map<String, List<Subject>> result = new HashMap<>();
        for (Object[] row : jpaSubjectCourseLinkRepository.findSubjectsByCourseIds(uuids)) {
            String courseId = row[0].toString();
            SubjectModel subjectModel = (SubjectModel) row[1];
            result.computeIfAbsent(courseId, k -> new ArrayList<>())
                    .add(SubjectMapper.INSTANCE.toDomainEntity(subjectModel));
        }
        return result;
    }

    @Override
    public SubjectWithTopics link(UUID subjectId, List<UUID> topicIds, String tenantId) {
        List<TopicModel> topicModels = jpaTopicRepository.findAllById(topicIds);
        SubjectModel subjectModel = jpaSubjectRepository.findById(subjectId).orElseThrow(() -> new RuntimeException("topic not found"));
        List<TopicSubjectLinkModel> topicSubjectModelLinks = topicModels
                .stream()
                .<TopicSubjectLinkModel>map(tpc -> TopicSubjectLinkModel
                        .builder()
                        .subject(subjectModel)
                        .topic(tpc)
                        .tenantId(tenantId)
                        .build())
                .toList();
        jpaTopicSubjectLinkRepository.saveAll(topicSubjectModelLinks);
        return new SubjectWithTopics(
                SubjectMapper.INSTANCE.toDomainEntity(subjectModel),
                topicModels.stream().map(TopicMapper.INSTANCE::toDomainEntity).toList());
    }

    @Override
    public List<SubjectCourseLink> createSubjectCourseLinks(List<String> subjectIds, String courseId, String tenantId) {
        if (subjectIds.isEmpty()) return Collections.emptyList();

        CourseModel courseModel = jpaCourseRepository.getReferenceById(UUID.fromString(courseId));
        var foundSubjects = jpaSubjectRepository.findAllById(subjectIds.stream().map(UUID::fromString).toList());
        if (foundSubjects.size() != subjectIds.size()) {
            throw new TechnicalException("some subjects are non-existent");
        }

        List<SubjectCourseLinkModel> subjectCourseLinkModels = foundSubjects
                .stream()
                .<SubjectCourseLinkModel>map(subjectModel -> SubjectCourseLinkModel.builder()
                        .course(courseModel)
                        .subject(subjectModel)
                        .tenantId(tenantId)
                        .build())
                .toList();

        return jpaSubjectCourseLinkRepository
                .saveAll(subjectCourseLinkModels)
                .stream()
                .map(SubjectCourseLinkMapper.INSTANCE::toDomain)
                .toList();
    }

    @Override
    public Map<String, List<Subject>> findAllByEducatorIds(List<String> educatorIds) {
        if (educatorIds.isEmpty()) return Collections.emptyMap();
        List<UUID> uuids = educatorIds.stream().map(UUID::fromString).toList();
        Map<String, List<Subject>> result = new HashMap<>();
        for (Object[] row : jpaSubjectEducatorLinkRepository.findSubjectsByEducatorIds(uuids)) {
            String educatorId = row[0].toString();
            SubjectModel subjectModel = (SubjectModel) row[1];
            result.computeIfAbsent(educatorId, k -> new ArrayList<>())
                    .add(SubjectMapper.INSTANCE.toDomainEntity(subjectModel));
        }
        return result;
    }

    @Override
    public Map<String, List<Subject>> findAllByLessonIds(List<String> lessonIds) {
        if (lessonIds.isEmpty()) return Collections.emptyMap();
        List<UUID> uuids = lessonIds.stream().map(UUID::fromString).toList();
        Map<String, List<Subject>> result = new HashMap<>();
        for (Object[] row : jpaSubjectLessonLinkRepository.findSubjectsByLessonIds(uuids)) {
            String lessonId = row[0].toString();
            SubjectModel subjectModel = (SubjectModel) row[1];
            result.computeIfAbsent(lessonId, k -> new ArrayList<>())
                    .add(SubjectMapper.INSTANCE.toDomainEntity(subjectModel));
        }
        return result;
    }

    @Override
    public Map<String, List<Topic>> findTopicsBySubjectIds(List<String> subjectIds) {
        if (subjectIds.isEmpty()) return Collections.emptyMap();
        List<UUID> uuids = subjectIds.stream().map(UUID::fromString).toList();
        Map<String, List<Topic>> result = new HashMap<>();
        for (Object[] row : jpaTopicSubjectLinkRepository.findTopicsBySubjectIds(uuids)) {
            String subjectId = row[0].toString();
            TopicModel topicModel = (TopicModel) row[1];
            result.computeIfAbsent(subjectId, k -> new ArrayList<>())
                    .add(TopicMapper.INSTANCE.toDomainEntity(topicModel));
        }
        return result;
    }

    @Override
    public void deleteById(String id) {
        jpaSubjectRepository.deleteById(UUID.fromString(id));
    }

    @Override
    public void deleteAllTopicSubjectLinksBySubjectId(String subjectId) {
        jpaTopicSubjectLinkRepository.deleteAllBySubjectId(UUID.fromString(subjectId));
    }

    @Override
    public void deleteAllSubjectCourseLinksBySubjectId(String subjectId) {
        jpaSubjectCourseLinkRepository.deleteAllBySubjectId(UUID.fromString(subjectId));
    }

    @Override
    public void deleteAllSubjectCourseLinksByCourseId(String courseId) {
        jpaSubjectCourseLinkRepository.deleteAllByCourseId(UUID.fromString(courseId));
    }
}
