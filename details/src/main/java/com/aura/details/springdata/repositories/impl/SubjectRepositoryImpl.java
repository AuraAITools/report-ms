package com.aura.details.springdata.repositories.impl;

import com.aura.core.entities.links.TopicSubjectLink;
import com.aura.core.entities.subjects.Subject;
import com.aura.core.repositories.SubjectRepository;
import com.aura.details.springdata.mappers.subjects.SubjectMapper;
import com.aura.details.springdata.mappers.subjects.TopicSubjectLinkMapper;
import com.aura.details.springdata.repositories.jpa.links.JpaTopicSubjectLinkRepository;
import com.aura.details.springdata.repositories.jpa.subjects.JpaSubjectRepository;
import com.aura.details.springdata.repositories.jpa.topics.JpaTopicRepository;
import com.aura.details.springdata.tables.links.TopicSubjectLinkTable;
import com.aura.details.springdata.tables.subjects.SubjectTable;
import com.aura.details.springdata.tables.topics.TopicTable;
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
        SubjectTable createdSubject = jpaSubjectRepository.save(SubjectMapper.INSTANCE.toTableEntity(subject));
        return SubjectMapper.INSTANCE.toDomainEntity(createdSubject);
    }

    @Override
    public List<TopicSubjectLink> link(UUID subjectId, List<UUID> topicIds, String tenantId) {
        List<TopicTable> topicTables = jpaTopicRepository.findAllById(topicIds);
        SubjectTable subjectTable = jpaSubjectRepository.findById(subjectId).orElseThrow(() -> new RuntimeException("topic not found"));
        List<TopicSubjectLinkTable> topicSubjectTableLinks = topicTables
                .stream()
                .map(tpc -> {
                    TopicSubjectLinkTable topicSubjectLinkTable = TopicSubjectLinkTable
                            .builder()
                            .subject(subjectTable)
                            .topic(tpc)
                            .tenantId(tenantId)
                            .build();
                    return topicSubjectLinkTable;
                })
                .toList();
        List<TopicSubjectLinkTable> topicSubjectLinkTables = jpaTopicSubjectLinkRepository.saveAll(topicSubjectTableLinks);
        return topicSubjectLinkTables.stream().map(TopicSubjectLinkMapper.INSTANCE::toDomain).toList();
    }
}
