package com.aura.details.springdata.repositories.impl;

import com.aura.core.entities.links.TopicSubjectLink;
import com.aura.core.entities.subjects.Subject;
import com.aura.core.repositories.SubjectRepository;
import com.aura.details.springdata.mappers.subjects.SubjectMapper;
import com.aura.details.springdata.mappers.subjects.TopicSubjectLinkMapper;
import com.aura.details.springdata.models.links.TopicSubjectLinkModel;
import com.aura.details.springdata.models.subjects.SubjectModel;
import com.aura.details.springdata.models.topics.TopicModel;
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
}
