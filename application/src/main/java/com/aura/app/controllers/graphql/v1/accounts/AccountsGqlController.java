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


import static com.aura.app.codegen.DgsConstants.MUTATION.ADDOUTLETADMIN_INPUT_ARGUMENT.AddOutletAdminInput;
import static com.aura.app.codegen.DgsConstants.MUTATION.ADDINSTITUTIONADMIN_INPUT_ARGUMENT.AddInstitutionAdminInput;
import static com.aura.app.codegen.DgsConstants.MUTATION.ADDCOURSEADMIN_INPUT_ARGUMENT.AddCourseAdminInput;
import static com.aura.app.codegen.DgsConstants.MUTATION.ADDLESSONADMIN_INPUT_ARGUMENT.AddLessonAdminInput;
import static com.aura.app.codegen.DgsConstants.MUTATION.AddOutletAdmin;
import static com.aura.app.codegen.DgsConstants.MUTATION.AddInstitutionAdmin;
import static com.aura.app.codegen.DgsConstants.MUTATION.AddCourseAdmin;
import static com.aura.app.codegen.DgsConstants.MUTATION.AddLessonAdmin;
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

    @PreAuthorize("hasPermission(#addOutletAdminInput.outletId, 'outlet', 'delegate')")
    @MutationMapping(name = AddOutletAdmin)
    public Boolean addOutletAdmin(@Argument(value = AddOutletAdminInput) com.aura.app.codegen.types.AddOutletAdminInput addOutletAdminInput, @ContextValue(name = "user") User user) {
        accountService.assignUserRole(
                addOutletAdminInput.getUserEmail(),
                "outlet",
                addOutletAdminInput.getOutletId(),
                "admin"
        );
        return true;
    }

    @PreAuthorize("hasPermission(#addInstitutionAdminInput.institutionId, 'institution', 'delegate')")
    @MutationMapping(name = AddInstitutionAdmin)
    public Boolean addInstitutionAdmin(@Argument(value = AddInstitutionAdminInput) com.aura.app.codegen.types.AddInstitutionAdminInput addInstitutionAdminInput, @ContextValue(name = "user") User user) {
        accountService.assignUserRole(
                addInstitutionAdminInput.getUserEmail(),
                "institution",
                addInstitutionAdminInput.getInstitutionId(),
                "admin"
        );
        return true;
    }

    @PreAuthorize("hasPermission(#addCourseAdminInput.courseId, 'course', 'delegate')")
    @MutationMapping(name = AddCourseAdmin)
    public Boolean addCourseAdmin(@Argument(value = AddCourseAdminInput) com.aura.app.codegen.types.AddCourseAdminInput addCourseAdminInput, @ContextValue(name = "user") User user) {
        accountService.assignUserRole(
                addCourseAdminInput.getUserEmail(),
                "course",
                addCourseAdminInput.getCourseId(),
                "admin"
        );
        return true;
    }

    @PreAuthorize("hasPermission(#addLessonAdminInput.lessonId, 'lesson', 'delegate')")
    @MutationMapping(name = AddLessonAdmin)
    public Boolean addLessonAdmin(@Argument(value = AddLessonAdminInput) com.aura.app.codegen.types.AddLessonAdminInput addLessonAdminInput, @ContextValue(name = "user") User user) {
        accountService.assignUserRole(
                addLessonAdminInput.getUserEmail(),
                "lesson",
                addLessonAdminInput.getLessonId(),
                "admin"
        );
        return true;
    }
}
