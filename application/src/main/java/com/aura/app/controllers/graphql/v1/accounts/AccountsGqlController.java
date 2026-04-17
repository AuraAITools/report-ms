package com.aura.app.controllers.graphql.v1.accounts;

import com.aura.app.annotations.ProtectedOperation;
import com.aura.app.codegen.types.AccountResponse;
import com.aura.app.codegen.types.CreateEducatorInput;
import com.aura.app.codegen.types.CreateInstitutionAdminAccountInput;
import com.aura.app.codegen.types.CreateParentAccountInput;
import com.aura.app.codegen.types.CreateStudentInput;
import com.aura.app.codegen.types.EducatorResponse;
import com.aura.app.codegen.types.StudentResponse;
import com.aura.app.exceptions.AggregateOperationUnsupportedException;
import com.aura.app.mappers.AccountMapper;
import com.aura.app.mappers.EducatorMapper;
import com.aura.app.mappers.StudentMapper;
import com.aura.core.auth.User;
import com.aura.core.entities.accounts.Account;
import com.aura.details.services.AccountService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.ContextValue;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.stereotype.Controller;


import static com.aura.app.codegen.DgsConstants.MUTATION.CREATEEDUCATORSINACCOUNT_INPUT_ARGUMENT.CreateEducatorInputs;
import static com.aura.app.codegen.DgsConstants.MUTATION.CREATEINSTITUTIONADMINACCOUNT_INPUT_ARGUMENT.CreateAccountInput;
import static com.aura.app.codegen.DgsConstants.MUTATION.CREATEPARENTACCOUNT_INPUT_ARGUMENT.CreateParentAccountInput;
import static com.aura.app.codegen.DgsConstants.MUTATION.CREATESTUDENTSINPARENTACCOUNT_INPUT_ARGUMENT.CreateStudentInputs;
import static com.aura.app.codegen.DgsConstants.MUTATION.CreateEducatorsInAccount;
import static com.aura.app.codegen.DgsConstants.MUTATION.CreateInstitutionAdminAccount;
import static com.aura.app.codegen.DgsConstants.MUTATION.CreateParentAccount;
import static com.aura.app.codegen.DgsConstants.MUTATION.CreateStudentsInParentAccount;

@Slf4j
@Controller
@RequiredArgsConstructor
public class AccountsGqlController {

    private final AccountService accountService;

    @ProtectedOperation(resource = "account", scope = "create")
    @MutationMapping(name = CreateInstitutionAdminAccount)
    public AccountResponse createAccountWithInstitutionAdminRole(@Argument(value = CreateAccountInput) CreateInstitutionAdminAccountInput createAccountInput, @ContextValue(name = "user") User user) {
        rejectAggregateQuery(user);
        Account account = AccountMapper.INSTANCE.toDomainEntity(createAccountInput, user.getCurrentTenantAccessed());
        Account createdAccount = accountService.createAccountWithInstitutionAdminRole(account);
        return AccountMapper.INSTANCE.toDto(createdAccount);
    }

    @ProtectedOperation(resource = "account", scope = "create")
    @MutationMapping(name = CreateParentAccount)
    public AccountResponse createParentAccount(@Argument(value = CreateParentAccountInput) CreateParentAccountInput createParentAccountInput, @ContextValue(name = "user") User user) {
        rejectAggregateQuery(user);
        Account account = AccountMapper.INSTANCE.toDomainEntity(createParentAccountInput, user.getCurrentTenantAccessed());
        Account createdAccount = accountService.createParentAccount(account);
        return AccountMapper.INSTANCE.toDto(createdAccount);
    }

    @ProtectedOperation(resource = "account", scope = "create")
    @MutationMapping(name = CreateStudentsInParentAccount)
    public List<StudentResponse> createStudentsInParentAccount(@Argument(value = CreateStudentInputs) List<CreateStudentInput> createStudentInputs, @ContextValue(name = "user") User user) {
        rejectAggregateQuery(user);
        List<AccountService.CreateStudentAccountParams> createStudentAccountParams = createStudentInputs.stream()
                .map(csi -> StudentMapper.INSTANCE.toParams(csi, user.getCurrentTenantAccessed())).toList();
        var createdStudentResults = accountService.addStudentsToParentAccount(createStudentAccountParams);
        return createdStudentResults.stream().map(StudentMapper.INSTANCE::toDto).toList();
    }

    @ProtectedOperation(resource = "account", scope = "create")
    @MutationMapping(name = CreateEducatorsInAccount)
    public List<EducatorResponse> createEducatorsInAccount(@Argument(value = CreateEducatorInputs) List<CreateEducatorInput> createEducatorInputs, @ContextValue(name = "user") User user) {
        rejectAggregateQuery(user);
        var params = createEducatorInputs.stream().map(cei -> EducatorMapper.INSTANCE.toParams(cei, user.getCurrentTenantAccessed())).toList();
        List<AccountService.CreatedEducatorResult> results = accountService.createAndLinkEducatorsToAccount(params);
        return results.stream().map(EducatorMapper.INSTANCE::toDto).toList();
    }

    private static void rejectAggregateQuery(User user) {
        if ("*".equals(user.getCurrentTenantAccessed())) {
            throw new AggregateOperationUnsupportedException("aggregate operation unsupported for this API");
        }
    }
}
