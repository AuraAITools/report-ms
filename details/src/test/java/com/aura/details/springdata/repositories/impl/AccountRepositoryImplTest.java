package com.aura.details.springdata.repositories.impl;

import com.aura.core.entities.accounts.Account;
import com.aura.details.springdata.models.accounts.AccountModel;
import com.aura.details.springdata.repositories.jpa.accounts.JpaAccountRepository;
import com.aura.details.springdata.repositories.jpa.educators.JpaEducatorRepository;
import com.aura.details.springdata.repositories.jpa.links.JpaAccountEducatorLinkRepository;
import com.aura.details.springdata.repositories.jpa.links.JpaAccountStudentLinkRepository;
import com.aura.details.springdata.repositories.jpa.students.JpaStudentRepository;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;


import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

@DisplayName("Account repository implementation")
@ExtendWith(MockitoExtension.class)
class AccountRepositoryImplTest {

    @Mock
    private JpaAccountRepository jpaAccountRepository;
    @Mock
    private JpaAccountStudentLinkRepository jpaAccountStudentLinkRepository;
    @Mock
    private JpaAccountEducatorLinkRepository jpaAccountEducatorLinkRepository;
    @Mock
    private JpaStudentRepository jpaStudentRepository;
    @Mock
    private JpaEducatorRepository jpaEducatorRepository;

    @InjectMocks
    private AccountRepositoryImpl accountRepositoryImpl;

    @DisplayName("Should return empty map when no educator ids provided to findAllByEducatorIds")
    @Test
    void findAllByEducatorIds_emptyList_returnsEmptyMap() {
        assertThat(accountRepositoryImpl.findAllByEducatorIds(List.of())).isEmpty();
        verifyNoInteractions(jpaAccountEducatorLinkRepository);
    }

    @DisplayName("Should map each educator id to its single account")
    @Test
    void findAllByEducatorIds_mapsEducatorIdToSingleAccount() {
        UUID educatorId1 = UUID.randomUUID();
        UUID educatorId2 = UUID.randomUUID();
        UUID accountId1 = UUID.randomUUID();
        UUID accountId2 = UUID.randomUUID();
        AccountModel a1 = AccountModel.builder().id(accountId1).email("a1@e.com").build();
        AccountModel a2 = AccountModel.builder().id(accountId2).email("a2@e.com").build();

        when(jpaAccountEducatorLinkRepository.findAccountsByEducatorIds(List.of(educatorId1, educatorId2)))
                .thenReturn(List.<Object[]>of(
                        new Object[]{educatorId1, a1},
                        new Object[]{educatorId2, a2}
                ));

        Map<String, Account> result = accountRepositoryImpl.findAllByEducatorIds(
                List.of(educatorId1.toString(), educatorId2.toString()));

        assertThat(result).hasSize(2);
        assertThat(result.get(educatorId1.toString()).getEmail()).isEqualTo("a1@e.com");
        assertThat(result.get(educatorId2.toString()).getEmail()).isEqualTo("a2@e.com");
    }

    @Test
    @DisplayName("Should batch-load accounts for the given userIds within the tenant")
    void findAllByUserIdsAndTenantId_returnsAccountsKeyedByUserId() {
        String tenantId = "tenant-1";
        AccountModel m1 = AccountModel.builder()
                .id(UUID.fromString("00000000-0000-0000-0000-000000000001"))
                .userId("user-a")
                .firstName("Alice")
                .lastName("A")
                .build();
        AccountModel m2 = AccountModel.builder()
                .id(UUID.fromString("00000000-0000-0000-0000-000000000002"))
                .userId("user-b")
                .firstName("Bob")
                .lastName("B")
                .build();

        when(jpaAccountRepository.findAllByUserIdInAndTenantId(List.of("user-a", "user-b"), tenantId))
                .thenReturn(List.of(m1, m2));

        Map<String, Account> result = accountRepositoryImpl.findAllByUserIdsAndTenantId(
                List.of("user-a", "user-b"), tenantId);

        assertThat(result).hasSize(2);
        assertThat(result.get("user-a").getUserId()).isEqualTo("user-a");
        assertThat(result.get("user-b").getUserId()).isEqualTo("user-b");
    }

    @Test
    @DisplayName("Should return an empty map when no userIds are provided")
    void findAllByUserIdsAndTenantId_emptyInput_returnsEmptyMap() {
        Map<String, Account> result = accountRepositoryImpl.findAllByUserIdsAndTenantId(List.of(), "tenant-1");

        assertThat(result).isEmpty();
        verifyNoInteractions(jpaAccountRepository);
    }
}
