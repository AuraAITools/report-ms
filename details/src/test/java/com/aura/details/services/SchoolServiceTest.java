package com.aura.details.services;

import com.aura.core.entities.delete_impact.AffectedResourceGroup;
import com.aura.core.entities.delete_impact.CleanupAction;
import com.aura.core.entities.delete_impact.DeleteImpact;
import com.aura.core.repositories.SchoolRepository;
import com.aura.core.usecases.schools.CreateSchoolUseCase;
import com.aura.core.usecases.schools.DeleteSchoolUseCase;
import com.aura.core.usecases.schools.GetSchoolDeleteImpactUseCase;
import com.aura.core.usecases.schools.SchoolQueryUseCase;
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

@DisplayName("School service")
@ExtendWith(MockitoExtension.class)
class SchoolServiceTest {

    @Mock
    private SchoolQueryUseCase schoolQueryUseCase;
    @Mock
    private CreateSchoolUseCase createSchoolUseCase;
    @Mock
    private DeleteSchoolUseCase deleteSchoolUseCase;
    @Mock
    private GetSchoolDeleteImpactUseCase getSchoolDeleteImpactUseCase;
    @Mock
    private SchoolRepository schoolRepository;

    @InjectMocks
    private SchoolService schoolService;

    @DisplayName("Should null out the school FK on students before deleting the school")
    @Test
    void deleteById_nullsOutStudentSchoolFkBeforeDeletingSchool() {
        schoolService.deleteById("school-1");

        InOrder ordered = inOrder(schoolRepository, deleteSchoolUseCase);
        ordered.verify(schoolRepository).nullOutSchoolOnStudentsBySchoolId("school-1");
        ordered.verify(deleteSchoolUseCase).deleteById("school-1");
    }

    @DisplayName("Should return delete-impact preview by delegating to the use case")
    @Test
    void getDeleteImpact_delegatesToUseCase() {
        DeleteImpact impact = DeleteImpact.builder()
                .resourceType("School")
                .resourceId("school-1")
                .affectedGroups(List.of(
                        AffectedResourceGroup.builder()
                                .resourceType("Student")
                                .cleanupAction(CleanupAction.NULL_OUT)
                                .affectedIds(List.of("student-1", "student-2"))
                                .build()))
                .build();
        when(getSchoolDeleteImpactUseCase.getDeleteImpact("school-1")).thenReturn(impact);

        DeleteImpact result = schoolService.getDeleteImpact("school-1");

        assertThat(result).isSameAs(impact);
    }
}
