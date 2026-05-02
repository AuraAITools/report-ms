package com.aura.details.springdata.repositories.impl;

import com.aura.core.entities.links.TopicSubjectLink;
import com.aura.core.entities.subjects.Subject;
import com.aura.core.repositories.SubjectRepository;
import com.aura.details.springdata.mappers.subjects.SubjectMapper;
import com.aura.details.springdata.mappers.subjects.TopicSubjectLinkMapper;
import com.aura.details.springdata.models.courses.CourseModel;
import com.aura.details.springdata.models.links.SubjectCourseLinkModel;
import com.aura.details.springdata.models.links.TopicSubjectLinkModel;
import com.aura.details.springdata.models.subjects.SubjectModel;
import com.aura.details.springdata.models.topics.TopicModel;
import com.aura.details.springdata.repositories.jpa.courses.JpaCourseRepository;
import com.aura.details.springdata.repositories.jpa.links.JpaSubjectCourseLinkRepository;
import com.aura.details.springdata.repositories.jpa.links.JpaTopicSubjectLinkRepository;
import com.aura.details.springdata.repositories.jpa.subjects.JpaSubjectRepository;
import com.aura.details.springdata.repositories.jpa.topics.JpaTopicRepository;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class SubjectRepositoryImpl implements SubjectRepository {
    private final JpaSubjectRepository jpaSubjectRepository;
    private final JpaTopicRepository jpaTopicRepository;
    private final JpaTopicSubjectLinkRepository jpaTopicSubjectLinkRepository;
    private final JpaCourseRepository jpaCourseRepository;
    private final JpaSubjectCourseLinkRepository jpaSubjectCourseLinkRepository;


    @Override
    public Subject save(Subject subject) {
        SubjectModel createdSubject = jpaSubjectRepository.save(SubjectMapper.INSTANCE.toModelEntity(subject));
        return SubjectMapper.INSTANCE.toDomainEntity(createdSubject);
    }

    @Override
    public List<TopicSubjectLink> link(UUID subjectId, List<UUID> topicIds, String tenantId) {
        List<TopicModel> topicModels = jpaTopicRepository.findAllById(topicIds);
        SubjectModel subjectModel = jpaSubjectRepository.findById(subjectId).orElseThrow(() -> new RuntimeException("topic not found"));
        List<TopicSubjectLinkModel> topicSubjectModelLinks = topicModels
                .stream()
                .map(tpc -> {
                    TopicSubjectLinkModel topicSubjectLinkModel = TopicSubjectLinkModel
                            .builder()
                            .subject(subjectModel)
                            .topic(tpc)
                            .tenantId(tenantId)
                            .build();
                    return topicSubjectLinkModel;
                })
                .toList();
        List<TopicSubjectLinkModel> topicSubjectLinkModels = jpaTopicSubjectLinkRepository.saveAll(topicSubjectModelLinks);
        return topicSubjectLinkModels.stream().map(TopicSubjectLinkMapper.INSTANCE::toDomain).toList();
    }

    @Override
    public void createSubjectCourseLinks(List<String> subjectIds, String courseId, String tenantId) {
        if (subjectIds.isEmpty()) return;

        CourseModel courseModel = jpaCourseRepository.getReferenceById(UUID.fromString(courseId));
        for (String subjectId : subjectIds) {
            SubjectModel subjectModel = jpaSubjectRepository.getReferenceById(UUID.fromString(subjectId));
            SubjectCourseLinkModel link = SubjectCourseLinkModel.builder()
                    .course(courseModel)
                    .subject(subjectModel)
                    .tenantId(tenantId)
                    .build();
            jpaSubjectCourseLinkRepository.save(link);
        }
    }
}
