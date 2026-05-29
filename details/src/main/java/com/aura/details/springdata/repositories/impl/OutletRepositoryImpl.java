package com.aura.details.springdata.repositories.impl;

import com.aura.core.entities.outlets.Outlet;
import com.aura.core.repositories.OutletRepository;
import com.aura.core.utils.KeysetPaginationCriteria;
import com.aura.core.utils.PageInfo;
import com.aura.core.utils.PagedResult;
import com.aura.core.utils.SearchCriteria;
import com.aura.core.utils.SortCriteria;
import com.aura.core.utils.SortDirection;
import com.aura.details.springdata.mappers.outlets.OutletMapper;
import com.aura.details.springdata.models.outlets.OutletModel;
import com.aura.details.springdata.repositories.jpa.links.JpaOutletEducatorLinkRepository;
import com.aura.details.springdata.repositories.jpa.outlets.JpaOutletRepository;
import com.aura.details.springdata.specifications.OutletSpecifications;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.Nullable;
import org.springframework.data.domain.ScrollPosition;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Window;
import org.springframework.data.jpa.domain.Specification;


import static com.aura.details.springdata.specifications.TenantSpecification.whereTenantId;

@RequiredArgsConstructor
public class OutletRepositoryImpl implements OutletRepository {
    private final JpaOutletRepository jpaOutletRepository;
    private final JpaOutletEducatorLinkRepository jpaOutletEducatorLinkRepository;

    @Override
    public @Nullable Outlet findById(String id) {
        Optional<OutletModel> outletModel = jpaOutletRepository.findById(UUID.fromString(id));
        return outletModel.map(OutletMapper.INSTANCE::toDomainEntity).orElse(null);
    }

    @Override
    public Outlet save(Outlet outlet) {
        OutletModel outletModel = OutletMapper.INSTANCE.toModelEntity(outlet);
        OutletModel createdModel = jpaOutletRepository.save(outletModel);
        return OutletMapper.INSTANCE.toDomainEntity(createdModel);
    }

    @Override
    public List<Outlet> findAllByIds(List<String> ids) {
        if (ids.isEmpty()) return List.of();
        List<UUID> uuids = ids.stream().map(UUID::fromString).toList();
        return jpaOutletRepository.findAllById(uuids).stream()
                .map(OutletMapper.INSTANCE::toDomainEntity)
                .toList();
    }

    @Override
    public List<Outlet> findAllByTenantId(String tenantId) {
        return jpaOutletRepository.findByTenantId(tenantId).stream()
                .map(OutletMapper.INSTANCE::toDomainEntity)
                .toList();
    }

    @Override
    public List<Outlet> findAllByTenantIds(Set<String> tenantIds) {
        return jpaOutletRepository.findByTenantIdIn(tenantIds).stream()
                .map(OutletMapper.INSTANCE::toDomainEntity)
                .toList();
    }

    @Override
    public PagedResult<Outlet> queryPageBy(KeysetPaginationCriteria keysetPaginationCriteria, List<SortCriteria> orderedSortCriterias, SearchCriteria searchCriteria) {
        ScrollPosition scrollPosition = keysetPaginationCriteria.isForward()
                ? ScrollPosition.forward(keysetPaginationCriteria.getKeyset())
                : ScrollPosition.backward(keysetPaginationCriteria.getKeyset());

        int pageSize = keysetPaginationCriteria.getPageSize();

        Sort sort = Sort.by(orderedSortCriterias.stream()
                .map(osc -> new Sort.Order(
                        osc.getDirection() == SortDirection.ASC ? Sort.Direction.ASC : Sort.Direction.DESC,
                        osc.getField()))
                .toList());
        Specification<OutletModel> searchSpecifications = OutletSpecifications.buildOutletSearchSpecification(searchCriteria);

        Window<OutletModel> windowResults = jpaOutletRepository.findBy(
                searchSpecifications,
                fluentQuery -> fluentQuery
                        .sortBy(sort)
                        .limit(pageSize)
                        .scroll(scrollPosition)
        );

        boolean hasPreviousPage = !keysetPaginationCriteria.getKeyset().isEmpty();
        return new PagedResult<>(
                windowResults.getContent().stream().map(OutletMapper.INSTANCE::toDomainEntity).toList(),
                new PageInfo(hasPreviousPage, windowResults.hasNext())
        );
    }

    @Override
    public PagedResult<Outlet> queryPageBy(String tenantId, KeysetPaginationCriteria keysetPaginationCriteria, List<SortCriteria> orderedSortCriterias, SearchCriteria searchCriteria) {
        ScrollPosition scrollPosition = keysetPaginationCriteria.isForward()
                ? ScrollPosition.forward(keysetPaginationCriteria.getKeyset())
                : ScrollPosition.backward(keysetPaginationCriteria.getKeyset());

        int pageSize = keysetPaginationCriteria.getPageSize();

        Sort sort = Sort.by(orderedSortCriterias.stream()
                .map(osc -> new Sort.Order(
                        osc.getDirection() == SortDirection.ASC ? Sort.Direction.ASC : Sort.Direction.DESC,
                        osc.getField()))
                .toList());
        Specification<OutletModel> searchSpecifications = OutletSpecifications.buildOutletSearchSpecification(searchCriteria);

        Window<OutletModel> windowResults = jpaOutletRepository.findBy(
                searchSpecifications.and(whereTenantId(tenantId)),
                fluentQuery -> fluentQuery
                        .sortBy(sort)
                        .limit(pageSize)
                        .scroll(scrollPosition)
        );

        boolean hasPreviousPage = !keysetPaginationCriteria.getKeyset().isEmpty();
        return new PagedResult<>(
                windowResults.getContent().stream().map(OutletMapper.INSTANCE::toDomainEntity).toList(),
                new PageInfo(hasPreviousPage, windowResults.hasNext())
        );
    }

    @Override
    public Map<String, List<Outlet>> findAllByEducatorIds(List<String> educatorIds) {
        if (educatorIds.isEmpty()) return Collections.emptyMap();
        List<UUID> uuids = educatorIds.stream().map(UUID::fromString).toList();
        Map<String, List<Outlet>> result = new HashMap<>();
        for (Object[] row : jpaOutletEducatorLinkRepository.findOutletsByEducatorIds(uuids)) {
            String educatorId = row[0].toString();
            OutletModel outletModel = (OutletModel) row[1];
            result.computeIfAbsent(educatorId, k -> new ArrayList<>())
                    .add(OutletMapper.INSTANCE.toDomainEntity(outletModel));
        }
        return result;
    }

    @Override
    public void deleteById(String id) {
        jpaOutletRepository.deleteById(UUID.fromString(id));
    }
}
