package com.aura.details.springdata.repositories.impl;

import com.aura.core.entities.outlets.Outlet;
import com.aura.core.repositories.OutletRepository;
import com.aura.details.springdata.mappers.outlets.OutletMapper;
import com.aura.details.springdata.repositories.jpa.outlets.JpaOutletRepository;
import com.aura.details.springdata.tables.outlets.OutletTable;
import java.util.Optional;
import java.util.UUID;

public class OutletRepositoryImpl implements OutletRepository {
    private final JpaOutletRepository jpaOutletRepository;

    public OutletRepositoryImpl(JpaOutletRepository jpaOutletRepository) {
        this.jpaOutletRepository = jpaOutletRepository;
    }

    @Override
    public Optional<Outlet> findById(String id) {
        Optional<OutletTable> outletTable = jpaOutletRepository.findById(UUID.fromString(id));
        return outletTable.map(OutletMapper.INSTANCE::toDomainEntity);
    }

    @Override
    public Outlet save(Outlet outlet) {
        OutletTable outletTable = OutletMapper.INSTANCE.toTableEntity(outlet);
        OutletTable createdTable = jpaOutletRepository.save(outletTable);
        return OutletMapper.INSTANCE.toDomainEntity(createdTable);
    }
}
