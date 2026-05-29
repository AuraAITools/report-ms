package com.aura.app.controllers.graphql.v1.accounts;

import com.aura.app.codegen.types.AccountResponse;
import com.aura.app.codegen.types.CreateAccountInput;
import com.aura.app.codegen.types.CreateEducatorsInput;
import com.aura.app.codegen.types.CreateParentAndStudentsInAccountInput;
import com.aura.app.codegen.types.CreateParentAndStudentsInAccountResponse;
import com.aura.app.codegen.types.CreateParentInAccountInput;
import com.aura.app.codegen.types.CreateStudentsInput;
import com.aura.app.codegen.types.EducatorResponse;
import com.aura.app.codegen.types.EnableAccountFeatureInput;
import com.aura.app.codegen.types.ParentResponse;
import com.aura.app.codegen.types.StudentResponse;
import com.aura.app.exceptions.ResourceNotFoundBusinessException;
import com.aura.app.mappers.AccountMapper;
import com.aura.app.mappers.EducatorMapper;
import com.aura.app.mappers.ParentMapper;
import com.aura.app.mappers.StudentMapper;
import com.aura.core.auth.User;
import com.aura.core.entities.accounts.Account;
import com.aura.core.entities.accounts.Parent;
import com.aura.core.entities.educators.Educator;
import com.aura.core.entities.students.Student;
import com.aura.details.services.AccountService;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.BatchMapping;
import org.springframework.graphql.data.method.annotation.ContextValue;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;


import static com.aura.app.codegen.DgsConstants.MUTATION.CREATEACCOUNT_INPUT_ARGUMENT.CreateAccountInput;
import static com.aura.app.codegen.DgsConstants.MUTATION.CREATEEDUCATORSINACCOUNT_INPUT_ARGUMENT.CreateEducatorsInput;
import static com.aura.app.codegen.DgsConstants.MUTATION.CREATEPARENTANDSTUDENTSINACCOUNT_INPUT_ARGUMENT.CreateParentAndStudentsInAccountInput;
import static com.aura.app.codegen.DgsConstants.MUTATION.CREATEPARENTINACCOUNT_INPUT_ARGUMENT.CreateParentInAccountInput;
import static com.aura.app.codegen.DgsConstants.MUTATION.CREATESTUDENTSINACCOUNT_INPUT_ARGUMENT.CreateStudentsInput;
import static com.aura.app.codegen.DgsConstants.MUTATION.CreateAccount;
import static com.aura.app.codegen.DgsConstants.MUTATION.CreateEducatorsInAccount;
import static com.aura.app.codegen.DgsConstants.MUTATION.CreateParentAndStudentsInAccount;
import static com.aura.app.codegen.DgsConstants.MUTATION.CreateParentInAccount;
import static com.aura.app.codegen.DgsConstants.MUTATION.CreateStudentsInAccount;
import static com.aura.app.codegen.DgsConstants.MUTATION.ENABLEACCOUNTFEATURE_INPUT_ARGUMENT.EnableAccountFeatureInput;
import static com.aura.app.codegen.DgsConstants.MUTATION.EnableAccountFeature;

@Slf4j
@Controller
@RequiredArgsConstructor
public class AccountsGqlController {

    private final AccountService accountService;

    @PreAuthorize("hasPermission(#createAccountInput.institutionId, 'institution', 'create_account')")
    @MutationMapping(name = CreateAccount)
    public AccountResponse createAccount(@Argument(value = CreateAccountInput) CreateAccountInput createAccountInput, @ContextValue(name = "user") User user) {
        Account account = AccountMapper.INSTANCE.toDomainEntity(createAccountInput);
        Account createdAccount = accountService.createAccount(account);
        return AccountMapper.INSTANCE.toDto(createdAccount);
    }

    @PreAuthorize("hasPermission(#createParentInAccountInput.institutionId, 'institution', 'create_account')")
    @MutationMapping(name = CreateParentInAccount)
    public ParentResponse createParentInAccount(@Argument(value = CreateParentInAccountInput) CreateParentInAccountInput createParentInAccountInput, @ContextValue(name = "user") User user) {
        var parent = ParentMapper.INSTANCE.toDomainEntity(createParentInAccountInput);
        var createdParent = accountService.createParentInAccount(createParentInAccountInput.getAccountId(), parent);
        return ParentMapper.INSTANCE.toDto(createdParent);
    }

    @PreAuthorize("hasPermission(#createStudentsInput.institutionId, 'institution', 'create_account')")
    @MutationMapping(name = CreateStudentsInAccount)
    public List<StudentResponse> createStudentsInAccount(@Argument(value = CreateStudentsInput) CreateStudentsInput createStudentsInput, @ContextValue(name = "user") User user) {
        List<AccountService.CreateStudentParams> params = createStudentsInput.getStudents().stream()
                .map(StudentMapper.INSTANCE::toParams).toList();
        var createdStudentResults = accountService.createStudentsInAccount(params);
        return createdStudentResults.stream().map(StudentMapper.INSTANCE::toDto).toList();
    }

    @PreAuthorize("hasPermission(#createEducatorsInput.institutionId, 'institution', 'create_account')")
    @MutationMapping(name = CreateEducatorsInAccount)
    public List<EducatorResponse> createEducatorsInAccount(@Argument(value = CreateEducatorsInput) CreateEducatorsInput createEducatorsInput, @ContextValue(name = "user") User user) {
        var params = createEducatorsInput.getEducators().stream().map(EducatorMapper.INSTANCE::toParams).toList();
        List<AccountService.CreatedEducatorResult> results = accountService.createEducatorsInAccount(params);
        return results.stream().map(EducatorMapper.INSTANCE::toDto).toList();
    }

    @PreAuthorize("hasPermission(#enableAccountFeatureInput.accountId, 'account', 'update')")
    @MutationMapping(name = EnableAccountFeature)
    public AccountResponse enableAccountFeature(@Argument(value = EnableAccountFeatureInput) EnableAccountFeatureInput enableAccountFeatureInput, @ContextValue(name = "user") User user) {
        Account updated = accountService.enableAccountFeature(
                enableAccountFeatureInput.getAccountId(),
                AccountMapper.INSTANCE.toDomain(enableAccountFeatureInput.getFeature()));
        return AccountMapper.INSTANCE.toDto(updated);
    }

    @PreAuthorize("hasPermission(#createParentAndStudentsInAccountInput.institutionId, 'institution', 'create_account')")
    @MutationMapping(name = CreateParentAndStudentsInAccount)
    public CreateParentAndStudentsInAccountResponse createParentAndStudentsInAccount(
            @Argument(value = CreateParentAndStudentsInAccountInput) CreateParentAndStudentsInAccountInput createParentAndStudentsInAccountInput,
            @ContextValue(name = "user") User user) {
        Parent parent = ParentMapper.INSTANCE.toDomainEntity(createParentAndStudentsInAccountInput);
        List<AccountService.CreateStudentParams> studentParams = StudentMapper.INSTANCE.toParams(createParentAndStudentsInAccountInput);
        AccountService.ParentAndStudentsResult result = accountService.createParentAndStudentsInAccount(
                createParentAndStudentsInAccountInput.getAccountId(), parent, studentParams);
        return CreateParentAndStudentsInAccountResponse.newBuilder()
                ._parent(ParentMapper.INSTANCE.toDto(result.parent()))
                .students(result.students().stream().map(StudentMapper.INSTANCE::toDto).toList())
                .build();
    }

    @PreAuthorize("hasPermission(#accountId, 'account', 'view')")
    @QueryMapping
    public AccountResponse getAccountById(@Argument("accountId") String accountId,
                                          @ContextValue(name = "user") User user) {
        return Optional.ofNullable(accountService.findById(accountId))
                .map(AccountMapper.INSTANCE::toDto)
                .orElseThrow(() -> new ResourceNotFoundBusinessException("Account"));
    }

    @PreAuthorize("hasPermission(#institutionId, 'institution', 'view')")
    @QueryMapping
    public List<AccountResponse> getAllAccountsInInstitution(@Argument("institutionId") String institutionId,
                                                             @ContextValue(name = "user") User user) {
        return accountService.findAllByTenantId(institutionId).stream()
                .map(AccountMapper.INSTANCE::toDto)
                .toList();
    }

    @BatchMapping(typeName = "AccountResponse", field = "students")
    public Map<AccountResponse, List<StudentResponse>> students(List<AccountResponse> accountResponses) {
        List<String> accountIds = accountResponses.stream().map(AccountResponse::getId).toList();
        Map<String, List<Student>> studentsByAccountId = accountService.findStudentsByAccountIds(accountIds);
        return accountResponses.stream().collect(Collectors.toMap(
                Function.identity(),
                a -> studentsByAccountId.getOrDefault(a.getId(), List.of()).stream()
                        .map(StudentMapper.INSTANCE::toDto).toList()
        ));
    }

    @BatchMapping(typeName = "AccountResponse", field = "educators")
    public Map<AccountResponse, List<EducatorResponse>> educators(List<AccountResponse> accountResponses) {
        List<String> accountIds = accountResponses.stream().map(AccountResponse::getId).toList();
        Map<String, List<Educator>> educatorsByAccountId = accountService.findEducatorsByAccountIds(accountIds);
        return accountResponses.stream().collect(Collectors.toMap(
                Function.identity(),
                a -> educatorsByAccountId.getOrDefault(a.getId(), List.of()).stream()
                        .map(EducatorMapper.INSTANCE::toDto).toList()
        ));
    }

    @BatchMapping(typeName = "AccountResponse", field = "parent")
    public Map<AccountResponse, ParentResponse> parent(List<AccountResponse> accountResponses) {
        List<String> accountIds = accountResponses.stream().map(AccountResponse::getId).toList();
        Map<String, Parent> parentByAccountId = accountService.findParentsByAccountIds(accountIds);
        return accountResponses.stream()
                .filter(a -> parentByAccountId.containsKey(a.getId()))
                .collect(Collectors.toMap(
                        Function.identity(),
                        a -> ParentMapper.INSTANCE.toDto(parentByAccountId.get(a.getId()))
                ));
    }
}
