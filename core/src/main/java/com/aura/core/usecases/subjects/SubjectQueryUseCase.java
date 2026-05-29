package com.aura.core.usecases.subjects;

import com.aura.core.entities.subjects.Subject;
import com.aura.core.entities.topics.Topic;
import java.util.List;
import java.util.Map;
import org.jspecify.annotations.Nullable;

public interface SubjectQueryUseCase {
    @Nullable Subject findById(String id);

    List<Subject> findAllByTenantId(String tenantId);

    Map<String, List<Topic>> findTopicsBySubjectIds(List<String> subjectIds);
}
