package com.aura.details.springdata.repositories.impl;

import com.aura.core.entities.schools.School;
import com.aura.core.repositories.SchoolRepository;
import com.aura.details.springdata.mappers.schools.SchoolMapper;
import com.aura.details.springdata.models.schools.SchoolModel;
import com.aura.details.springdata.repositories.jpa.schools.JpaSchoolRepository;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.Nullable;


import static java.util.Optional.ofNullable;

@RequiredArgsConstructor
public class SchoolRepositoryImpl implements SchoolRepository {
    private final JpaSchoolRepository jpaSchoolRepository;

    @Override
    public @Nullable School findByNameAndTenantId(String name, String tenantId) {
        Optional<SchoolModel> schoolModel = ofNullable(jpaSchoolRepository.findByNameAndTenantId(name, tenantId));
        return schoolModel.map(SchoolMapper.INSTANCE::toDomainEntity).orElse(null);
    }

    @Override
    public List<School> findAllByTenantId(String tenantId) {
        return jpaSchoolRepository.findByTenantId(tenantId).stream()
                .map(SchoolMapper.INSTANCE::toDomainEntity)
                .toList();
    }

    @Override
    public @Nullable School findById(String id) {
        return jpaSchoolRepository.findById(UUID.fromString(id))
                .map(SchoolMapper.INSTANCE::toDomainEntity)
                .orElse(null);
    }

    @Override
    public School save(School school) {
        SchoolModel saved = jpaSchoolRepository.save(SchoolMapper.INSTANCE.toModelEntity(school));
        return SchoolMapper.INSTANCE.toDomainEntity(saved);
    }

    @Override
    public void deleteById(String id) {
        jpaSchoolRepository.deleteById(UUID.fromString(id));
    }
}
