package com.aura.details.springdata.mappers.links;

import com.aura.core.entities.links.AccountEducatorLink;
import com.aura.details.springdata.models.links.AccountEducatorLinkModel;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface AccountEducatorLinkMapper {

    AccountEducatorLinkMapper INSTANCE = Mappers.getMapper(AccountEducatorLinkMapper.class);

    @Mapping(source = "account.id", target = "accountId")
    @Mapping(source = "educator.id", target = "educatorId")
    AccountEducatorLink toDomain(AccountEducatorLinkModel model);

    // account and educator are attached separately by the repository impl via Refs.ref(entityManager, ...),
    // because a builder-stub *Model (id set, @Version null) trips Hibernate's
    // isTransient check ("Detached entity ... uninitialized version value 'null'") during persist.
    @Mapping(target = "account", ignore = true)
    @Mapping(target = "educator", ignore = true)
    AccountEducatorLinkModel toTable(AccountEducatorLink link);
}
