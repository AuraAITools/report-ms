package com.aura.details.springdata.repositories.impl;

import com.aura.core.entities.educators.Educator;
import com.aura.core.repositories.EducatorRepository;
import com.aura.details.springdata.mappers.educators.EducatorMapper;
import com.aura.details.springdata.repositories.jpa.educators.JpaEducatorRepository;
import com.aura.details.springdata.tables.educators.EducatorTable;
import java.util.UUID;
import org.jspecify.annotations.Nullable;

public class EducatorRepositoryImpl implements EducatorRepository {
    private final JpaEducatorRepository jpaEducatorRepository;

    public EducatorRepositoryImpl(JpaEducatorRepository jpaEducatorRepository) {
        this.jpaEducatorRepository = jpaEducatorRepository;
    }

    @Override
    public @Nullable Educator findById(String id) {
        return jpaEducatorRepository.findById(UUID.fromString(id)).map(EducatorMapper.INSTANCE::toDomainEntity).orElse(null);
    }

    @Override
    public Educator save(Educator educator) {
        EducatorTable educatorTable = EducatorMapper.INSTANCE.toTableEntity(educator);
        EducatorTable createdEducatorTable = jpaEducatorRepository.save(educatorTable);
        return EducatorMapper.INSTANCE.toDomainEntity(createdEducatorTable);
    }
}
