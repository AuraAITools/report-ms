package com.reportai.www.reportapi.api.v1.accounts;

import com.reportai.www.reportapi.annotations.authorisation.HasResourcePermission;
import com.reportai.www.reportapi.annotations.authorisation.HasRole;
import com.reportai.www.reportapi.api.v1.accounts.requests.CreateEducatorDTO;
import com.reportai.www.reportapi.api.v1.accounts.requests.CreateInstitutionAdminAccountDTO;
import com.reportai.www.reportapi.api.v1.accounts.requests.CreateStudentDTO;
import com.reportai.www.reportapi.api.v1.accounts.requests.CreateUserDTO;
import com.reportai.www.reportapi.api.v1.accounts.responses.CreateAccountResponseDTO;
import com.reportai.www.reportapi.api.v1.accounts.responses.CreateEducatorResponseDTO;
import com.reportai.www.reportapi.api.v1.accounts.responses.CreateStudentResponseDTO;
import com.reportai.www.reportapi.entities.Account;
import com.reportai.www.reportapi.entities.Educator;
import com.reportai.www.reportapi.entities.Level;
import com.reportai.www.reportapi.entities.Student;
import com.reportai.www.reportapi.mappers.AccountMappers;
import com.reportai.www.reportapi.mappers.EducatorMappers;
import com.reportai.www.reportapi.mappers.StudentMappers;
import com.reportai.www.reportapi.services.accounts.TenantAwareAccountsService;
import com.reportai.www.reportapi.services.accounts.creationstrategies.TenantAwareAccountCreationContext;
import com.reportai.www.reportapi.services.educators.EducatorsService;
import com.reportai.www.reportapi.services.levels.LevelsService;
import com.reportai.www.reportapi.services.students.StudentsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import java.util.UUID;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Accounts APIs", description = "APIs for creating accounts in an institutions")
@RestController
@RequestMapping("/api/v1")
@Validated
@Slf4j
public class AccountsController {
    private final TenantAwareAccountsService tenantAwareAccountsService;
    private final StudentsService studentsService;
    private final LevelsService levelsService;
    private final EducatorsService educatorsService;

    @Autowired
    public AccountsController(TenantAwareAccountsService tenantAwareAccountsService, StudentsService studentsService, LevelsService levelsService, EducatorsService educatorsService) {
        this.tenantAwareAccountsService = tenantAwareAccountsService;
        this.studentsService = studentsService;
        this.levelsService = levelsService;
        this.educatorsService = educatorsService;
    }

    @Operation(summary = "create an client account for the clients of a institution", description = "create an account for the clients of a institution. A client of a institution is defined as the person who is registering with the institution, it could be either a student or a parent. The client will be able to log in to the Aura Report mobile app after email verification")
    @ApiResponse(responseCode = "201", description = "OK")
    @PostMapping("/institutions/{id}/accounts")
    @HasResourcePermission(permission = "'institutions::' + #id + '::accounts:create'")
    public ResponseEntity<CreateAccountResponseDTO> createClientAccountForInstitution(@PathVariable UUID id, @RequestBody @Valid CreateUserDTO createUserDTO) {
        Account account = AccountMappers.convert(createUserDTO, id.toString());
        Account createdAccount = tenantAwareAccountsService.createTenantAwareAccount(account, TenantAwareAccountCreationContext.AccountType.CLIENT, id);
        return new ResponseEntity<>(AccountMappers.convert(createdAccount), HttpStatus.CREATED);
    }

    //TODO: allow institution admin authority to create admin accounts for their institution too
    @Operation(summary = "Creates an admin account for an institution ", description = "Creates an admin account for the institution. This admin user can be used to add other accounts")
    @ApiResponses({@ApiResponse(responseCode = "201", description = "created"), @ApiResponse(responseCode = "409", description = "existing account for institution account already exists"), @ApiResponse(responseCode = "500", description = "unexpected internal server error has occurred")})
    @PostMapping("/institutions/{id}/admin-accounts")
    @HasRole("aura-admin")
    public ResponseEntity<CreateAccountResponseDTO> createAdminAccountForInstitution(@PathVariable UUID id, @RequestBody @Valid CreateInstitutionAdminAccountDTO createInstitutionAdminAccountDTO) {
        Account account = AccountMappers.convert(createInstitutionAdminAccountDTO);
        account.setTenantId(id.toString());
        Account createdAccount = tenantAwareAccountsService.createTenantAwareAccount(account, TenantAwareAccountCreationContext.AccountType.INSTITUTION_ADMIN, id);
        return new ResponseEntity<>(AccountMappers.convert(createdAccount), HttpStatus.OK);
    }

    //TODO: create and link student with account
    @Operation(summary = "create an student in a client account", description = "create student and link student to an already created client account")
    @ApiResponse(responseCode = "201", description = "OK")
    @PostMapping("/institutions/{id}/accounts/{account_id}/students")
    @HasResourcePermission(permission = "'institutions::' + #id + '::accounts:link-student'")
    @Transactional
    public ResponseEntity<CreateStudentResponseDTO> createAndLinkStudentToClientAccount(@PathVariable UUID id, @PathVariable("account_id") UUID accountId, @RequestBody @Valid CreateStudentDTO createStudentDTO) {
        Account account = tenantAwareAccountsService.findById(accountId);
        Student newStudent = StudentMappers.convert(createStudentDTO, id);
        Level level = levelsService.findById(createStudentDTO.getLevelId());
        newStudent.setLevel(level);
        Student createdStudent = studentsService.createStudentForInstitution(newStudent, id);
        tenantAwareAccountsService.linkAccountToStudent(account.getId(), createdStudent.getId());
        return new ResponseEntity<>(StudentMappers.convert(createdStudent), HttpStatus.OK);
    }

    //TODO: create educator with account
    @Operation(summary = "create an educator account", description = "create educator in a already created client account")
    @ApiResponse(responseCode = "201", description = "OK")
    @PostMapping("/institutions/{id}/accounts/{account_id}/educators")
    @HasResourcePermission(permission = "'institutions::' + #id + '::accounts:link-educator'")
    @Transactional
    public ResponseEntity<CreateEducatorResponseDTO> createAndLinkEducatorToClientAccount(@PathVariable UUID id, @PathVariable("account_id") UUID accountId, @RequestBody @Valid CreateEducatorDTO createEducatorDTO) {
        Account account = tenantAwareAccountsService.findById(accountId);
        Educator newEducator = EducatorMappers.convert(createEducatorDTO, id);
        Educator createdEducator = educatorsService.createEducatorForInstitution(newEducator, id);
        tenantAwareAccountsService.linkAccountToEducator(account.getId(), createdEducator.getId());
        return new ResponseEntity<>(EducatorMappers.convert(createdEducator), HttpStatus.OK);
    }

    // TODO: add account as outlet admin
    @Operation(summary = "add account as outlet admin", description = "add account as outlet admin")
    @ApiResponse(responseCode = "200", description = "OK")
    @PatchMapping("/institutions/{id}/accounts/{account_id}/outlets/{outlet_id}")
    @HasResourcePermission(permission = "'institutions::' + #id + '::accounts:link-outlet-admin'")
    public ResponseEntity<Void> addAccountAsOutletAdmin(@PathVariable UUID id, @PathVariable(name = "account_id") UUID accountId, @PathVariable(name = "outlet_id") UUID outletId) {
        Account account = tenantAwareAccountsService.assignOutletAdminRole(id, outletId, accountId);
        return ResponseEntity.ok().build();
    }
}
