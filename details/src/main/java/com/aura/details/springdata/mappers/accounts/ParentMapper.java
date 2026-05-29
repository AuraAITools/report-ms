package com.aura.details.springdata.mappers.accounts;

import com.aura.core.entities.accounts.Parent;
import com.aura.details.springdata.models.accounts.ParentModel;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface ParentMapper {
    ParentMapper INSTANCE = Mappers.getMapper(ParentMapper.class);

    @Mapping(source = "account.id", target = "accountId")
    Parent toDomainEntity(ParentModel parentModel);

    // account is attached separately by ParentRepositoryImpl via Refs.ref(entityManager, ...),
    // because a builder-stub AccountModel (id set, @Version null) trips Hibernate's
    // isTransient check ("Detached entity ... uninitialized version value 'null'") during persist.
    @Mapping(target = "account", ignore = true)
    ParentModel toModelEntity(Parent parent);
}
