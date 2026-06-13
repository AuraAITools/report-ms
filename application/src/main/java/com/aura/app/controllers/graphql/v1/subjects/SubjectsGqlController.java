package com.aura.app.controllers.graphql.v1.subjects;

import com.aura.app.codegen.types.CreateSubjectInput;
import com.aura.app.codegen.types.CreateSubjectTopicLinksInput;
import com.aura.app.codegen.types.DeleteImpactPreview;
import com.aura.app.codegen.types.SubjectResponse;
import com.aura.app.codegen.types.SubjectResponseWithTopics;
import com.aura.app.codegen.types.TopicResponse;
import com.aura.app.exceptions.ResourceNotFoundBusinessException;
import com.aura.app.mappers.DeleteImpactMapper;
import com.aura.app.mappers.SubjectMapper;
import com.aura.app.mappers.TopicMapper;
import com.aura.core.auth.User;
import com.aura.core.entities.subjects.Subject;
import com.aura.core.entities.subjects.SubjectWithTopics;
import com.aura.core.entities.topics.Topic;
import com.aura.details.services.SubjectService;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.BatchMapping;
import org.springframework.graphql.data.method.annotation.ContextValue;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;


import static com.aura.app.codegen.DgsConstants.MUTATION.CREATESUBJECTTOPICLINKS_INPUT_ARGUMENT.CreateSubjectTopicLinksInput;
import static com.aura.app.codegen.DgsConstants.MUTATION.CREATESUBJECT_INPUT_ARGUMENT.CreateSubjectInput;
import static com.aura.app.codegen.DgsConstants.MUTATION.CreateSubject;
import static com.aura.app.codegen.DgsConstants.MUTATION.CreateSubjectTopicLinks;

@Controller
@RequiredArgsConstructor
public class SubjectsGqlController {
    private final SubjectService subjectService;

    @PreAuthorize("hasPermission(#createSubjectInput.institutionId, 'institution', 'create_subject')")
    @MutationMapping(CreateSubject)
    public SubjectResponse createSubject(@Argument(name = CreateSubjectInput) CreateSubjectInput createSubjectInput, @ContextValue(value = "user") User user) {
        Subject createdSubject = subjectService.createSubject(SubjectMapper.INSTANCE.toDomain(createSubjectInput));
        return SubjectMapper.INSTANCE.toDto(createdSubject);
    }

    @PreAuthorize("hasPermission(#createSubjectTopicLinksInput.id, 'subject', 'update')")
    @MutationMapping(CreateSubjectTopicLinks)
    public SubjectResponseWithTopics createSubjectTopicLinks(@Argument(name = CreateSubjectTopicLinksInput) CreateSubjectTopicLinksInput createSubjectTopicLinksInput, @ContextValue(name = "user") User user) {
        SubjectWithTopics result = subjectService.createTopicSubjectLinks(UUID.fromString(createSubjectTopicLinksInput.getId()), createSubjectTopicLinksInput.getTopicIds().stream().map(UUID::fromString).toList(), createSubjectTopicLinksInput.getInstitutionId());

        return SubjectMapper.INSTANCE.toDto(result.subject(), result.topics());
    }

    @PreAuthorize("hasPermission(#subjectId, 'subject', 'view')")
    @QueryMapping
    public SubjectResponse getSubjectById(@Argument("subjectId") String subjectId,
                                          @ContextValue(name = "user") User user) {
        return Optional.ofNullable(subjectService.findById(subjectId))
                .map(SubjectMapper.INSTANCE::toDto)
                .orElseThrow(() -> new ResourceNotFoundBusinessException("Subject"));
    }

    @PreAuthorize("hasPermission(#institutionId, 'institution', 'view')")
    @QueryMapping
    public List<SubjectResponse> getAllSubjectsInInstitution(@Argument("institutionId") String institutionId,
                                                             @ContextValue(name = "user") User user) {
        return subjectService.findAllByTenantId(institutionId).stream()
                .map(SubjectMapper.INSTANCE::toDto)
                .toList();
    }

    @PreAuthorize("hasPermission(#id, 'subject', 'view')")
    @QueryMapping
    public DeleteImpactPreview getAllAffectedResourcesIfSubjectDeleted(@Argument("institutionId") String institutionId,
                                                                       @Argument("id") String id,
                                                                       @ContextValue(name = "user") User user) {
        if (subjectService.findById(id) == null) {
            throw new ResourceNotFoundBusinessException("Subject");
        }
        return DeleteImpactMapper.INSTANCE.toDto(subjectService.getDeleteImpact(id));
    }

    @PreAuthorize("hasPermission(#id, 'subject', 'delete')")
    @MutationMapping
    public Boolean deleteSubjectById(@Argument("id") String id, @ContextValue(name = "user") User user) {
        if (subjectService.findById(id) == null) {
            throw new ResourceNotFoundBusinessException("Subject");
        }
        subjectService.deleteById(id);
        return true;
    }

    @BatchMapping(typeName = "SubjectResponse", field = "topics")
    public Map<SubjectResponse, List<TopicResponse>> topics(List<SubjectResponse> subjectResponses) {
        List<String> subjectIds = subjectResponses.stream().map(SubjectResponse::getId).toList();
        Map<String, List<Topic>> topicsBySubjectId = subjectService.findTopicsBySubjectIds(subjectIds);
        return subjectResponses.stream().collect(Collectors.toMap(
                Function.identity(),
                s -> topicsBySubjectId.getOrDefault(s.getId(), List.of()).stream()
                        .map(TopicMapper.INSTANCE::toDto).toList()
        ));
    }
}
