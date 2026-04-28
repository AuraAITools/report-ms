package com.aura.details.services;

import com.aura.core.clients.RelationshipAuthorizationService;
import com.aura.core.entities.subjects.Subject;
import com.aura.core.usecases.subjects.CreateSubjectUseCase;
import com.aura.core.usecases.subjects.CreateTopicSubjectLinksUseCase;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@DisplayName("Subject service")
@ExtendWith(MockitoExtension.class)
class SubjectServiceTest {

    @Mock private CreateSubjectUseCase createSubjectUseCase;
    @Mock private CreateTopicSubjectLinksUseCase createTopicSubjectLinksUseCase;
    @Mock private RelationshipAuthorizationService relationshipService;

    @InjectMocks private SubjectService subjectService;

    @DisplayName("Should create subject and write parent_institution relationship")
    @Test
    void createSubject_writesParentInstitutionRelationship() {
        Subject input = Subject.builder().name("Math").tenantId("tenant-1").build();
        Subject created = Subject.builder().id("sub-1").name("Math").tenantId("tenant-1").build();
        when(createSubjectUseCase.createSubject(input)).thenReturn(created);

        Subject result = subjectService.createSubject(input);

        assertThat(result).isEqualTo(created);
        verify(relationshipService).writeRelationship(
                "subject", "sub-1", "parent_institution", "institution", "tenant-1");
    }
}
