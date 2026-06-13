package com.aura.details.services;

import com.aura.core.entities.delete_impact.DeleteImpact;
import com.aura.core.entities.topics.Topic;
import com.aura.core.repositories.TopicRepository;
import com.aura.core.usecases.topics.CreateTopicUseCase;
import com.aura.core.usecases.topics.DeleteTopicUseCase;
import com.aura.core.usecases.topics.GetTopicDeleteImpactUseCase;
import com.aura.core.usecases.topics.TopicQueryUseCase;
import jakarta.transaction.Transactional;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.Nullable;

@RequiredArgsConstructor
public class TopicService {

    private final CreateTopicUseCase createTopicUseCase;
    private final TopicQueryUseCase topicQueryUseCase;
    private final DeleteTopicUseCase deleteTopicUseCase;
    private final GetTopicDeleteImpactUseCase getTopicDeleteImpactUseCase;
    private final TopicRepository topicRepository;

    @Transactional
    public Topic createTopic(Topic topic) {
        return createTopicUseCase.createTopic(topic);
    }

    public @Nullable Topic findById(String id) {
        return topicQueryUseCase.findById(id);
    }

    public List<Topic> findAllByTenantId(String tenantId) {
        return topicQueryUseCase.findAllByTenantId(tenantId);
    }

    public DeleteImpact getDeleteImpact(String id) {
        return getTopicDeleteImpactUseCase.getDeleteImpact(id);
    }

    @Transactional
    public void deleteById(String id) {
        topicRepository.deleteAllTopicSubjectLinksByTopicId(id);
        topicRepository.deleteAllLessonObjectiveTopicLinksByTopicId(id);
        topicRepository.deleteAllMaterialTopicLinksByTopicId(id);
        topicRepository.deleteAllTopicLessonLinksByTopicId(id);
        topicRepository.deleteAllLessonGroupPlanTopicLinksByTopicId(id);
        deleteTopicUseCase.deleteById(id);
    }
}
