package com.aura.core.usecases.lesson_group_plans;

import com.aura.core.entities.lessons.LessonGroupPlan;
import com.aura.core.entities.links.LessonGroupPlanMaterialLink;
import com.aura.core.entities.links.MaterialTopicLink;
import com.aura.core.entities.materials.Material;
import com.aura.core.repositories.LessonGroupPlanMaterialLinkRepository;
import com.aura.core.repositories.LessonGroupPlanRepository;
import com.aura.core.repositories.MaterialRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import org.jspecify.annotations.Nullable;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;


import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("Add materials to lesson group plan use case")
class AddMaterialsToLessonGroupPlanUseCaseImplTest {

    @DisplayName("Should return empty list when materialIds is empty")
    @Test
    void emptyMaterialIds_returnsEmpty() {
        var linkRepo = new StubLessonGroupPlanMaterialLinkRepository();
        var planRepo = new StubLessonGroupPlanRepository(null);
        var matRepo = new StubMaterialRepository(List.of());
        var useCase = new AddMaterialsToLessonGroupPlanUseCaseImpl(linkRepo, planRepo, matRepo);

        List<LessonGroupPlanMaterialLink> result = useCase.addMaterialsToLessonGroupPlan("plan-id", List.of(), "tenant-1");

        assertThat(result).isEmpty();
        assertThat(linkRepo.savedAll).isEmpty();
    }

    @DisplayName("Should create a material link per materialId with the plan id and tenant")
    @Test
    void createsLinks() {
        UUID planUuid = UUID.randomUUID();
        LessonGroupPlan plan = LessonGroupPlan.builder().id(planUuid.toString()).plan("p").build();
        Material m1 = Material.builder().id("mat-1").name("m1").build();
        Material m2 = Material.builder().id("mat-2").name("m2").build();

        var linkRepo = new StubLessonGroupPlanMaterialLinkRepository();
        var planRepo = new StubLessonGroupPlanRepository(plan);
        var matRepo = new StubMaterialRepository(List.of(m1, m2));
        var useCase = new AddMaterialsToLessonGroupPlanUseCaseImpl(linkRepo, planRepo, matRepo);

        List<LessonGroupPlanMaterialLink> result = useCase.addMaterialsToLessonGroupPlan(
                planUuid.toString(), List.of("mat-1", "mat-2"), "tenant-1");

        assertThat(result).hasSize(2);
        assertThat(result).allSatisfy(link -> {
            assertThat(link.getLessonGroupPlanId()).isEqualTo(plan.getId());
            assertThat(link.getTenantId()).isEqualTo("tenant-1");
        });
        assertThat(result).extracting(LessonGroupPlanMaterialLink::getMaterialId)
                .containsExactlyInAnyOrder("mat-1", "mat-2");
    }

    @DisplayName("Should throw when any materialId is not found")
    @Test
    void missingMaterial_throws() {
        UUID planUuid = UUID.randomUUID();
        LessonGroupPlan plan = LessonGroupPlan.builder().id(planUuid.toString()).plan("p").build();
        Material m1 = Material.builder().id("mat-1").name("m1").build();

        var linkRepo = new StubLessonGroupPlanMaterialLinkRepository();
        var planRepo = new StubLessonGroupPlanRepository(plan);
        var matRepo = new StubMaterialRepository(List.of(m1));
        var useCase = new AddMaterialsToLessonGroupPlanUseCaseImpl(linkRepo, planRepo, matRepo);

        assertThatThrownBy(() -> useCase.addMaterialsToLessonGroupPlan(
                planUuid.toString(), List.of("mat-1", "mat-missing"), "tenant-1"))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("mat-missing");
    }

    // --- Stubs ---

    private static class StubLessonGroupPlanMaterialLinkRepository implements LessonGroupPlanMaterialLinkRepository {
        final List<List<LessonGroupPlanMaterialLink>> savedAll = new ArrayList<>();

        @Override
        public List<LessonGroupPlanMaterialLink> saveAll(List<LessonGroupPlanMaterialLink> links) {
            savedAll.add(links);
            return links;
        }
    }

    private static class StubLessonGroupPlanRepository implements LessonGroupPlanRepository {
        private final LessonGroupPlan planToReturn;

        StubLessonGroupPlanRepository(LessonGroupPlan planToReturn) {
            this.planToReturn = planToReturn;
        }

        @Override
        public LessonGroupPlan save(LessonGroupPlan plan) {
            throw new UnsupportedOperationException();
        }

        @Override
        public LessonGroupPlan findById(UUID id) {
            return planToReturn;
        }

        @Override
        public LessonGroupPlan findByLessonGroupId(String lessonGroupId) {
            throw new UnsupportedOperationException();
        }

        @Override
        public List<LessonGroupPlan> findAllByIds(List<UUID> ids) {
            throw new UnsupportedOperationException();
        }

        @Override
        public Map<String, LessonGroupPlan> findAllByLessonGroupIds(List<String> lessonGroupIds) {
            throw new UnsupportedOperationException();
        }
    }

    private static class StubMaterialRepository implements MaterialRepository {
        private final List<Material> materialsToReturn;

        StubMaterialRepository(List<Material> materialsToReturn) {
            this.materialsToReturn = materialsToReturn;
        }

        @Override
        public Material save(Material material) {
            throw new UnsupportedOperationException();
        }

        @Override
        public @Nullable Material findById(String id) {
            return materialsToReturn.stream().filter(m -> m.getId().equals(id)).findFirst().orElse(null);
        }

        @Override
        public List<Material> findAllByTenantId(String tenantId) {
            throw new UnsupportedOperationException();
        }

        @Override
        public Map<String, List<Material>> findAllByLessonIds(List<String> lessonIds) {
            throw new UnsupportedOperationException();
        }

        @Override
        public Map<String, List<com.aura.core.entities.topics.Topic>> findTopicsByMaterialIds(List<String> materialIds) {
            throw new UnsupportedOperationException();
        }

        @Override
        public void deleteById(String id) {
            throw new UnsupportedOperationException();
        }

        @Override
        public void deleteAllTopicLinksByMaterialId(String materialId) {
            throw new UnsupportedOperationException();
        }

        @Override
        public void deleteAllLessonPlanLinksByMaterialId(String materialId) {
            throw new UnsupportedOperationException();
        }

        @Override
        public List<MaterialTopicLink> linkTopicsToMaterial(String materialId, List<String> topicIds, String tenantId) {
            throw new UnsupportedOperationException();
        }
    }
}
