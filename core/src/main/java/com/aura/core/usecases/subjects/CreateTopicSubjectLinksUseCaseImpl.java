package com.aura.core.usecases.subjects;

import com.aura.core.entities.subjects.SubjectWithTopics;
import com.aura.core.repositories.SubjectRepository;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class CreateTopicSubjectLinksUseCaseImpl implements CreateTopicSubjectLinksUseCase {
    private final SubjectRepository subjectRepository;

    @Override
    public SubjectWithTopics linkTopicsToSubject(UUID subjectId, List<UUID> topicIds, String tenantId) {
        return subjectRepository.link(subjectId, topicIds, tenantId);
    }
}
