package com.aura.details.repositories.impl;

import com.aura.core.entities.institutions.Institution;
import com.aura.core.repositories.InstitutionRepository;
import com.aura.core.utils.KeysetPaginationCriteria;
import com.aura.core.utils.PageInfo;
import com.aura.core.utils.PagedResult;
import com.aura.core.utils.SearchCriteria;
import com.aura.core.utils.SortCriteria;
import com.aura.core.utils.SortDirection;
import com.aura.details.mappers.institutions.InstitutionMapper;
import com.aura.details.repositories.jpa.institutions.JpaInstitutionRepository;
import com.aura.details.repositories.specifications.InstitutionSpecifications;
import com.aura.details.tables.institutions.InstitutionTable;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.domain.ScrollPosition;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Window;
import org.springframework.data.jpa.domain.Specification;

public class InstitutionRepositoryImpl implements InstitutionRepository {
    public final JpaInstitutionRepository jpaRepository;

    public InstitutionRepositoryImpl(JpaInstitutionRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public Institution save(Institution institution) {
        InstitutionTable institutionTable = InstitutionMapper.INSTANCE.toTableEntity(institution);
        InstitutionTable saved = jpaRepository.save(institutionTable);
        return InstitutionMapper.INSTANCE.toDomainEntity(saved);
    }

    @Override
    public Optional<Institution> findById(String id) {
        Optional<InstitutionTable> optionalInstitutionTable = jpaRepository.findById(UUID.fromString(id));
        return optionalInstitutionTable.map(InstitutionMapper.INSTANCE::toDomainEntity);
    }

    @Override
    public List<Institution> findAll() {
        List<InstitutionTable> institutionTables = jpaRepository.findAll();
        return institutionTables.stream().map(InstitutionMapper.INSTANCE::toDomainEntity).toList();
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

        Specification<InstitutionTable> searchSpecifications = InstitutionSpecifications.buildInstitutionSearchSpecification(searchCriteria);

        Window<InstitutionTable> windowResults = jpaRepository.findBy(
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

}
