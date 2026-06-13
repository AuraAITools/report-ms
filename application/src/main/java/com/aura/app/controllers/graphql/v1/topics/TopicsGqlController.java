package com.aura.app.controllers.graphql.v1.topics;

import com.aura.app.codegen.types.DeleteImpactPreview;
import com.aura.app.codegen.types.TopicInput;
import com.aura.app.codegen.types.TopicResponse;
import com.aura.app.exceptions.ResourceNotFoundBusinessException;
import com.aura.app.mappers.DeleteImpactMapper;
import com.aura.app.mappers.TopicMapper;
import com.aura.core.auth.User;
import com.aura.core.entities.topics.Topic;
import com.aura.details.services.TopicService;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.ContextValue;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;


import static com.aura.app.codegen.DgsConstants.MUTATION.CreateTopic;

@Controller
@RequiredArgsConstructor
public class TopicsGqlController {

    private final TopicService topicService;

    @PreAuthorize("hasPermission(#topicInput.institutionId, 'institution', 'create_topic')")
    @MutationMapping(CreateTopic)
    public TopicResponse createTopic(@Argument TopicInput topicInput, @ContextValue(name = "user") User user) {
        Topic topicToBeCreated = TopicMapper.INSTANCE.toDomainEntity(topicInput);
        Topic createdTopic = topicService.createTopic(topicToBeCreated);
        return TopicMapper.INSTANCE.toDto(createdTopic);
    }

    @PreAuthorize("hasPermission(#institutionId, 'institution', 'view_topic')")
    @QueryMapping
    public TopicResponse getTopicById(@Argument("institutionId") String institutionId,
                                      @Argument("topicId") String topicId,
                                      @ContextValue(name = "user") User user) {
        return Optional.ofNullable(topicService.findById(topicId))
                .map(TopicMapper.INSTANCE::toDto)
                .orElseThrow(() -> new ResourceNotFoundBusinessException("Topic"));
    }

    @PreAuthorize("hasPermission(#institutionId, 'institution', 'view')")
    @QueryMapping
    public List<TopicResponse> getAllTopicsInInstitution(@Argument("institutionId") String institutionId,
                                                         @ContextValue(name = "user") User user) {
        return topicService.findAllByTenantId(institutionId).stream()
                .map(TopicMapper.INSTANCE::toDto)
                .toList();
    }

    @PreAuthorize("hasPermission(#institutionId, 'institution', 'view_topic')")
    @QueryMapping
    public DeleteImpactPreview getAllAffectedResourcesIfTopicDeleted(@Argument("institutionId") String institutionId,
                                                                     @Argument("id") String id,
                                                                     @ContextValue(name = "user") User user) {
        if (topicService.findById(id) == null) {
            throw new ResourceNotFoundBusinessException("Topic");
        }
        return DeleteImpactMapper.INSTANCE.toDto(topicService.getDeleteImpact(id));
    }

    @PreAuthorize("hasPermission(#institutionId, 'institution', 'delete_topic')")
    @MutationMapping
    public Boolean deleteTopicById(@Argument("institutionId") String institutionId,
                                    @Argument("id") String id,
                                    @ContextValue(name = "user") User user) {
        if (topicService.findById(id) == null) {
            throw new ResourceNotFoundBusinessException("Topic");
        }
        topicService.deleteById(id);
        return true;
    }
}
