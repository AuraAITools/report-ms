package com.aura.details.springdata.mappers.links;

import com.aura.core.entities.links.AccountEducatorLink;
import com.aura.details.springdata.mappers.accounts.AccountMapper;
import com.aura.details.springdata.mappers.educators.EducatorMapper;
import com.aura.details.springdata.models.links.AccountEducatorLinkModel;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(uses = {AccountMapper.class, EducatorMapper.class})
public interface AccountEducatorLinkMapper {

    AccountEducatorLinkMapper INSTANCE = Mappers.getMapper(AccountEducatorLinkMapper.class);

    AccountEducatorLink toDomain(AccountEducatorLinkModel table);


    AccountEducatorLinkModel toTable(AccountEducatorLink domain);
}
