package com.aura.app.controllers.graphql.v1.accounts;

import com.aura.app.codegen.types.AccountResponse;
import com.aura.app.codegen.types.CreateEducatorInput;
import com.aura.app.codegen.types.CreateInstitutionAdminAccountInput;
import com.aura.app.codegen.types.CreateParentAccountInput;
import com.aura.app.codegen.types.CreateStudentInput;
import com.aura.app.codegen.types.EducatorResponse;
import com.aura.app.codegen.types.StudentResponse;
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
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;


import static com.aura.app.codegen.DgsConstants.MUTATION.ADDUSERTOGROUP_INPUT_ARGUMENT.AddUserToGroupInput;
import static com.aura.app.codegen.DgsConstants.MUTATION.AddUserToGroup;
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

    @PreAuthorize("hasPermission(#createAccountInput.institutionId, 'institution', 'create_account')")
    @MutationMapping(name = CreateInstitutionAdminAccount)
    public AccountResponse createAccountWithInstitutionAdminRole(@Argument(value = CreateAccountInput) CreateInstitutionAdminAccountInput createAccountInput, @ContextValue(name = "user") User user) {
        Account account = AccountMapper.INSTANCE.toDomainEntity(createAccountInput);
        Account createdAccount = accountService.createAccountWithInstitutionAdminRole(account);
        return AccountMapper.INSTANCE.toDto(createdAccount);
    }

    @PreAuthorize("hasPermission(#createParentAccountInput.institutionId, 'institution', 'create_account')")
    @MutationMapping(name = CreateParentAccount)
    public AccountResponse createParentAccount(@Argument(value = CreateParentAccountInput) CreateParentAccountInput createParentAccountInput, @ContextValue(name = "user") User user) {
        Account account = AccountMapper.INSTANCE.toDomainEntity(createParentAccountInput);
        Account createdAccount = accountService.createParentAccount(account);
        return AccountMapper.INSTANCE.toDto(createdAccount);
    }

    @PreAuthorize("hasPermission(null, 'institution', 'create_account')")
    @MutationMapping(name = CreateStudentsInParentAccount)
    public List<StudentResponse> createStudentsInParentAccount(@Argument(value = CreateStudentInputs) List<CreateStudentInput> createStudentInputs, @ContextValue(name = "user") User user) {
        List<AccountService.CreateStudentAccountParams> createStudentAccountParams = createStudentInputs.stream()
                .map(csi -> StudentMapper.INSTANCE.toParams(csi, user.getCurrentTenantAccessed())).toList();
        var createdStudentResults = accountService.addStudentsToParentAccount(createStudentAccountParams);
        return createdStudentResults.stream().map(StudentMapper.INSTANCE::toDto).toList();
    }

    @PreAuthorize("hasPermission(null, 'institution', 'create_account')")
    @MutationMapping(name = CreateEducatorsInAccount)
    public List<EducatorResponse> createEducatorsInAccount(@Argument(value = CreateEducatorInputs) List<CreateEducatorInput> createEducatorInputs, @ContextValue(name = "user") User user) {
        var params = createEducatorInputs.stream().map(cei -> EducatorMapper.INSTANCE.toParams(cei, user.getCurrentTenantAccessed())).toList();
        List<AccountService.CreatedEducatorResult> results = accountService.createAndLinkEducatorsToAccount(params);
        return results.stream().map(EducatorMapper.INSTANCE::toDto).toList();
    }

    @PreAuthorize("hasPermission(#addUserToGroupInput.institutionId, 'institution', 'delegate')")
    @MutationMapping(name = AddUserToGroup)
    public Boolean addUserToGroup(@Argument(value = AddUserToGroupInput) com.aura.app.codegen.types.AddUserToGroupInput addUserToGroupInput, @ContextValue(name = "user") User user) {
        accountService.assignUserRole(
                addUserToGroupInput.getUserEmail(),
                "institution",
                addUserToGroupInput.getInstitutionId(),
                addUserToGroupInput.getGroupName()
        );
        return true;
    }
}
