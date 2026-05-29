package com.aura.core.usecases.subjects;

import com.aura.core.entities.subjects.Subject;
import com.aura.core.entities.topics.Topic;
import com.aura.core.repositories.SubjectRepository;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.Nullable;

@RequiredArgsConstructor
public class SubjectQueryUseCaseImpl implements SubjectQueryUseCase {
    private final SubjectRepository subjectRepository;

    @Override
    public @Nullable Subject findById(String id) {
        return subjectRepository.findById(id);
    }

    @Override
    public List<Subject> findAllByTenantId(String tenantId) {
        return subjectRepository.findAllByTenantId(tenantId);
    }

    @Override
    public Map<String, List<Topic>> findTopicsBySubjectIds(List<String> subjectIds) {
        return subjectRepository.findTopicsBySubjectIds(subjectIds);
    }
}
