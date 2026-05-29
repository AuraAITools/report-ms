package com.aura.details.springdata.mappers.accounts;

import com.aura.core.entities.accounts.Parent;
import com.aura.details.springdata.models.accounts.AccountModel;
import com.aura.details.springdata.models.accounts.ParentModel;
import java.util.UUID;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;


import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("ParentMapper")
class ParentMapperTest {

    private final ParentMapper mapper = ParentMapper.INSTANCE;

    @DisplayName("Should map ParentModel.account.id into Parent.accountId without traversing back into Account")
    @Test
    void toDomainEntity_extractsAccountIdFromModel() {
        UUID accountId = UUID.randomUUID();
        AccountModel accountModel = AccountModel.builder().id(accountId).build();
        ParentModel parentModel = ParentModel.builder()
                .id(UUID.randomUUID())
                .relationship(ParentModel.RELATIONSHIP.PARENT)
                .account(accountModel)
                .build();

        Parent parent = mapper.toDomainEntity(parentModel);

        assertThat(parent.getAccountId()).isEqualTo(accountId.toString());
        assertThat(parent.getRelationship()).isEqualTo(Parent.RELATIONSHIP.PARENT);
    }

    @DisplayName("Should leave ParentModel.account null on toModelEntity — ParentRepositoryImpl attaches via Refs.ref")
    @Test
    void toModelEntity_leavesAccountNullForRepositoryImplToAttach() {
        Parent parent = Parent.builder()
                .id(UUID.randomUUID().toString())
                .accountId(UUID.randomUUID().toString())
                .relationship(Parent.RELATIONSHIP.PARENT)
                .build();

        ParentModel parentModel = mapper.toModelEntity(parent);

        assertThat(parentModel.getAccount()).isNull();
        assertThat(parentModel.getRelationship()).isEqualTo(ParentModel.RELATIONSHIP.PARENT);
    }
}
