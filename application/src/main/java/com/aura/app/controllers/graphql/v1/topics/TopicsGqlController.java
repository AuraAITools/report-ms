package com.aura.app.controllers.graphql.v1.topics;

import com.aura.app.codegen.types.TopicInput;
import com.aura.app.codegen.types.TopicResponse;
import com.aura.app.mappers.TopicMapper;
import com.aura.core.auth.User;
import com.aura.core.entities.topics.Topic;
import com.aura.details.services.TopicService;
import lombok.RequiredArgsConstructor;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.ContextValue;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.stereotype.Controller;

import static com.aura.app.codegen.DgsConstants.MUTATION.CreateTopic;

@Controller
@RequiredArgsConstructor
public class TopicsGqlController {

    private final TopicService topicService;

    @MutationMapping(CreateTopic)
    public TopicResponse createTopic(@Argument TopicInput topicInput, @ContextValue(name = "user") User user) {
        Topic topicToBeCreated = TopicMapper.INSTANCE.toDomainEntity(topicInput, user.getCurrentTenantAccessed());
        Topic createdTopic = topicService.createTopic(topicToBeCreated);
        return TopicMapper.INSTANCE.toDto(createdTopic);
    }
}
