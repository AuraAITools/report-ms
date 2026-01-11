package com.aura.details.springdata.mappers.links;

import com.aura.core.entities.links.AccountStudentLink;
import com.aura.details.springdata.mappers.accounts.AccountMapper;
import com.aura.details.springdata.mappers.students.StudentMapper;
import com.aura.details.springdata.tables.links.AccountStudentLinkTable;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(uses = {StudentMapper.class, AccountMapper.class})
public interface AccountStudentLinkMapper {

    AccountStudentLinkMapper INSTANCE = Mappers.getMapper(AccountStudentLinkMapper.class);

    AccountStudentLink toDomain(AccountStudentLinkTable table);

    @Mapping(target = "Id", source = "id")
    AccountStudentLinkTable toTable(AccountStudentLink domain);
}
