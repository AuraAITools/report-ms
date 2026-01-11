package com.aura.details.springdata.repositories.impl;

import com.aura.core.entities.schools.School;
import com.aura.core.repositories.SchoolRepository;
import com.aura.details.springdata.mappers.schools.SchoolMapper;
import com.aura.details.springdata.repositories.jpa.schools.JpaSchoolRepository;
import com.aura.details.springdata.tables.schools.SchoolTable;

public class SchoolRepositoryImpl implements SchoolRepository {
    private final JpaSchoolRepository jpaSchoolRepository;

    public SchoolRepositoryImpl(JpaSchoolRepository jpaSchoolRepository) {
        this.jpaSchoolRepository = jpaSchoolRepository;
    }

    @Override
    public School findByNameAndTenantId(String name, String tenantId) {
        SchoolTable schoolTable = jpaSchoolRepository.findByNameAndTenantId(name, tenantId);
        return SchoolMapper.INSTANCE.toDomainEntity(schoolTable);
    }
}
