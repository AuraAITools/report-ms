package com.aura.details.springdata.mappers.links;

import com.aura.core.entities.links.AccountEducatorLink;
import com.aura.details.springdata.models.accounts.AccountModel;
import com.aura.details.springdata.models.educators.EducatorModel;
import com.aura.details.springdata.models.links.AccountEducatorLinkModel;
import java.util.UUID;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;


import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("AccountEducatorLinkMapper")
class AccountEducatorLinkMapperTest {

    private final AccountEducatorLinkMapper mapper = AccountEducatorLinkMapper.INSTANCE;

    @DisplayName("Should map AccountEducatorLinkModel.account.id into AccountEducatorLink.accountId without traversing back into Account")
    @Test
    void toDomain_extractsAccountIdFromModel() {
        UUID accountId = UUID.randomUUID();
        AccountModel accountModel = AccountModel.builder().id(accountId).build();
        AccountEducatorLinkModel model = AccountEducatorLinkModel.builder()
                .id(UUID.randomUUID())
                .account(accountModel)
                .build();

        AccountEducatorLink domain = mapper.toDomain(model);

        assertThat(domain.getAccountId()).isEqualTo(accountId.toString());
    }

    @DisplayName("Should map AccountEducatorLinkModel.educator.id into AccountEducatorLink.educatorId without traversing back into Educator")
    @Test
    void toDomain_extractsEducatorIdFromModel() {
        UUID educatorId = UUID.randomUUID();
        EducatorModel educatorModel = EducatorModel.builder().id(educatorId).build();
        AccountEducatorLinkModel model = AccountEducatorLinkModel.builder()
                .id(UUID.randomUUID())
                .educator(educatorModel)
                .build();

        AccountEducatorLink domain = mapper.toDomain(model);

        assertThat(domain.getEducatorId()).isEqualTo(educatorId.toString());
    }

    @DisplayName("Should leave AccountEducatorLinkModel.account null on toTable — repository impl attaches via Refs.ref")
    @Test
    void toTable_leavesAccountNullForRepositoryImplToAttach() {
        AccountEducatorLink domain = AccountEducatorLink.builder()
                .id(UUID.randomUUID().toString())
                .accountId(UUID.randomUUID().toString())
                .build();

        AccountEducatorLinkModel model = mapper.toTable(domain);

        assertThat(model.getAccount()).isNull();
    }

    @DisplayName("Should leave AccountEducatorLinkModel.educator null on toTable — repository impl attaches via Refs.ref")
    @Test
    void toTable_leavesEducatorNullForRepositoryImplToAttach() {
        AccountEducatorLink domain = AccountEducatorLink.builder()
                .id(UUID.randomUUID().toString())
                .educatorId(UUID.randomUUID().toString())
                .build();

        AccountEducatorLinkModel model = mapper.toTable(domain);

        assertThat(model.getEducator()).isNull();
    }
}
