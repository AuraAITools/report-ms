package com.aura.details.services;

import com.aura.core.clients.IdentityService;
import com.aura.core.clients.RelationshipAuthorizationService;
import com.aura.core.entities.accounts.Account;
import com.aura.core.entities.accounts.Parent;
import com.aura.core.entities.educators.Educator;
import com.aura.core.events.EducatorAccountCreatedEvent;
import com.aura.core.repositories.AccountRepository;
import com.aura.core.repositories.ParentRepository;
import com.aura.core.usecases.accounts.AccountQueryUseCase;
import com.aura.core.usecases.accounts.CreateAccountUseCase;
import com.aura.core.usecases.accounts.CreateEducatorInAccountUseCase;
import com.aura.core.usecases.accounts.CreateStudentsInParentAccountUseCase;
import com.aura.core.usecases.courses.EnrollStudentToCourseUseCase;
import com.aura.core.usecases.courses.TagEducatorToCourseUseCase;
import com.aura.core.usecases.educators.EnrollEducatorToOutletUseCase;
import com.aura.core.usecases.educators.TagEducatorToLevelUseCase;
import com.aura.core.usecases.educators.TagEducatorToSubjectUseCase;
import com.aura.core.usecases.students.TagStudentToLevelUseCase;
import com.aura.core.usecases.students.TagStudentToSchoolUseCase;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationEventPublisher;


import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@DisplayName("Account service")
@ExtendWith(MockitoExtension.class)
class AccountServiceTest {

    @Mock
    private CreateAccountUseCase createAccountUseCase;
    @Mock
    private CreateStudentsInParentAccountUseCase createStudentsInParentAccountUseCase;
    @Mock
    private TagStudentToLevelUseCase tagStudentToLevelUseCase;
    @Mock
    private TagStudentToSchoolUseCase tagStudentToSchoolUseCase;
    @Mock
    private EnrollStudentToCourseUseCase enrollStudentToCourseUseCase;
    @Mock
    private CreateEducatorInAccountUseCase createEducatorInAccountUseCase;
    @Mock
    private TagEducatorToCourseUseCase tagEducatorToCourseUseCase;
    @Mock
    private TagEducatorToLevelUseCase tagEducatorToLevelUseCase;
    @Mock
    private TagEducatorToSubjectUseCase tagEducatorToSubjectUseCase;
    @Mock
    private EnrollEducatorToOutletUseCase enrollEducatorToOutletUseCase;
    @Mock
    private AccountRepository accountRepository;
    @Mock
    private IdentityService identityService;
    @Mock
    private RelationshipAuthorizationService relationshipService;
    @Mock
    private ApplicationEventPublisher applicationEventPublisher;
    @Mock
    private ParentRepository parentRepository;
    @Mock
    private AccountQueryUseCase accountQueryUseCase;
    @Mock
    private AssetUrlService assetUrlService;
    @Mock
    private ProfileImageValidator profileImageValidator;

    private static final String ACTION_REDIRECT_URI = "https://app-test.aura.com/auth/complete";

    private AccountService accountService;

    @BeforeEach
    void initService() {
        accountService = new AccountService(
                createAccountUseCase,
                createStudentsInParentAccountUseCase,
                tagStudentToLevelUseCase,
                tagStudentToSchoolUseCase,
                enrollStudentToCourseUseCase,
                createEducatorInAccountUseCase,
                tagEducatorToCourseUseCase,
                tagEducatorToLevelUseCase,
                tagEducatorToSubjectUseCase,
                enrollEducatorToOutletUseCase,
                accountRepository,
                identityService,
                relationshipService,
                applicationEventPublisher,
                ACTION_REDIRECT_URI,
                parentRepository,
                accountQueryUseCase,
                assetUrlService,
                profileImageValidator);
    }

    @DisplayName("Should delegate findById to the account query use case")
    @Test
    void findById_delegatesToQueryUseCase() {
        Account account = Account.builder().id("acc-1").build();
        when(accountQueryUseCase.findById("acc-1")).thenReturn(account);

        assertThat(accountService.findById("acc-1")).isEqualTo(account);
    }

    @DisplayName("Should delegate findByUserIdAndTenantId to the account query use case")
    @Test
    void findByUserIdAndTenantId_delegatesToQueryUseCase() {
        Account account = Account.builder()
                .id("acc-1").userId("user-1").tenantId("tenant-1")
                .state(Account.STATE.ACTIVE).build();
        when(accountQueryUseCase.findByUserIdAndTenantId("user-1", "tenant-1")).thenReturn(account);

        assertThat(accountService.findByUserIdAndTenantId("user-1", "tenant-1")).isEqualTo(account);
    }

    @DisplayName("Should return null when no account is found for the given user and tenant")
    @Test
    void findByUserIdAndTenantId_returnsNullWhenMissing() {
        when(accountQueryUseCase.findByUserIdAndTenantId("user-x", "tenant-1")).thenReturn(null);

        assertThat(accountService.findByUserIdAndTenantId("user-x", "tenant-1")).isNull();
    }

    @DisplayName("Should promote a VERIFYING account to ACTIVE when required actions are completed")
    @Test
    void findByUserIdAndTenantId_promotesVerifyingAccountWhenActionsCompleted() {
        Account account = Account.builder()
                .id("acc-1").userId("user-1").tenantId("tenant-1")
                .state(Account.STATE.VERIFYING).build();
        when(accountQueryUseCase.findByUserIdAndTenantId("user-1", "tenant-1")).thenReturn(account);
        when(identityService.hasCompletedRequiredActions("user-1", List.of("VERIFY_EMAIL", "UPDATE_PASSWORD")))
                .thenReturn(true);

        Account result = accountService.findByUserIdAndTenantId("user-1", "tenant-1");

        assertThat(result.getState()).isEqualTo(Account.STATE.ACTIVE);
        verify(accountRepository).updateStateById("acc-1", Account.STATE.ACTIVE);
    }

    @DisplayName("Should delegate findAllByTenantId to the account query use case")
    @Test
    void findAllByTenantId_delegatesToQueryUseCase() {
        Account a1 = Account.builder().id("acc-1").tenantId("tenant-1").build();
        Account a2 = Account.builder().id("acc-2").tenantId("tenant-1").build();
        when(accountQueryUseCase.findAllByTenantId("tenant-1")).thenReturn(List.of(a1, a2));

        assertThat(accountService.findAllByTenantId("tenant-1")).containsExactly(a1, a2);
    }


    @Test
    @DisplayName("Should delegate findAllByUserIdsAndTenantId to the account repository")
    void findAllByUserIdsAndTenantId_delegatesToRepository() {
        List<String> userIds = List.of("user-a", "user-b");
        String tenantId = "tenant-1";
        Account a = Account.builder()
                .id("acc-1").userId("user-a").tenantId(tenantId).build();
        Map<String, Account> expected = Map.of("user-a", a);

        when(accountRepository.findAllByUserIdsAndTenantId(userIds, tenantId)).thenReturn(expected);

        Map<String, Account> result =
                accountService.findAllByUserIdsAndTenantId(userIds, tenantId);

        assertThat(result).isEqualTo(expected);
        verify(accountRepository).findAllByUserIdsAndTenantId(userIds, tenantId);
    }

    @DisplayName("Should publish EducatorAccountCreatedEvent on educator-account linking")
    @Test
    void createEducatorInAccount_publishesEvent() {
        Educator educator = Educator.builder().name("Jane").build();
        Educator createdEducator = Educator.builder().id("edu-1").name("Jane").build();
        Account account = Account.builder().userId("user-1").tenantId("tenant-1").build();
        var params = AccountService.CreateEducatorAccountParams.builder()
                .educator(educator).accountId("acc-1")
                .courseIds(List.of()).subjectIds(List.of())
                .outletIds(List.of()).levelIds(List.of()).build();
        when(createEducatorInAccountUseCase.createEducator("acc-1", educator)).thenReturn(createdEducator);
        when(accountRepository.findById("acc-1")).thenReturn(account);

        accountService.createEducatorInAccount(params);

        ArgumentCaptor<EducatorAccountCreatedEvent> captor = ArgumentCaptor.forClass(EducatorAccountCreatedEvent.class);
        verify(applicationEventPublisher).publishEvent(captor.capture());
        assertThat(captor.getValue().account()).isEqualTo(account);
    }

    @DisplayName("Should set accountId on Parent and persist via parentRepository when account exists")
    @Test
    void createParentInAccount_setsAccountIdAndSaves() {
        Account account = Account.builder().id("acc-123").tenantId("tenant-1").build();
        Parent parent = Parent.builder().relationship(Parent.RELATIONSHIP.PARENT).build();
        when(accountRepository.findById("acc-123")).thenReturn(account);
        when(parentRepository.save(parent)).thenReturn(parent);

        Parent result = accountService.createParentInAccount("acc-123", parent);

        assertThat(parent.getAccountId()).isEqualTo("acc-123");
        assertThat(result).isSameAs(parent);
        verify(parentRepository).save(parent);
    }

    @DisplayName("Should throw when the target account already has a parent attached")
    @Test
    void createParentInAccount_throwsWhenParentExists() {
        Parent existing = Parent.builder().build();
        Account account = Account.builder().id("acc-123").parent(existing).build();
        Parent parent = Parent.builder().build();
        when(accountRepository.findById("acc-123")).thenReturn(account);

        assertThatThrownBy(() -> accountService.createParentInAccount("acc-123", parent))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("parent already exists");
    }

    @DisplayName("Should throw when the target account does not exist")
    @Test
    void createParentInAccount_throwsWhenAccountMissing() {
        when(accountRepository.findById("missing")).thenReturn(null);
        Parent parent = Parent.builder().build();

        assertThatThrownBy(() -> accountService.createParentInAccount("missing", parent))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("account does not exist");
    }
}
