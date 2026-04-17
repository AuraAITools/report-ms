package com.aura.app.controllers.graphql.v1.subjects;

import com.aura.app.annotations.ProtectedOperation;
import com.aura.app.codegen.types.CreateSubjectInput;
import com.aura.app.codegen.types.CreateSubjectTopicLinksInput;
import com.aura.app.codegen.types.SubjectResponse;
import com.aura.app.codegen.types.SubjectResponseWithTopics;
import com.aura.app.mappers.SubjectMapper;
import com.aura.core.auth.User;
import com.aura.core.entities.links.TopicSubjectLink;
import com.aura.core.entities.subjects.Subject;
import com.aura.details.services.SubjectService;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.ContextValue;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.stereotype.Controller;


import static com.aura.app.codegen.DgsConstants.MUTATION.CREATESUBJECTTOPICLINKS_INPUT_ARGUMENT.CreateSubjectTopicLinksInput;
import static com.aura.app.codegen.DgsConstants.MUTATION.CREATESUBJECT_INPUT_ARGUMENT.CreateSubjectInput;
import static com.aura.app.codegen.DgsConstants.MUTATION.CreateSubject;
import static com.aura.app.codegen.DgsConstants.MUTATION.CreateSubjectTopicLinks;

@Controller
@RequiredArgsConstructor
public class SubjectsGqlController {
    private final SubjectService subjectService;

    @ProtectedOperation(resource = "subject", scope = "create")
    @MutationMapping(CreateSubject)
    public SubjectResponse createSubject(@Argument(name = CreateSubjectInput) CreateSubjectInput createSubjectInput, @ContextValue(value = "user") User user) {
        Subject createdSubject = subjectService.createSubject(SubjectMapper.INSTANCE.toDomain(createSubjectInput, user.getCurrentTenantAccessed()));
        return SubjectMapper.INSTANCE.toDto(createdSubject);
    }

    @ProtectedOperation(resource = "subject", scope = "manage")
    @MutationMapping(CreateSubjectTopicLinks)
    public SubjectResponseWithTopics createSubjectTopicLinks(@Argument(name = CreateSubjectTopicLinksInput) CreateSubjectTopicLinksInput createSubjectTopicLinksInput, @ContextValue(name = "user") User user) {
        List<TopicSubjectLink> topicSubjectLinks = subjectService.createTopicSubjectLinks(UUID.fromString(createSubjectTopicLinksInput.getId()), createSubjectTopicLinksInput.getTopicIds().stream().map(UUID::fromString).toList(), user.getCurrentTenantAccessed());
        return SubjectMapper.INSTANCE.toDto(topicSubjectLinks.getFirst().getSubject(), topicSubjectLinks.stream().map(TopicSubjectLink::getTopic).toList());
    }
}
