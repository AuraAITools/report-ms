package com.aura.details.springdata.mappers.links;

import com.aura.core.entities.links.AccountStudentLink;
import com.aura.details.springdata.models.links.AccountStudentLinkModel;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface AccountStudentLinkMapper {

    AccountStudentLinkMapper INSTANCE = Mappers.getMapper(AccountStudentLinkMapper.class);

    @Mapping(source = "account.id", target = "accountId")
    @Mapping(source = "student.id", target = "studentId")
    AccountStudentLink toDomain(AccountStudentLinkModel model);

    // account and student are attached separately by the repository impl via Refs.ref(entityManager, ...),
    // because a builder-stub *Model (id set, @Version null) trips Hibernate's
    // isTransient check ("Detached entity ... uninitialized version value 'null'") during persist.
    @Mapping(target = "account", ignore = true)
    @Mapping(target = "student", ignore = true)
    AccountStudentLinkModel toTable(AccountStudentLink link);
}
