package com.aura.details.services;

import com.aura.core.entities.delete_impact.AffectedResourceGroup;
import com.aura.core.entities.delete_impact.CleanupAction;
import com.aura.core.entities.delete_impact.DeleteImpact;
import com.aura.core.entities.subjects.Subject;
import com.aura.core.repositories.SubjectRepository;
import com.aura.core.usecases.subjects.CreateSubjectUseCase;
import com.aura.core.usecases.subjects.CreateTopicSubjectLinksUseCase;
import com.aura.core.usecases.subjects.DeleteSubjectUseCase;
import com.aura.core.usecases.subjects.GetSubjectDeleteImpactUseCase;
import com.aura.core.usecases.subjects.SubjectQueryUseCase;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InOrder;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;


import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.when;

@DisplayName("Subject service")
@ExtendWith(MockitoExtension.class)
class SubjectServiceTest {

    @Mock
    private CreateSubjectUseCase createSubjectUseCase;
    @Mock
    private CreateTopicSubjectLinksUseCase createTopicSubjectLinksUseCase;
    @Mock
    private SubjectQueryUseCase subjectQueryUseCase;
    @Mock
    private DeleteSubjectUseCase deleteSubjectUseCase;
    @Mock
    private GetSubjectDeleteImpactUseCase getSubjectDeleteImpactUseCase;
    @Mock
    private SubjectRepository subjectRepository;

    @InjectMocks
    private SubjectService subjectService;

    @DisplayName("Should create subject by delegating to the use case")
    @Test
    void createSubject_delegatesToUseCase() {
        Subject input = Subject.builder().name("Math").tenantId("tenant-1").build();
        Subject created = Subject.builder().id("sub-1").name("Math").tenantId("tenant-1").build();
        when(createSubjectUseCase.createSubject(input)).thenReturn(created);

        assertThat(subjectService.createSubject(input)).isEqualTo(created);
    }

    @DisplayName("Should delete subject, purge all 7 link tables, then null-out TestResult.subject in order")
    @Test
    void deleteById_clearsAllLinks() {
        subjectService.deleteById("sub-1");

        InOrder ordered = inOrder(subjectRepository, deleteSubjectUseCase);
        ordered.verify(subjectRepository).deleteAllTopicSubjectLinksBySubjectId("sub-1");
        ordered.verify(subjectRepository).deleteAllSubjectCourseLinksBySubjectId("sub-1");
        ordered.verify(subjectRepository).deleteAllSubjectStudentLinksBySubjectId("sub-1");
        ordered.verify(subjectRepository).deleteAllSubjectEducatorLinksBySubjectId("sub-1");
        ordered.verify(subjectRepository).deleteAllSubjectLevelLinksBySubjectId("sub-1");
        ordered.verify(subjectRepository).deleteAllSubjectTestGroupLinksBySubjectId("sub-1");
        ordered.verify(subjectRepository).deleteAllSubjectLessonLinksBySubjectId("sub-1");
        ordered.verify(subjectRepository).nullOutSubjectOnTestResultsBySubjectId("sub-1");
        ordered.verify(deleteSubjectUseCase).deleteById("sub-1");
    }

    @DisplayName("Should return delete-impact preview by delegating to the use case")
    @Test
    void getDeleteImpact_delegatesToUseCase() {
        DeleteImpact impact = DeleteImpact.builder()
                .resourceType("Subject")
                .resourceId("sub-1")
                .affectedGroups(List.of(
                        AffectedResourceGroup.builder()
                                .resourceType("TestResult")
                                .cleanupAction(CleanupAction.NULL_OUT)
                                .affectedIds(List.of("tr-1"))
                                .build()))
                .build();
        when(getSubjectDeleteImpactUseCase.getDeleteImpact("sub-1")).thenReturn(impact);

        DeleteImpact result = subjectService.getDeleteImpact("sub-1");

        assertThat(result).isSameAs(impact);
    }
}
