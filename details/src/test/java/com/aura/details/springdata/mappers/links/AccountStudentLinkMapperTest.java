package com.aura.details.springdata.mappers.links;

import com.aura.core.entities.links.AccountStudentLink;
import com.aura.details.springdata.models.accounts.AccountModel;
import com.aura.details.springdata.models.links.AccountStudentLinkModel;
import com.aura.details.springdata.models.students.StudentModel;
import java.util.UUID;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;


import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("AccountStudentLinkMapper")
class AccountStudentLinkMapperTest {

    private final AccountStudentLinkMapper mapper = AccountStudentLinkMapper.INSTANCE;

    @DisplayName("Should map AccountStudentLinkModel.account.id into AccountStudentLink.accountId without traversing back into Account")
    @Test
    void toDomain_extractsAccountIdFromModel() {
        UUID accountId = UUID.randomUUID();
        AccountModel accountModel = AccountModel.builder().id(accountId).build();
        AccountStudentLinkModel model = AccountStudentLinkModel.builder()
                .id(UUID.randomUUID())
                .account(accountModel)
                .build();

        AccountStudentLink domain = mapper.toDomain(model);

        assertThat(domain.getAccountId()).isEqualTo(accountId.toString());
    }

    @DisplayName("Should map AccountStudentLinkModel.student.id into AccountStudentLink.studentId without traversing back into Student")
    @Test
    void toDomain_extractsStudentIdFromModel() {
        UUID studentId = UUID.randomUUID();
        StudentModel studentModel = StudentModel.builder().id(studentId).build();
        AccountStudentLinkModel model = AccountStudentLinkModel.builder()
                .id(UUID.randomUUID())
                .student(studentModel)
                .build();

        AccountStudentLink domain = mapper.toDomain(model);

        assertThat(domain.getStudentId()).isEqualTo(studentId.toString());
    }

    @DisplayName("Should leave AccountStudentLinkModel.account null on toTable — repository impl attaches via Refs.ref")
    @Test
    void toTable_leavesAccountNullForRepositoryImplToAttach() {
        AccountStudentLink domain = AccountStudentLink.builder()
                .id(UUID.randomUUID().toString())
                .accountId(UUID.randomUUID().toString())
                .build();

        AccountStudentLinkModel model = mapper.toTable(domain);

        assertThat(model.getAccount()).isNull();
    }

    @DisplayName("Should leave AccountStudentLinkModel.student null on toTable — repository impl attaches via Refs.ref")
    @Test
    void toTable_leavesStudentNullForRepositoryImplToAttach() {
        AccountStudentLink domain = AccountStudentLink.builder()
                .id(UUID.randomUUID().toString())
                .studentId(UUID.randomUUID().toString())
                .build();

        AccountStudentLinkModel model = mapper.toTable(domain);

        assertThat(model.getStudent()).isNull();
    }
}
