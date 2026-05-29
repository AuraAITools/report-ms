package com.aura.details.springdata.repositories.impl;

import com.aura.core.entities.accounts.Parent;
import com.aura.core.repositories.ParentRepository;
import com.aura.details.springdata.mappers.accounts.ParentMapper;
import com.aura.details.springdata.models.accounts.AccountModel;
import com.aura.details.springdata.models.accounts.ParentModel;
import com.aura.details.springdata.repositories.jpa.accounts.JpaParentRepository;
import jakarta.persistence.EntityManager;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.Nullable;

@RequiredArgsConstructor
public class ParentRepositoryImpl implements ParentRepository {
    private final JpaParentRepository jpaParentRepository;
    private final EntityManager entityManager;

    @Override
    public Parent save(Parent parent) {
        var model = toAttachedModel(parent);
        var saved = jpaParentRepository.save(model);
        return ParentMapper.INSTANCE.toDomainEntity(saved);
    }

    @Override
    public List<Parent> saveAll(List<Parent> parents) {
        var models = parents.stream().map(this::toAttachedModel).toList();
        return jpaParentRepository.saveAll(models).stream().map(ParentMapper.INSTANCE::toDomainEntity).toList();
    }

    @Override
    public @Nullable Parent findById(String id) {
        return jpaParentRepository.findById(UUID.fromString(id)).map(ParentMapper.INSTANCE::toDomainEntity).orElse(null);
    }

    @Override
    public Map<String, Parent> findAllByAccountIds(List<String> accountIds) {
        if (accountIds.isEmpty()) return Collections.emptyMap();
        List<UUID> uuids = accountIds.stream().map(UUID::fromString).toList();
        Map<String, Parent> result = new HashMap<>();
        for (Object[] row : jpaParentRepository.findByAccountIds(uuids)) {
            String accountId = row[0].toString();
            ParentModel parentModel = (ParentModel) row[1];
            result.put(accountId, ParentMapper.INSTANCE.toDomainEntity(parentModel));
        }
        return result;
    }

    @Override
    public List<Parent> findAllByTenantId(String tenantId) {
        return jpaParentRepository.findByTenantId(tenantId).stream()
                .map(ParentMapper.INSTANCE::toDomainEntity)
                .toList();
    }

    @Override
    public void deleteById(String id) {
        jpaParentRepository.deleteById(UUID.fromString(id));
    }

    private ParentModel toAttachedModel(Parent parent) {
        ParentModel model = ParentMapper.INSTANCE.toModelEntity(parent);
        model.setAccount(Refs.ref(entityManager, AccountModel.class, parent.getAccountId()));
        return model;
    }
}
