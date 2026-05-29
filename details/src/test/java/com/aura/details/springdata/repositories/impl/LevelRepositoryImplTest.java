package com.aura.details.springdata.repositories.impl;

import com.aura.core.entities.levels.Level;
import com.aura.details.springdata.models.levels.LevelModel;
import com.aura.details.springdata.repositories.jpa.courses.JpaCourseRepository;
import com.aura.details.springdata.repositories.jpa.levels.JpaLevelRepository;
import com.aura.details.springdata.repositories.jpa.links.JpaLevelEducatorLinkRepository;
import com.aura.details.springdata.repositories.jpa.links.JpaLevelLessonLinkRepository;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;


import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@DisplayName("Level repository impl")
@ExtendWith(MockitoExtension.class)
class LevelRepositoryImplTest {

    @Mock
    private JpaLevelRepository jpaLevelRepository;
    @Mock
    private JpaLevelLessonLinkRepository jpaLevelLessonLinkRepository;
    @Mock
    private JpaCourseRepository jpaCourseRepository;
    @Mock
    private JpaLevelEducatorLinkRepository jpaLevelEducatorLinkRepository;

    @DisplayName("Should map each course id to its single level when fetching by course ids")
    @Test
    void findAllByCourseIds_mapsCourseIdToSingleLevel() {
        UUID courseId1 = UUID.randomUUID();
        UUID courseId2 = UUID.randomUUID();
        UUID level1Id = UUID.randomUUID();
        UUID level2Id = UUID.randomUUID();
        LevelModel l1 = LevelModel.builder().id(level1Id).name("Primary 1").build();
        LevelModel l2 = LevelModel.builder().id(level2Id).name("Primary 2").build();

        when(jpaCourseRepository.findLevelsByCourseIds(List.of(courseId1, courseId2)))
                .thenReturn(List.of(
                        new Object[]{courseId1, l1},
                        new Object[]{courseId2, l2}
                ));

        Map<String, Level> result = repository()
                .findAllByCourseIds(List.of(courseId1.toString(), courseId2.toString()));

        assertThat(result).hasSize(2);
        assertThat(result.get(courseId1.toString()).getName()).isEqualTo("Primary 1");
        assertThat(result.get(courseId2.toString()).getName()).isEqualTo("Primary 2");
    }

    @DisplayName("Should omit courses without a level from the result map")
    @Test
    void findAllByCourseIds_omitsCoursesWithoutLevel() {
        UUID courseWithLevel = UUID.randomUUID();
        UUID courseWithoutLevel = UUID.randomUUID();
        LevelModel level = LevelModel.builder().id(UUID.randomUUID()).name("L").build();

        when(jpaCourseRepository.findLevelsByCourseIds(List.of(courseWithLevel, courseWithoutLevel)))
                .thenReturn(List.<Object[]>of(new Object[]{courseWithLevel, level}));

        Map<String, Level> result = repository()
                .findAllByCourseIds(List.of(courseWithLevel.toString(), courseWithoutLevel.toString()));

        assertThat(result).hasSize(1);
        assertThat(result).containsOnlyKeys(courseWithLevel.toString());
    }

    @DisplayName("Should return empty map when course id list is empty")
    @Test
    void findAllByCourseIds_emptyList_returnsEmptyMap() {
        assertThat(repository().findAllByCourseIds(List.of())).isEmpty();
    }

    @DisplayName("Should return all levels scoped to the tenant id")
    @Test
    void findAllByTenantId_returnsScopedLevels() {
        String tenantId = UUID.randomUUID().toString();
        LevelModel l1 = LevelModel.builder().id(UUID.randomUUID()).name("P1").tenantId(tenantId).build();
        LevelModel l2 = LevelModel.builder().id(UUID.randomUUID()).name("P2").tenantId(tenantId).build();
        when(jpaLevelRepository.findAll(org.mockito.ArgumentMatchers.<org.springframework.data.jpa.domain.Specification<LevelModel>>any()))
                .thenReturn(List.of(l1, l2));

        List<Level> result = repository().findAllByTenantId(tenantId);

        assertThat(result).hasSize(2);
        assertThat(result).extracting(Level::getName).containsExactly("P1", "P2");
    }

    @DisplayName("Should return empty list when tenant has no levels")
    @Test
    void findAllByTenantId_noLevels_returnsEmptyList() {
        when(jpaLevelRepository.findAll(org.mockito.ArgumentMatchers.<org.springframework.data.jpa.domain.Specification<LevelModel>>any()))
                .thenReturn(List.of());

        List<Level> result = repository().findAllByTenantId(UUID.randomUUID().toString());

        assertThat(result).isEmpty();
    }

    @DisplayName("Should return empty map when no educator ids provided to findAllByEducatorIds")
    @Test
    void findAllByEducatorIds_emptyList_returnsEmptyMap() {
        assertThat(repository().findAllByEducatorIds(List.of())).isEmpty();
    }

    @DisplayName("Should group levels by educator id from link table query")
    @Test
    void findAllByEducatorIds_groupsLevelsByEducator() {
        UUID educatorId1 = UUID.randomUUID();
        UUID educatorId2 = UUID.randomUUID();
        LevelModel l1 = LevelModel.builder().id(UUID.randomUUID()).name("P1").build();
        LevelModel l2 = LevelModel.builder().id(UUID.randomUUID()).name("P2").build();
        LevelModel l3 = LevelModel.builder().id(UUID.randomUUID()).name("P3").build();

        when(jpaLevelEducatorLinkRepository.findLevelsByEducatorIds(List.of(educatorId1, educatorId2)))
                .thenReturn(List.<Object[]>of(
                        new Object[]{educatorId1, l1},
                        new Object[]{educatorId1, l2},
                        new Object[]{educatorId2, l3}
                ));

        Map<String, List<Level>> result = repository()
                .findAllByEducatorIds(List.of(educatorId1.toString(), educatorId2.toString()));

        assertThat(result).hasSize(2);
        assertThat(result.get(educatorId1.toString())).extracting(Level::getName).containsExactly("P1", "P2");
        assertThat(result.get(educatorId2.toString())).extracting(Level::getName).containsExactly("P3");
    }

    private LevelRepositoryImpl repository() {
        return new LevelRepositoryImpl(jpaLevelRepository, jpaLevelLessonLinkRepository, jpaCourseRepository, jpaLevelEducatorLinkRepository);
    }
}
