package com.aura.details.springdata.repositories.impl;

import com.aura.core.entities.outlets.OutletRoom;
import com.aura.core.repositories.OutletRoomRepository;
import com.aura.details.springdata.mappers.outlets.OutletRoomMapper;
import com.aura.details.springdata.models.outlets.OutletModel;
import com.aura.details.springdata.models.outlets.OutletRoomModel;
import com.aura.details.springdata.repositories.jpa.outlets.JpaOutletRoomRepository;
import jakarta.persistence.EntityManager;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.Nullable;

@RequiredArgsConstructor
public class OutletRoomRepositoryImpl implements OutletRoomRepository {
    private final JpaOutletRoomRepository jpaOutletRoomRepository;
    private final EntityManager entityManager;

    @Override
    public OutletRoom save(OutletRoom outletRoom) {
        OutletRoomModel model = OutletRoomMapper.INSTANCE.toModelEntity(outletRoom);
        model.setOutlet(Refs.ref(entityManager, OutletModel.class, outletRoom.getOutletId()));
        OutletRoomModel saved = jpaOutletRoomRepository.save(model);
        return OutletRoomMapper.INSTANCE.toDomainEntity(saved);
    }

    @Override
    public @Nullable OutletRoom findById(String id) {
        return jpaOutletRoomRepository.findById(UUID.fromString(id))
                .map(OutletRoomMapper.INSTANCE::toDomainEntity)
                .orElse(null);
    }

    @Override
    public List<OutletRoom> findAllByIds(List<UUID> ids) {
        if (ids.isEmpty()) {
            return List.of();
        }
        return jpaOutletRoomRepository.findAllById(ids).stream()
                .map(OutletRoomMapper.INSTANCE::toDomainEntity)
                .toList();
    }

    @Override
    public List<OutletRoom> findAllByTenantId(String tenantId) {
        return jpaOutletRoomRepository.findAllByTenantId(tenantId).stream()
                .map(OutletRoomMapper.INSTANCE::toDomainEntity)
                .toList();
    }

    @Override
    public List<OutletRoom> findAllByOutletId(String outletId) {
        return jpaOutletRoomRepository.findAllByOutletId(UUID.fromString(outletId)).stream()
                .map(OutletRoomMapper.INSTANCE::toDomainEntity)
                .toList();
    }

    @Override
    public void deleteById(String id) {
        jpaOutletRoomRepository.deleteById(UUID.fromString(id));
    }
}
