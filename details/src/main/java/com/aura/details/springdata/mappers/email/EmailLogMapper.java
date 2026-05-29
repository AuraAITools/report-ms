package com.aura.details.springdata.mappers.email;

import com.aura.core.entities.email.EmailLog;
import com.aura.details.springdata.models.email.EmailLogModel;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface EmailLogMapper {
    EmailLogMapper INSTANCE = Mappers.getMapper(EmailLogMapper.class);

    EmailLog toDomainEntity(EmailLogModel model);

    EmailLogModel toModelEntity(EmailLog domain);
}
