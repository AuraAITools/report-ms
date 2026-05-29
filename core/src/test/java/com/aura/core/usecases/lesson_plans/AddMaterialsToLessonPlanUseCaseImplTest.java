package com.aura.core.usecases.lesson_plans;

import com.aura.core.entities.lessons.LessonPlan;
import com.aura.core.entities.links.LessonPlanMaterialLink;
import com.aura.core.entities.links.MaterialTopicLink;
import com.aura.core.entities.materials.Material;
import com.aura.core.repositories.LessonPlanMaterialLinkRepository;
import com.aura.core.repositories.LessonPlanRepository;
import com.aura.core.repositories.MaterialRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import org.jspecify.annotations.Nullable;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;


import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("Add materials to lesson plan use case")
class AddMaterialsToLessonPlanUseCaseImplTest {

    @DisplayName("Should return empty list when materialIds is empty")
    @Test
    void emptyMaterialIds_returnsEmpty() {
        var linkRepo = new StubLessonPlanMaterialLinkRepository();
        var planRepo = new StubLessonPlanRepository(null);
        var matRepo = new StubMaterialRepository(List.of());
        var useCase = new AddMaterialsToLessonPlanUseCaseImpl(linkRepo, planRepo, matRepo);

        List<LessonPlanMaterialLink> result = useCase.addMaterialsToLessonPlan("plan-id", List.of(), "tenant-1");

        assertThat(result).isEmpty();
        assertThat(linkRepo.savedAll).isEmpty();
    }

    @DisplayName("Should create a material link per materialId with the plan id and tenant")
    @Test
    void createsLinks() {
        UUID planUuid = UUID.randomUUID();
        LessonPlan plan = LessonPlan.builder().id(planUuid.toString()).plan("p").build();
        Material m1 = Material.builder().id("mat-1").name("m1").build();
        Material m2 = Material.builder().id("mat-2").name("m2").build();

        var linkRepo = new StubLessonPlanMaterialLinkRepository();
        var planRepo = new StubLessonPlanRepository(plan);
        var matRepo = new StubMaterialRepository(List.of(m1, m2));
        var useCase = new AddMaterialsToLessonPlanUseCaseImpl(linkRepo, planRepo, matRepo);

        List<LessonPlanMaterialLink> result = useCase.addMaterialsToLessonPlan(
                planUuid.toString(), List.of("mat-1", "mat-2"), "tenant-1");

        assertThat(result).hasSize(2);
        assertThat(result).allSatisfy(link -> {
            assertThat(link.getLessonPlanId()).isEqualTo(plan.getId());
            assertThat(link.getTenantId()).isEqualTo("tenant-1");
        });
        assertThat(result).extracting(LessonPlanMaterialLink::getMaterialId)
                .containsExactlyInAnyOrder("mat-1", "mat-2");
    }

    @DisplayName("Should throw when any materialId is not found")
    @Test
    void missingMaterial_throws() {
        UUID planUuid = UUID.randomUUID();
        LessonPlan plan = LessonPlan.builder().id(planUuid.toString()).plan("p").build();
        Material m1 = Material.builder().id("mat-1").name("m1").build();

        var linkRepo = new StubLessonPlanMaterialLinkRepository();
        var planRepo = new StubLessonPlanRepository(plan);
        var matRepo = new StubMaterialRepository(List.of(m1));
        var useCase = new AddMaterialsToLessonPlanUseCaseImpl(linkRepo, planRepo, matRepo);

        assertThatThrownBy(() -> useCase.addMaterialsToLessonPlan(
                planUuid.toString(), List.of("mat-1", "mat-missing"), "tenant-1"))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("mat-missing");
    }

    // --- Stubs ---

    private static class StubLessonPlanMaterialLinkRepository implements LessonPlanMaterialLinkRepository {
        final List<List<LessonPlanMaterialLink>> savedAll = new ArrayList<>();

        @Override
        public List<LessonPlanMaterialLink> saveAll(List<LessonPlanMaterialLink> links) {
            savedAll.add(links);
            return links;
        }
    }

    private static class StubLessonPlanRepository implements LessonPlanRepository {
        private final LessonPlan planToReturn;

        StubLessonPlanRepository(LessonPlan planToReturn) {
            this.planToReturn = planToReturn;
        }

        @Override
        public LessonPlan save(LessonPlan lessonPlan) {
            throw new UnsupportedOperationException();
        }

        @Override
        public LessonPlan findById(UUID id) {
            return planToReturn;
        }

        @Override
        public List<LessonPlan> findAllByLessonId(String lessonId) {
            throw new UnsupportedOperationException();
        }

        @Override
        public List<LessonPlan> findAllByIds(List<UUID> ids) {
            throw new UnsupportedOperationException();
        }

        @Override
        public java.util.Map<String, List<LessonPlan>> findAllByLessonIds(List<String> lessonIds) {
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
        public java.util.Map<String, List<Material>> findAllByLessonIds(List<String> lessonIds) {
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
