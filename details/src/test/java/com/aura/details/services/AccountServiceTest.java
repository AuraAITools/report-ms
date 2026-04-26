package com.aura.details.services;

import com.aura.core.clients.IdentityService;
import com.aura.core.clients.RelationshipAuthorizationService;
import com.aura.core.entities.accounts.Account;
import com.aura.core.repositories.AccountRepository;
import com.aura.core.usecases.accounts.AssignUserRoleUseCase;
import com.aura.core.usecases.accounts.CreateAccountUseCase;
import com.aura.core.usecases.accounts.CreateEducatorInAccountUseCase;
import com.aura.core.usecases.accounts.CreateParentAccountUseCase;
import com.aura.core.usecases.accounts.CreateStudentsInParentAccountUseCase;
import com.aura.core.usecases.courses.EnrollStudentToCourseUseCase;
import com.aura.core.usecases.courses.TagEducatorToCourseUseCase;
import com.aura.core.usecases.educators.EnrollEducatorToOutletUseCase;
import com.aura.core.usecases.educators.TagEducatorToLevelUseCase;
import com.aura.core.usecases.educators.TagEducatorToSubjectUseCase;
import com.aura.core.usecases.students.TagStudentToLevelUseCase;
import com.aura.core.usecases.students.TagStudentToSchoolUseCase;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@DisplayName("Account service")
@ExtendWith(MockitoExtension.class)
class AccountServiceTest {

    @Mock private CreateAccountUseCase createAccountUseCase;
    @Mock private CreateParentAccountUseCase createParentAccountUseCase;
    @Mock private CreateStudentsInParentAccountUseCase createStudentsInParentAccountUseCase;
    @Mock private TagStudentToLevelUseCase tagStudentToLevelUseCase;
    @Mock private TagStudentToSchoolUseCase tagStudentToSchoolUseCase;
    @Mock private EnrollStudentToCourseUseCase enrollStudentToCourseUseCase;
    @Mock private CreateEducatorInAccountUseCase createEducatorInAccountUseCase;
    @Mock private TagEducatorToCourseUseCase tagEducatorToCourseUseCase;
    @Mock private TagEducatorToLevelUseCase tagEducatorToLevelUseCase;
    @Mock private TagEducatorToSubjectUseCase tagEducatorToSubjectUseCase;
    @Mock private EnrollEducatorToOutletUseCase enrollEducatorToOutletUseCase;
    @Mock private AccountRepository accountRepository;
    @Mock private IdentityService identityService;
    @Mock private RelationshipAuthorizationService relationshipService;
    @Mock private AssignUserRoleUseCase assignUserRoleUseCase;

    @InjectMocks private AccountService accountService;

    @DisplayName("Should create account and write admin relationship to institution")
    @Test
    void createAccountWithInstitutionAdminRole_writesAdminRelationship() {
        Account input = Account.builder().tenantId("tenant-123").build();
        Account created = Account.builder().userId("user-456").tenantId("tenant-123").build();
        when(createAccountUseCase.createAccount(input)).thenReturn(created);

        Account result = accountService.createAccountWithInstitutionAdminRole(input);

        assertThat(result).isEqualTo(created);
        verify(relationshipService).writeRelationship(
                "institution", "tenant-123", "admin", "user", "user-456");
    }

    @DisplayName("Should delegate assignUserRole to use case")
    @Test
    void assignUserRole_delegatesToUseCase() {
        accountService.assignUserRole("user@test.com", "outlet", "outlet-123", "educator");

        verify(assignUserRoleUseCase).assignRole("user@test.com", "outlet", "outlet-123", "educator");
    }

    @DisplayName("Should delegate createParentAccount to use case")
    @Test
    void createParentAccount_delegatesToUseCase() {
        Account input = Account.builder().tenantId("tenant-123").build();
        Account created = Account.builder().userId("user-789").tenantId("tenant-123").build();
        when(createParentAccountUseCase.createParentAccount(input)).thenReturn(created);

        Account result = accountService.createParentAccount(input);

        assertThat(result).isEqualTo(created);
        verify(createParentAccountUseCase).createParentAccount(input);
    }
}
