package com.aura.details.services;

import com.aura.core.clients.RelationshipAuthorizationService;
import com.aura.core.entities.levels.Level;
import com.aura.core.entities.schools.SchoolCategory;
import com.aura.core.repositories.CourseRepository;
import com.aura.core.repositories.LevelRepository;
import com.aura.core.usecases.levels.CreateLevelUseCase;
import com.aura.core.usecases.levels.DeleteLevelUseCase;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InOrder;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;


import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@DisplayName("Level service")
@ExtendWith(MockitoExtension.class)
class LevelServiceTest {

    @Mock
    private CreateLevelUseCase createLevelUseCase;
    @Mock
    private DeleteLevelUseCase deleteLevelUseCase;
    @Mock
    private LevelRepository levelRepository;
    @Mock
    private CourseRepository courseRepository;
    @Mock
    private RelationshipAuthorizationService relationshipService;

    @InjectMocks
    private LevelService levelService;

    @DisplayName("Should null out course levels, clear lesson-level links, delete level, and wipe spicedb")
    @Test
    void deleteById_cascadeAndCleanup() {
        levelService.deleteById("level-1");

        InOrder ordered = inOrder(courseRepository, levelRepository, deleteLevelUseCase, relationshipService);
        ordered.verify(courseRepository).nullOutLevelForCourses("level-1");
        ordered.verify(levelRepository).deleteAllLevelLessonLinksByLevelId("level-1");
        ordered.verify(deleteLevelUseCase).deleteById("level-1");
        ordered.verify(relationshipService).deleteAllRelationshipsForResource("level", "level-1");
        ordered.verify(relationshipService).deleteAllRelationshipsForResource("level_user_group", "level-1");
    }

    @DisplayName("Should create level and write the three structural relationships in a single batch")
    @Test
    void createLevelInInstitution_writesStructuralRelationshipsAsBatch() {
        Level input = Level.builder().name("P1").category(SchoolCategory.PRIMARY).tenantId("tenant-1").build();
        Level created = Level.builder().id("level-1").name("P1").category(SchoolCategory.PRIMARY).tenantId("tenant-1").build();
        when(createLevelUseCase.createLevel(any())).thenReturn(created);

        Level result = levelService.createLevelInInstitution(input);

        assertThat(result).isEqualTo(created);
        verify(relationshipService).writeRelationships(argThat(list ->
                list.containsAll(List.of(
                        new RelationshipAuthorizationService.Relationship(
                                "level", "level-1", "user_group", "level_user_group", "level-1"),
                        new RelationshipAuthorizationService.Relationship(
                                "level_user_group", "level-1", "parent", "system_user_group", "aura"),
                        new RelationshipAuthorizationService.Relationship(
                                "level", "level-1", "parent", "institution", "tenant-1")))
                        && list.size() == 3));
        verifyNoMoreInteractions(relationshipService);
    }
}
