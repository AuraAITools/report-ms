package com.aura.core.usecases.accounts;

import com.aura.core.entities.accounts.Account;
import com.aura.core.entities.accounts.Parent;
import com.aura.core.entities.educators.Educator;
import com.aura.core.entities.students.Student;
import com.aura.core.repositories.AccountRepository;
import com.aura.core.repositories.ParentRepository;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;


import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@DisplayName("Account query use case")
@ExtendWith(MockitoExtension.class)
class AccountQueryUseCaseImplTest {

    @Mock
    private AccountRepository accountRepository;
    @Mock
    private ParentRepository parentRepository;

    @InjectMocks
    private AccountQueryUseCaseImpl useCase;

    @Test
    @DisplayName("Should delegate findById to the account repository")
    void findById_delegates() {
        Account account = Account.builder().id("acc-1").build();
        when(accountRepository.findById("acc-1")).thenReturn(account);

        assertThat(useCase.findById("acc-1")).isEqualTo(account);
    }

    @Test
    @DisplayName("Should delegate findAllByTenantId to the account repository")
    void findAllByTenantId_delegates() {
        Account a1 = Account.builder().id("acc-1").tenantId("tenant-1").build();
        Account a2 = Account.builder().id("acc-2").tenantId("tenant-1").build();
        when(accountRepository.findAllByTenantId("tenant-1")).thenReturn(List.of(a1, a2));

        assertThat(useCase.findAllByTenantId("tenant-1")).containsExactly(a1, a2);
    }

    @Test
    @DisplayName("Should delegate findStudentsByAccountIds to the account repository")
    void findStudentsByAccountIds_delegates() {
        Map<String, List<Student>> expected = Map.of("acc-1", List.of(Student.builder().id("stu-1").build()));
        when(accountRepository.findStudentsByAccountIds(List.of("acc-1"))).thenReturn(expected);

        assertThat(useCase.findStudentsByAccountIds(List.of("acc-1"))).isEqualTo(expected);
    }

    @Test
    @DisplayName("Should delegate findEducatorsByAccountIds to the account repository")
    void findEducatorsByAccountIds_delegates() {
        Map<String, List<Educator>> expected = Map.of("acc-1", List.of(Educator.builder().id("edu-1").build()));
        when(accountRepository.findEducatorsByAccountIds(List.of("acc-1"))).thenReturn(expected);

        assertThat(useCase.findEducatorsByAccountIds(List.of("acc-1"))).isEqualTo(expected);
    }

    @Test
    @DisplayName("Should delegate findParentsByAccountIds to the parent repository")
    void findParentsByAccountIds_delegates() {
        Map<String, Parent> expected = Map.of("acc-1", Parent.builder().id("par-1").build());
        when(parentRepository.findAllByAccountIds(List.of("acc-1"))).thenReturn(expected);

        assertThat(useCase.findParentsByAccountIds(List.of("acc-1"))).isEqualTo(expected);
    }
}
