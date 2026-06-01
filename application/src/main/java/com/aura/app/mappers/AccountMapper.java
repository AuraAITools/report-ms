package com.aura.app.mappers;

import com.aura.app.codegen.types.AccountResponse;
import com.aura.app.codegen.types.CreateAccountInput;
import com.aura.core.entities.accounts.Account;
import com.aura.core.entities.accounts.AccountFeature;
import com.aura.core.entities.accounts.AccountFeatureState;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(uses = {DateToEpochMapper.class, AccountMinimalDetailMapper.class})
public interface AccountMapper {
    AccountMapper INSTANCE = Mappers.getMapper(AccountMapper.class);

    @Mapping(target = "parent", ignore = true)
    @Mapping(target = "state", constant = "VERIFYING")
    @Mapping(target = "version", ignore = true)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "updatedBy", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "userId", ignore = true)
    @Mapping(target = "parentFeatureState", constant = "NOT_ONBOARDED")
    @Mapping(target = "educatorFeatureState", constant = "NOT_ONBOARDED")
    @Mapping(target = "staffFeatureState", constant = "NOT_ONBOARDED")
    @Mapping(target = "email", source = "email")
    @Mapping(source = "institutionId", target = "tenantId")
    Account toDomainEntity(CreateAccountInput input);

    @Mapping(target = "students", ignore = true)
    @Mapping(target = "_parent", ignore = true)
    @Mapping(target = "educators", ignore = true)
    @Mapping(target = "profileImageUrl", ignore = true)
    @Mapping(target = "status", source = "state")
    @Mapping(target = "createdBy", source = "createdBy", qualifiedByName = "toMinimal")
    @Mapping(target = "updatedBy", source = "updatedBy", qualifiedByName = "toMinimal")
    AccountResponse toDto(Account account);

    AccountFeature toDomain(com.aura.app.codegen.types.AccountFeature feature);

    AccountFeatureState toDomain(com.aura.app.codegen.types.AccountFeatureState state);
}
