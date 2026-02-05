package com.aura.app.controllers.graphql.v1.subjects;

import com.aura.app.codegen.types.CreateSubjectInput;
import com.aura.app.codegen.types.SubjectResponse;
import com.aura.app.mappers.SubjectMapper;
import com.aura.core.auth.User;
import com.aura.core.entities.subjects.Subject;
import com.aura.details.services.SubjectService;
import lombok.RequiredArgsConstructor;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.ContextValue;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.stereotype.Controller;


import static com.aura.app.codegen.DgsConstants.MUTATION.CreateSubject;

@Controller
@RequiredArgsConstructor
public class SubjectsGqlController {
    private final SubjectService subjectService;

    @MutationMapping(CreateSubject)
    public SubjectResponse createSubject(@Argument CreateSubjectInput createSubjectInput, @ContextValue(value = "user") User user) {
        Subject createdSubject = subjectService.createSubject(SubjectMapper.INSTANCE.toDomain(createSubjectInput, user.getCurrentTenantAccessed()));
        return SubjectMapper.INSTANCE.toDto(createdSubject);
    }
}
