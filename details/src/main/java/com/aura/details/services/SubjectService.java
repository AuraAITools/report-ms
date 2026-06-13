package com.aura.details.services;

import com.aura.core.entities.delete_impact.DeleteImpact;
import com.aura.core.entities.subjects.Subject;
import com.aura.core.entities.subjects.SubjectWithTopics;
import com.aura.core.entities.topics.Topic;
import com.aura.core.repositories.SubjectRepository;
import com.aura.core.usecases.subjects.CreateSubjectUseCase;
import com.aura.core.usecases.subjects.CreateTopicSubjectLinksUseCase;
import com.aura.core.usecases.subjects.DeleteSubjectUseCase;
import com.aura.core.usecases.subjects.GetSubjectDeleteImpactUseCase;
import com.aura.core.usecases.subjects.SubjectQueryUseCase;
import jakarta.transaction.Transactional;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.Nullable;

@RequiredArgsConstructor
public class SubjectService {

    private final CreateSubjectUseCase createSubjectUseCase;
    private final CreateTopicSubjectLinksUseCase createTopicSubjectLinksUseCase;
    private final SubjectQueryUseCase subjectQueryUseCase;
    private final DeleteSubjectUseCase deleteSubjectUseCase;
    private final GetSubjectDeleteImpactUseCase getSubjectDeleteImpactUseCase;
    private final SubjectRepository subjectRepository;

    @Transactional
    public Subject createSubject(Subject subject) {
        return createSubjectUseCase.createSubject(subject);
    }

    @Transactional
    public SubjectWithTopics createTopicSubjectLinks(UUID subjectId, List<UUID> topicIds, String tenantId) {
        return createTopicSubjectLinksUseCase.linkTopicsToSubject(subjectId, topicIds, tenantId);
    }

    public @Nullable Subject findById(String id) {
        return subjectQueryUseCase.findById(id);
    }

    public List<Subject> findAllByTenantId(String tenantId) {
        return subjectQueryUseCase.findAllByTenantId(tenantId);
    }

    public Map<String, List<Topic>> findTopicsBySubjectIds(List<String> subjectIds) {
        return subjectQueryUseCase.findTopicsBySubjectIds(subjectIds);
    }

    public DeleteImpact getDeleteImpact(String id) {
        return getSubjectDeleteImpactUseCase.getDeleteImpact(id);
    }

    @Transactional
    public void deleteById(String id) {
        subjectRepository.deleteAllTopicSubjectLinksBySubjectId(id);
        subjectRepository.deleteAllSubjectCourseLinksBySubjectId(id);
        subjectRepository.deleteAllSubjectStudentLinksBySubjectId(id);
        subjectRepository.deleteAllSubjectEducatorLinksBySubjectId(id);
        subjectRepository.deleteAllSubjectLevelLinksBySubjectId(id);
        subjectRepository.deleteAllSubjectTestGroupLinksBySubjectId(id);
        subjectRepository.deleteAllSubjectLessonLinksBySubjectId(id);
        subjectRepository.nullOutSubjectOnTestResultsBySubjectId(id);
        deleteSubjectUseCase.deleteById(id);
    }
}
