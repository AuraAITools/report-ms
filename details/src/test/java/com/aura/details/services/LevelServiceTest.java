package com.aura.details.services;

import com.aura.core.entities.delete_impact.AffectedResourceGroup;
import com.aura.core.entities.delete_impact.CleanupAction;
import com.aura.core.entities.delete_impact.DeleteImpact;
import com.aura.core.entities.levels.Level;
import com.aura.core.entities.schools.SchoolCategory;
import com.aura.core.repositories.CourseRepository;
import com.aura.core.repositories.LevelRepository;
import com.aura.core.usecases.levels.CreateLevelUseCase;
import com.aura.core.usecases.levels.DeleteLevelUseCase;
import com.aura.core.usecases.levels.GetLevelDeleteImpactUseCase;
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

@DisplayName("Level service")
@ExtendWith(MockitoExtension.class)
class LevelServiceTest {

    @Mock
    private CreateLevelUseCase createLevelUseCase;
    @Mock
    private DeleteLevelUseCase deleteLevelUseCase;
    @Mock
    private GetLevelDeleteImpactUseCase getLevelDeleteImpactUseCase;
    @Mock
    private LevelRepository levelRepository;
    @Mock
    private CourseRepository courseRepository;

    @InjectMocks
    private LevelService levelService;

    @DisplayName("Should create level by delegating to the use case")
    @Test
    void createLevelInInstitution_delegatesToUseCase() {
        Level input = Level.builder().name("P1").category(SchoolCategory.PRIMARY).tenantId("tenant-1").build();
        Level created = Level.builder().id("level-1").name("P1").category(SchoolCategory.PRIMARY).tenantId("tenant-1").build();
        when(createLevelUseCase.createLevel(input)).thenReturn(created);

        assertThat(levelService.createLevelInInstitution(input)).isSameAs(created);
    }

    @DisplayName("Should null out course/student levels, clear all link tables, and delete level")
    @Test
    void deleteById_cascadeAndCleanup() {
        levelService.deleteById("level-1");

        InOrder ordered = inOrder(courseRepository, levelRepository, deleteLevelUseCase);
        ordered.verify(courseRepository).nullOutLevelForCourses("level-1");
        ordered.verify(levelRepository).nullOutLevelOnStudentsByLevelId("level-1");
        ordered.verify(levelRepository).deleteAllLevelLessonLinksByLevelId("level-1");
        ordered.verify(levelRepository).deleteAllSubjectLevelLinksByLevelId("level-1");
        ordered.verify(levelRepository).deleteAllLevelEducatorLinksByLevelId("level-1");
        ordered.verify(deleteLevelUseCase).deleteById("level-1");
    }

    @DisplayName("Should return delete-impact preview by delegating to the use case")
    @Test
    void getDeleteImpact_delegatesToUseCase() {
        DeleteImpact impact = DeleteImpact.builder()
                .resourceType("Level")
                .resourceId("level-1")
                .affectedGroups(List.of(
                        AffectedResourceGroup.builder()
                                .resourceType("Student")
                                .cleanupAction(CleanupAction.NULL_OUT)
                                .affectedIds(List.of("student-1"))
                                .build()))
                .build();
        when(getLevelDeleteImpactUseCase.getDeleteImpact("level-1")).thenReturn(impact);

        DeleteImpact result = levelService.getDeleteImpact("level-1");

        assertThat(result).isSameAs(impact);
    }
}
