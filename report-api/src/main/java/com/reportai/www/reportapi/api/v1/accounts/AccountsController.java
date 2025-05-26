package com.reportai.www.reportapi.api.v1.accounts;

import com.reportai.www.reportapi.annotations.authorisation.HasResourcePermission;
import com.reportai.www.reportapi.api.v1.accounts.requests.CreateAccountParamsDTO;
import com.reportai.www.reportapi.api.v1.accounts.requests.CreateAccountWithEducatorsRequestDTO;
import com.reportai.www.reportapi.api.v1.accounts.requests.CreateAccountWithInstitutionAdminRoleRequestDTO;
import com.reportai.www.reportapi.api.v1.accounts.requests.CreateAccountWithOutletAdminRoleRequestDTO;
import com.reportai.www.reportapi.api.v1.accounts.requests.CreateAccountWithStudentsRequestDTO;
import com.reportai.www.reportapi.api.v1.accounts.requests.CreateOutletAdminRoleRequestDTO;
import com.reportai.www.reportapi.api.v1.accounts.requests.CreateStudentsInAccountRequestDTO;
import com.reportai.www.reportapi.api.v1.accounts.responses.AccountResponseDTO;
import com.reportai.www.reportapi.api.v1.accounts.responses.ExpandedAccountResponse;
import com.reportai.www.reportapi.api.v1.educators.requests.CreateEducatorRequestDTO;
import com.reportai.www.reportapi.entities.Account;
import com.reportai.www.reportapi.entities.Student;
import com.reportai.www.reportapi.entities.attachments.AccountEducatorAttachment;
import com.reportai.www.reportapi.entities.educators.Educator;
import com.reportai.www.reportapi.exceptions.lib.ResourceAlreadyExistsException;
import com.reportai.www.reportapi.services.accounts.TenantAwareAccountsService;
import com.reportai.www.reportapi.services.educators.EducatorsService;
import com.reportai.www.reportapi.services.students.StudentsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import java.util.Collection;
import java.util.List;
import java.util.UUID;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Accounts APIs", description = "APIs for creating accounts in an institutions")
@RestController
@RequestMapping("/api/v1")
@Validated
@Slf4j
public class AccountsController {
    private final TenantAwareAccountsService tenantAwareAccountsService;
    private final EducatorsService educatorsService;
    private final StudentsService studentsService;
    private final ModelMapper modelMapper;

    @Autowired
    public AccountsController(TenantAwareAccountsService tenantAwareAccountsService, EducatorsService educatorsService, StudentsService studentsService, ModelMapper modelMapper) {
        this.tenantAwareAccountsService = tenantAwareAccountsService;
        this.educatorsService = educatorsService;
        this.studentsService = studentsService;
        this.modelMapper = modelMapper;
    }

    @Operation(summary = "create an account for a institution", description = "create an account with no roles for a institution. A user can add roles (student-client,educator, outlet-admin and institution admin) to this account later")
    @ApiResponse(responseCode = "200", description = "OK")
    @PostMapping("/institutions/{id}/accounts")
    @HasResourcePermission(permission = "'institutions::' + #id + '::accounts:create'")
    @Transactional
    public ResponseEntity<ExpandedAccountResponse> createAccount(@PathVariable UUID id, @RequestBody @Valid CreateAccountParamsDTO createAccountParamsDTO) {
        Account account = modelMapper.map(createAccountParamsDTO, Account.class);
        tenantAwareAccountsService.createTenantAwareAccount(account, id);
        modelMapper.map(account, ExpandedAccountResponse.class);
        return new ResponseEntity<>(modelMapper.map(account, ExpandedAccountResponse.class), HttpStatus.OK);
    }

    // Role Granting Operations
    // =============================================================================
    // These endpoints handle granting different roles to existing accounts.
    // All operations are idempotent - granting an existing role will not cause errors.
    //
    // Available role types:
    // - Institution Admin: Full access to institution resources
    // - Outlet Admin: Access to specific outlet resources
    // - Student Client: Access to student dashboard features
    // - Educator Client: Access to educator dashboard features
    //
    // Note: Role assignments are managed through Keycloak and are tenant-aware
    // =============================================================================
    @Operation(summary = "grant institution admin role", description = "grant institution admin role")
    @ApiResponse(responseCode = "200", description = "OK")
    @PatchMapping("/institutions/{id}/accounts/{account_id}/institution-admin-roles")
    // TODO: add perms
    public ResponseEntity<Void> addInstitutionAdminRoleToAccount(@PathVariable UUID id, @PathVariable(name = "account_id") UUID accountId) {
        tenantAwareAccountsService.grantInstitutionAdminRole(accountId);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "grant outlet admin role for requested outlets to account", description = "grant outlet admin role for requested outlets to account")
    @ApiResponse(responseCode = "200", description = "OK")
    @PatchMapping("/institutions/{id}/accounts/{account_id}/outlet-admin-roles")
    // TODO: add perms
    public ResponseEntity<Void> addOutletAdminRoleToAccount(@PathVariable UUID id, @PathVariable(name = "account_id") UUID accountId, @RequestBody @Valid CreateOutletAdminRoleRequestDTO createOutletAdminRoleRequestDTO) {
        tenantAwareAccountsService.grantOutletAdminRoles(accountId, createOutletAdminRoleRequestDTO.getOutletIds());
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "add student client role to account", description = "add student client role to account")
    @ApiResponse(responseCode = "200", description = "OK")
    @PatchMapping("institutions/{id}/accounts/{account_id}/student-client-roles")
    @Transactional
    // TODO: add permission
    public ResponseEntity<Void> addStudentClientRoleToAccount(@PathVariable UUID id, @PathVariable(name = "account_id") UUID accountId) {
        tenantAwareAccountsService.grantStudentRole(accountId);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "add educator client role to account", description = "add educator client role to account")
    @ApiResponse(responseCode = "200", description = "OK")
    @PatchMapping("institutions/{id}/accounts/{account_id}/educator-client-roles") // TODO: add permissions
    @Transactional
    // TODO: add perms
    public ResponseEntity<Void> addEducatorClientRoleToAccount(@PathVariable UUID id, @PathVariable(name = "account_id") UUID accountId) {
        tenantAwareAccountsService.grantEducatorRole(accountId);
        return ResponseEntity.ok().build();
    }


    // =============================================================================
    // Role Granting & Entity Association Operations
    // =============================================================================
    // These endpoints handle granting roles and adding associated entities
    // (students/educators) to existing accounts.
    //
    // Operations:
    // - Grant role to existing account
    // - Link existing student(s) to student-client account
    // - Link existing educator to educator-client account
    //
    // Common flows:
    // 1. Student-client: Grant role -> Link student(s)
    // 2. Educator-client: Grant role -> Link educator
    //
    // Note: Multiple students can be linked to one account (e.g. parent account)
    //       However only 1 educator can be linked to one account
    // =============================================================================

    @Operation(summary = "create educator in an account", description = "creates educator in account and grants educator client role")
    @ApiResponse(responseCode = "200", description = "OK")
    @PostMapping("institutions/{id}/accounts/{account_id}/educators")
    @Transactional
    // TODO: add perms
    public ResponseEntity<ExpandedAccountResponse> createEducatorInAccountAndGrantRole(@PathVariable UUID id, @PathVariable(name = "account_id") UUID accountId, @RequestBody @Valid CreateEducatorRequestDTO createEducatorRequestDTO) {
        List<AccountEducatorAttachment> accountEducatorAttachments = tenantAwareAccountsService.getAccountEducatorAttachments(accountId);
        if (!accountEducatorAttachments.isEmpty()) {
            throw new ResourceAlreadyExistsException("an educator is already attached to this account");
        }
        Educator educator = educatorsService.create(createEducatorRequestDTO);
        tenantAwareAccountsService.attachEducatorToAccount(educator.getId(), accountId);
        tenantAwareAccountsService.grantEducatorRole(accountId);
        return new ResponseEntity<>(modelMapper.map(tenantAwareAccountsService.findById(accountId), ExpandedAccountResponse.class), HttpStatus.OK);
    }

    @Operation(summary = "create student in an account", description = "creates student in account and grants student client role, also sets relationship of student-client to student optionally")
    @ApiResponse(responseCode = "200", description = "OK")
    @PostMapping("institutions/{id}/accounts/{account_id}/students") // TODO: add permissions
    @HasResourcePermission(permission = "'institutions::' + #id + '::accounts:create-student'")
    @Transactional
    // TODO: add perms
    public ResponseEntity<ExpandedAccountResponse> createStudentsInAccountAndGrantRole(@PathVariable UUID id, @PathVariable(name = "account_id") UUID accountId, @RequestBody @Valid CreateStudentsInAccountRequestDTO createStudentsInAccountRequestDTO) {
        if (createStudentsInAccountRequestDTO.getRelationship() != null) {
            tenantAwareAccountsService.setRelationship(accountId, createStudentsInAccountRequestDTO.getRelationship());
        }
        List<Student> students = createStudentsInAccountRequestDTO
                .getStudents()
                .stream()
                .map(studentsService::create)
                .toList();
        students.forEach(student -> tenantAwareAccountsService.attachStudentToAccount(student.getId(), accountId));
        tenantAwareAccountsService.grantStudentRole(accountId);
        return new ResponseEntity<>(modelMapper.map(tenantAwareAccountsService.findById(accountId), ExpandedAccountResponse.class), HttpStatus.OK);
    }

    // =============================================================================
    // Combined Account Creation Operations
    // =============================================================================
    // These endpoints provide convenient shortcuts for common account setup patterns.
    // Each endpoint combines multiple operations:
    // 1. Create a new account
    // 2. Grant appropriate role(s)
    // 3. Create and link associated entities if any (student/educator)
    //
    // Available combinations:
    // - Account + Institution Admin Role
    // - Account + Outlet Admin Role + Outlet assignments
    // - Account + Student Client Role + Student entity
    // - Account + Educator Client Role + Educator entity
    //
    // Note: These operations are atomic - they will either complete fully or roll back
    // =============================================================================


    //TODO: allow institution admin authority to create admin accounts for their institution too
    @Operation(summary = "Creates a account with institution-admin role for an institution", description = "Creates an account and also creates an institution-admin role for the account")
    @ApiResponses({@ApiResponse(responseCode = "201", description = "created"), @ApiResponse(responseCode = "409", description = "existing account for institution account already exists"), @ApiResponse(responseCode = "500", description = "unexpected internal server error has occurred")})
    @PostMapping("/institutions/{id}/accounts/institution-admins")
//    @HasRole("'aura-admin'")
    @Transactional
    public ResponseEntity<ExpandedAccountResponse> createAccountWithInstitutionAdminRole(@PathVariable UUID id, @RequestBody @Valid CreateAccountWithInstitutionAdminRoleRequestDTO createAccountWithInstitutionAdminRoleRequestDTO) {
        Account account = modelMapper.map(createAccountWithInstitutionAdminRoleRequestDTO, Account.class);
        tenantAwareAccountsService.createTenantAwareAccount(account, id);
        tenantAwareAccountsService.grantInstitutionAdminRole(account.getId());
        return new ResponseEntity<>(modelMapper.map(account, ExpandedAccountResponse.class), HttpStatus.OK);
    }

    @Operation(summary = "Creates an account with outlet-admin roles for specified outlets for an institution", description = "Creates an account with outlet-admin role for specified outlets for an institution")
    @ApiResponse(responseCode = "200", description = "OK")
    @PostMapping("/institutions/{id}/accounts/outlet-admins")
    @HasResourcePermission(permission = "'institutions::' + #id + '::accounts:create-link-outlet-admin'")
    @Transactional
    // TODO: rename permission
    public ResponseEntity<ExpandedAccountResponse> createAccountWithOutletAdminRole(@PathVariable UUID id, @RequestBody @Valid CreateAccountWithOutletAdminRoleRequestDTO createAccountWithOutletAdminRoleRequestDTO) {
        Account account = modelMapper.map(createAccountWithOutletAdminRoleRequestDTO, Account.class);
        tenantAwareAccountsService.createTenantAwareAccount(account, id);
        tenantAwareAccountsService.grantOutletAdminRoles(account.getId(), createAccountWithOutletAdminRoleRequestDTO.getOutletIds());
        return new ResponseEntity<>(modelMapper.map(account, ExpandedAccountResponse.class), HttpStatus.OK);
    }

    @Operation(summary = "Creates an account with student-client role for an institution", description = "Creates an account and also creates the student client role which allows access to report-mobile student dashboard feature")
    @ApiResponse(responseCode = "200", description = "OK")
    @PostMapping("/institutions/{id}/accounts/student-clients")
    @HasResourcePermission(permission = "'institutions::' + #id + '::accounts::student-client-account:create'")
    @Transactional
    // TODO: this will fail if outlets have not been created yet, test
    public ResponseEntity<ExpandedAccountResponse> createAccountWithStudentClientRole(@PathVariable UUID id, @RequestBody @Valid CreateAccountWithStudentsRequestDTO createAccountWithStudentsRequestDTO) {
        Account account = modelMapper.map(createAccountWithStudentsRequestDTO, Account.class);
        List<Student> students = createAccountWithStudentsRequestDTO
                .getStudents()
                .stream()
                .map(studentsService::create)
                .toList();
        tenantAwareAccountsService.createTenantAwareAccount(account, id);
        students
                .forEach(student -> tenantAwareAccountsService.attachStudentToAccount(student.getId(), account.getId()));
        tenantAwareAccountsService.setRelationship(account.getId(), createAccountWithStudentsRequestDTO.getRelationship());
        tenantAwareAccountsService.grantStudentRole(account.getId());
        return new ResponseEntity<>(modelMapper.map(account, ExpandedAccountResponse.class), HttpStatus.OK);
    }

    @Operation(summary = "Creates account and grant educator client role and creates educator", description = "Creates account and grant educator client role and creates educator")
    @ApiResponse(responseCode = "200", description = "OK")
    @PostMapping("/institutions/{id}/accounts/educator-clients")
    @HasResourcePermission(permission = "'institutions::' + #id + '::accounts::educator-client-account:create'")
    public ResponseEntity<ExpandedAccountResponse> createAccountWithEducatorClientRole(@PathVariable UUID id, @RequestBody @Valid CreateAccountWithEducatorsRequestDTO createAccountWithEducatorsRequestDTO) {
        Account account = modelMapper.map(createAccountWithEducatorsRequestDTO, Account.class);
        Educator educator = educatorsService.create(createAccountWithEducatorsRequestDTO.getEducator());
        tenantAwareAccountsService.createTenantAwareAccount(account, id);
        tenantAwareAccountsService.attachEducatorToAccount(educator.getId(), account.getId());
        tenantAwareAccountsService.grantEducatorRole(account.getId());
        return new ResponseEntity<>(modelMapper.map(account, ExpandedAccountResponse.class), HttpStatus.OK);
    }

    @Operation(summary = "get all expanded account for a institution", description = "get all expanded accounts for a institution")
    @ApiResponse(responseCode = "200", description = "OK")
    @GetMapping("/institutions/{id}/accounts/expand")
    @HasResourcePermission(permission = "'institutions::' + #id + '::accounts:read'")
    public ResponseEntity<List<ExpandedAccountResponse>> getAllExpandedAccountsForInstitution(@PathVariable UUID id, @RequestParam(name = "email", required = false) @Email String email) {
        Collection<Account> accounts = email == null ? tenantAwareAccountsService.getAllTenantAwareAccounts() : List.of(tenantAwareAccountsService.getAllTenantAwareAccountsByEmail(email));
        List<ExpandedAccountResponse> response = accounts
                .stream()
                .map(account -> modelMapper.map(account, ExpandedAccountResponse.class))
                .toList();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @Operation(summary = "get all account for a institution", description = "get all accounts for a institution")
    @ApiResponse(responseCode = "201", description = "OK")
    @GetMapping("/institutions/{id}/accounts")
    @HasResourcePermission(permission = "'institutions::' + #id + '::accounts:read'")
    public ResponseEntity<List<AccountResponseDTO>> getAllAccountsForInstitution(@PathVariable UUID id, @RequestParam(name = "email", required = false) @Email String email) {

        Collection<Account> accounts = email == null ? tenantAwareAccountsService.getAllTenantAwareAccounts() : List.of(tenantAwareAccountsService.getAllTenantAwareAccountsByEmail(email));
        List<AccountResponseDTO> response = accounts
                .stream()
                .map(account -> modelMapper.map(account, AccountResponseDTO.class))
                .toList();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

}
