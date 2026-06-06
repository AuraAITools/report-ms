package com.aura.details.services;

import com.aura.core.clients.RelationshipAuthorizationService;
import com.aura.core.entities.courses.Course;
import com.aura.core.entities.outlets.Outlet;
import com.aura.core.repositories.CourseRepository;
import com.aura.core.repositories.OutletRepository;
import com.aura.core.usecases.outlets.CreateOutletUseCase;
import com.aura.core.usecases.outlets.DeleteOutletUseCase;
import com.aura.core.usecases.outlets.OutletQueryUseCase;
import com.aura.core.usecases.outlets.UpdateOutletUseCase;
import com.aura.core.utils.KeysetPaginationCriteria;
import com.aura.core.utils.PagedResult;
import com.aura.core.utils.SearchCriteria;
import com.aura.core.utils.SortCriteria;
import jakarta.transaction.Transactional;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.Nullable;

@RequiredArgsConstructor
public class OutletService {

    private final CreateOutletUseCase createOutletUseCase;
    private final OutletQueryUseCase outletQueryUseCase;
    private final UpdateOutletUseCase updateOutletUseCase;
    private final DeleteOutletUseCase deleteOutletUseCase;
    private final CourseService courseService;
    private final OutletRepository outletRepository;
    private final CourseRepository courseRepository;
    private final RelationshipAuthorizationService relationshipService;

    @Transactional
    public Outlet createOutlet(Outlet outlet) {
        Outlet created = createOutletUseCase.createOutlet(outlet);
        String outletId = created.getId();
        String institutionId = created.getTenantId();

        relationshipService.writeRelationships(List.of(
                new RelationshipAuthorizationService.Relationship(
                        "outlet", outletId, "user_group", "outlet_user_group", outletId),
                new RelationshipAuthorizationService.Relationship(
                        "outlet_user_group", outletId, "parent", "institution_user_group", institutionId),
                new RelationshipAuthorizationService.Relationship(
                        "outlet", outletId, "parent", "institution", institutionId)));
        return created;
    }

    public PagedResult<Outlet> queryPageBy(String tenantId, KeysetPaginationCriteria keysetPaginationCriteria, List<SortCriteria> orderedSortCriterias, SearchCriteria searchCriteria) {
        return outletQueryUseCase.queryPageBy(tenantId, keysetPaginationCriteria, orderedSortCriterias, searchCriteria);
    }

    public List<Outlet> findAllByTenantId(String tenantId) {
        return outletQueryUseCase.findAllByTenantId(tenantId);
    }

    @Transactional
    public @Nullable Outlet updateOutlet(Outlet outlet) {
        return updateOutletUseCase.updateOutlet(outlet);
    }

    @Transactional
    public void deleteById(String id) {
        for (Course course : courseRepository.findAllByOutletId(id)) {
            courseService.deleteById(course.getId());
        }
        deleteOutletUseCase.deleteById(id);
        relationshipService.deleteAllRelationshipsForResource("outlet", id);
        relationshipService.deleteAllRelationshipsForResource("outlet_user_group", id);
    }
}
