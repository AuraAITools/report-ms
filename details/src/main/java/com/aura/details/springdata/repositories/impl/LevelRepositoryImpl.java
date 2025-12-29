package com.aura.details.springdata.repositories.impl;

import com.aura.core.entities.levels.Level;
import com.aura.core.repositories.LevelRepository;
import com.aura.core.utils.KeysetPaginationCriteria;
import com.aura.core.utils.PageInfo;
import com.aura.core.utils.PagedResult;
import com.aura.core.utils.SearchCriteria;
import com.aura.core.utils.SortCriteria;
import com.aura.core.utils.SortDirection;
import com.aura.details.springdata.mappers.levels.LevelMapper;
import com.aura.details.springdata.repositories.jpa.levels.JpaLevelRepository;
import com.aura.details.springdata.specifications.LevelSpecification;
import com.aura.details.springdata.tables.levels.LevelTable;
import java.util.List;
import org.springframework.data.domain.ScrollPosition;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Window;
import org.springframework.data.jpa.domain.Specification;

public class LevelRepositoryImpl implements LevelRepository {
    JpaLevelRepository jpaLevelRepository;

    public LevelRepositoryImpl(JpaLevelRepository jpaLevelRepository) {
        this.jpaLevelRepository = jpaLevelRepository;
    }

    @Override
    public PagedResult<Level> findAllBy(String tenantId, KeysetPaginationCriteria keysetPaginationCriteria, List<SortCriteria> orderedSortCriterias, SearchCriteria searchCriteria) {
        ScrollPosition scrollPosition = keysetPaginationCriteria.isForward()
                ? ScrollPosition.forward(keysetPaginationCriteria.getKeyset())
                : ScrollPosition.backward(keysetPaginationCriteria.getKeyset());

        int pageSize = keysetPaginationCriteria.getPageSize();

        Sort sort = Sort.by(orderedSortCriterias.stream()
                .map(osc -> new Sort.Order(
                        osc.getDirection() == SortDirection.ASC ? Sort.Direction.ASC : Sort.Direction.DESC,
                        osc.getField()))
                .toList());

        Specification<LevelTable> searchSpecifications = LevelSpecification.buildLevelSearchSpecification(searchCriteria, tenantId);

        Window<LevelTable> windowResults = jpaLevelRepository.findBy(
                searchSpecifications,
                fluentQuery -> fluentQuery
                        .sortBy(sort)
                        .limit(pageSize)
                        .scroll(scrollPosition)
        );

        boolean hasPreviousPage = !keysetPaginationCriteria.getKeyset().isEmpty();
        return new PagedResult<>(
                windowResults.getContent().stream().map(LevelMapper.INSTANCE::toDomainEntity).toList(),
                new PageInfo(hasPreviousPage, windowResults.hasNext()));
    }
}
