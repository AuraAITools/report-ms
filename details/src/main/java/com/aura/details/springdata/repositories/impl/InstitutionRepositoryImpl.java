package com.aura.details.springdata.repositories.impl;

import com.aura.core.entities.institutions.Institution;
import com.aura.core.repositories.InstitutionRepository;
import com.aura.core.utils.KeysetPaginationCriteria;
import com.aura.core.utils.PageInfo;
import com.aura.core.utils.PagedResult;
import com.aura.core.utils.SearchCriteria;
import com.aura.core.utils.SortCriteria;
import com.aura.core.utils.SortDirection;
import com.aura.details.springdata.mappers.institutions.InstitutionMapper;
import com.aura.details.springdata.models.institutions.InstitutionModel;
import com.aura.details.springdata.repositories.jpa.institutions.JpaInstitutionRepository;
import com.aura.details.springdata.specifications.InstitutionSpecifications;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import org.jspecify.annotations.Nullable;
import org.springframework.data.domain.ScrollPosition;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Window;
import org.springframework.data.jpa.domain.Specification;


import static com.aura.details.springdata.specifications.TenantSpecification.whereTenantId;

public class InstitutionRepositoryImpl implements InstitutionRepository {
    public final JpaInstitutionRepository jpaRepository;

    public InstitutionRepositoryImpl(JpaInstitutionRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public Institution save(Institution institution) {
        InstitutionModel institutionModel = InstitutionMapper.INSTANCE.toModelEntity(institution);
        InstitutionModel saved = jpaRepository.save(institutionModel);
        return InstitutionMapper.INSTANCE.toDomainEntity(saved);
    }

    @Override
    public @Nullable Institution findById(String id) {
        Optional<InstitutionModel> optionalInstitutionModel = jpaRepository.findById(UUID.fromString(id));
        return optionalInstitutionModel.map(InstitutionMapper.INSTANCE::toDomainEntity).orElse(null);
    }

    @Override
    public List<Institution> findAll() {
        List<InstitutionModel> institutionModels = jpaRepository.findAll();
        return institutionModels.stream().map(InstitutionMapper.INSTANCE::toDomainEntity).toList();
    }

    @Override
    public List<Institution> findAllByIds(Set<String> ids) {
        List<UUID> uuids = ids.stream().map(UUID::fromString).toList();
        return jpaRepository.findAllById(uuids).stream()
                .map(InstitutionMapper.INSTANCE::toDomainEntity)
                .toList();
    }

    @Override
    public PagedResult<Institution> findAllBy(KeysetPaginationCriteria keysetPaginationCriteria, List<SortCriteria> orderedSortCriterias, SearchCriteria searchCriteria) {
        ScrollPosition scrollPosition = keysetPaginationCriteria.isForward()
                ? ScrollPosition.forward(keysetPaginationCriteria.getKeyset())
                : ScrollPosition.backward(keysetPaginationCriteria.getKeyset());

        int pageSize = keysetPaginationCriteria.getPageSize();

        Sort sort = Sort.by(orderedSortCriterias.stream()
                .map(osc -> new Sort.Order(
                        osc.getDirection() == SortDirection.ASC ? Sort.Direction.ASC : Sort.Direction.DESC,
                        osc.getField()))
                .toList());

        Specification<InstitutionModel> searchSpecifications = InstitutionSpecifications.buildInstitutionSearchSpecification(searchCriteria);

        Window<InstitutionModel> windowResults = jpaRepository.findBy(
                searchSpecifications,
                fluentQuery -> fluentQuery
                        .sortBy(sort)
                        .limit(pageSize)
                        .scroll(scrollPosition)
        );

        boolean hasPreviousPage = !keysetPaginationCriteria.getKeyset().isEmpty();
        return new PagedResult<>(
                windowResults.getContent().stream().map(InstitutionMapper.INSTANCE::toDomainEntity).toList(),
                new PageInfo(hasPreviousPage, windowResults.hasNext())
        );
    }

    @Override
    public PagedResult<Institution> findAllByTenantId(String tenantId, KeysetPaginationCriteria keysetPaginationCriteria, List<SortCriteria> orderedSortCriterias, SearchCriteria searchCriteria) {
        ScrollPosition scrollPosition = keysetPaginationCriteria.isForward()
                ? ScrollPosition.forward(keysetPaginationCriteria.getKeyset())
                : ScrollPosition.backward(keysetPaginationCriteria.getKeyset());

        int pageSize = keysetPaginationCriteria.getPageSize();

        Sort sort = Sort.by(orderedSortCriterias.stream()
                .map(osc -> new Sort.Order(
                        osc.getDirection() == SortDirection.ASC ? Sort.Direction.ASC : Sort.Direction.DESC,
                        osc.getField()))
                .toList());

        Specification<InstitutionModel> searchSpecifications = InstitutionSpecifications.buildInstitutionSearchSpecification(searchCriteria);

        Window<InstitutionModel> windowResults = jpaRepository.findBy(
                searchSpecifications.and(whereTenantId(tenantId)),
                fluentQuery -> fluentQuery
                        .sortBy(sort)
                        .limit(pageSize)
                        .scroll(scrollPosition)
        );

        boolean hasPreviousPage = !keysetPaginationCriteria.getKeyset().isEmpty();
        return new PagedResult<>(
                windowResults.getContent().stream().map(InstitutionMapper.INSTANCE::toDomainEntity).toList(),
                new PageInfo(hasPreviousPage, windowResults.hasNext())
        );
    }
}
