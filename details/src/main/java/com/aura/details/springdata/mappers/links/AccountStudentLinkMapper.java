package com.aura.details.springdata.mappers.links;

import com.aura.core.entities.links.AccountStudentLink;
import com.aura.details.springdata.mappers.accounts.AccountMapper;
import com.aura.details.springdata.mappers.students.StudentMapper;
import com.aura.details.springdata.models.links.AccountStudentLinkModel;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(uses = {StudentMapper.class, AccountMapper.class})
public interface AccountStudentLinkMapper {

    AccountStudentLinkMapper INSTANCE = Mappers.getMapper(AccountStudentLinkMapper.class);

    AccountStudentLink toDomain(AccountStudentLinkModel table);


    AccountStudentLinkModel toTable(AccountStudentLink domain);
}
