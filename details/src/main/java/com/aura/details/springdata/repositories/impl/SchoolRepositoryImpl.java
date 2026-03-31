package com.aura.details.springdata.repositories.impl;

import com.aura.core.entities.schools.School;
import com.aura.core.repositories.SchoolRepository;
import com.aura.details.springdata.mappers.schools.SchoolMapper;
import com.aura.details.springdata.repositories.jpa.schools.JpaSchoolRepository;
import com.aura.details.springdata.tables.schools.SchoolTable;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.Nullable;


import static java.util.Optional.ofNullable;

@RequiredArgsConstructor
public class SchoolRepositoryImpl implements SchoolRepository {
    private final JpaSchoolRepository jpaSchoolRepository;

    @Override
    public @Nullable School findByNameAndTenantId(String name, String tenantId) {
        Optional<SchoolTable> schoolTable = ofNullable(jpaSchoolRepository.findByNameAndTenantId(name, tenantId));
        return schoolTable.map(SchoolMapper.INSTANCE::toDomainEntity).orElse(null);
    }
}
