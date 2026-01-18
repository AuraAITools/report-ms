package com.aura.details.springdata.mappers.links;

import com.aura.core.entities.links.AccountEducatorLink;
import com.aura.details.springdata.mappers.accounts.AccountMapper;
import com.aura.details.springdata.mappers.educators.EducatorMapper;
import com.aura.details.springdata.tables.links.AccountEducatorLinkTable;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(uses = {AccountMapper.class, EducatorMapper.class})
public interface AccountEducatorLinkMapper {

    AccountEducatorLinkMapper INSTANCE = Mappers.getMapper(AccountEducatorLinkMapper.class);

    AccountEducatorLink toDomain(AccountEducatorLinkTable table);

    @Mapping(target = "Id", source = "id")
    AccountEducatorLinkTable toTable(AccountEducatorLink domain);
}
