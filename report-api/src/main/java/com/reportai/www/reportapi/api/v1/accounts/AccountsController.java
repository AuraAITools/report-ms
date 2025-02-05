package com.reportai.www.reportapi.api.v1.accounts;

import com.reportai.www.reportapi.annotations.authorisation.HasResourcePermission;
import com.reportai.www.reportapi.api.v1.accounts.requests.CreateBlankAccountRequestDTO;
import com.reportai.www.reportapi.api.v1.accounts.requests.CreateClientRequestDTO;
import com.reportai.www.reportapi.api.v1.accounts.requests.CreateEducatorDTO;
import com.reportai.www.reportapi.api.v1.accounts.requests.CreateInstitutionAdminAccountDTO;
import com.reportai.www.reportapi.api.v1.accounts.requests.CreateStudentDTO;
import com.reportai.www.reportapi.api.v1.accounts.responses.CreateAccountResponseDTO;
import com.reportai.www.reportapi.api.v1.accounts.responses.CreateEducatorResponseDTO;
import com.reportai.www.reportapi.api.v1.accounts.responses.CreateStudentResponseDTO;
import com.reportai.www.reportapi.entities.Account;
import com.reportai.www.reportapi.entities.Client;
import com.reportai.www.reportapi.entities.Educator;
import com.reportai.www.reportapi.entities.Level;
import com.reportai.www.reportapi.entities.Student;
import com.reportai.www.reportapi.mappers.AccountMappers;
import com.reportai.www.reportapi.mappers.ClientMappers;
import com.reportai.www.reportapi.mappers.EducatorMappers;
import com.reportai.www.reportapi.mappers.StudentMappers;
import com.reportai.www.reportapi.services.accounts.TenantAwareAccountsService;
import com.reportai.www.reportapi.services.accounts.creationstrategies.TenantAwareAccountCreationContext;
import com.reportai.www.reportapi.services.clients.ClientService;
import com.reportai.www.reportapi.services.educators.EducatorsService;
import com.reportai.www.reportapi.services.levels.LevelsService;
import com.reportai.www.reportapi.services.students.StudentsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import java.util.List;
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
    private final ClientService clientService;

    @Autowired
    public AccountsController(TenantAwareAccountsService tenantAwareAccountsService, StudentsService studentsService, LevelsService levelsService, EducatorsService educatorsService, ClientService clientService) {
        this.tenantAwareAccountsService = tenantAwareAccountsService;
        this.studentsService = studentsService;
        this.levelsService = levelsService;
        this.educatorsService = educatorsService;
        this.clientService = clientService;
    }


    @Operation(summary = "create an blank account for a institution", description = "create an blank account for a institution. This blank account can later be linked to a client, student, parent, educator, outlet and institution admin")
    @ApiResponse(responseCode = "201", description = "OK")
    @PostMapping("/institutions/{id}/accounts")
    @HasResourcePermission(permission = "'institutions::' + #id + '::accounts:create'")
    public ResponseEntity<CreateAccountResponseDTO> createBlankAccountForInstitution(@PathVariable UUID id, @RequestBody @Valid CreateBlankAccountRequestDTO createBlankAccountRequestDTO) {
        Account account = AccountMappers.convert(createBlankAccountRequestDTO, id.toString());
        Account createdAccount = tenantAwareAccountsService.createTenantAwareAccount(account, TenantAwareAccountCreationContext.AccountType.BLANK, id, null);
        return new ResponseEntity<>(AccountMappers.convert(createdAccount), HttpStatus.CREATED);
    }

    // TODO: finalist the process to create an account
    // creating an account should always be step 1: create account (creates keycloak account)
    // step 2: gives that account the admin privileges (adds roles to account, outlet-admin or so on)
    //TODO: allow institution admin authority to create admin accounts for their institution too
    @Operation(summary = "Creates an blank account and links institution-admin permissions for an institution", description = "Creates an admin account for the institution. This admin user can be used to add other accounts")
    @ApiResponses({@ApiResponse(responseCode = "201", description = "created"), @ApiResponse(responseCode = "409", description = "existing account for institution account already exists"), @ApiResponse(responseCode = "500", description = "unexpected internal server error has occurred")})
    @PostMapping("/institutions/{id}/accounts/institution-admins")
//    @HasRole("'aura-admin'")
    public ResponseEntity<CreateAccountResponseDTO> createInstitutionAdminAccountForInstitution(@PathVariable UUID id, @RequestBody @Valid CreateInstitutionAdminAccountDTO createInstitutionAdminAccountDTO) {
        Account account = AccountMappers.convert(createInstitutionAdminAccountDTO);
        account.setTenantId(id.toString());
        Account createdAccount = tenantAwareAccountsService.createTenantAwareAccount(account, TenantAwareAccountCreationContext.AccountType.INSTITUTION_ADMIN, id, null);
        return new ResponseEntity<>(AccountMappers.convert(createdAccount), HttpStatus.OK);
    }

    @Operation(summary = "Creates an blank account and links outlet-admin permissions for an institution", description = "link outlet-admin to base admin")
    @ApiResponse(responseCode = "200", description = "OK")
    @PostMapping("/institutions/{id}/outlets/{outlet_id}/accounts/outlet-admins")
    @HasResourcePermission(permission = "'institutions::' + #id + '::accounts:create-link-outlet-admin'")
    public ResponseEntity<CreateAccountResponseDTO> createAccountAndAddOutletAdminRole(@PathVariable UUID id, @PathVariable(name = "outlet_id") UUID outletId, @RequestBody CreateBlankAccountRequestDTO createBlankAccountRequestDTO) {
        Account account = AccountMappers.convert(createBlankAccountRequestDTO, id.toString());
        Account createdAccount = tenantAwareAccountsService.createTenantAwareAccount(account, TenantAwareAccountCreationContext.AccountType.OUTLET_ADMIN, id, List.of(outletId.toString()));
        tenantAwareAccountsService.createOutletAdminAndLinkToAccount(id, outletId, createdAccount.getId());
        return new ResponseEntity<>(AccountMappers.convert(createdAccount), HttpStatus.OK);
    }

    @Operation(summary = "links a client to a account", description = "links a client to an account")
    @ApiResponse(responseCode = "200", description = "OK")
    @PostMapping("/institutions/{id}/accounts/{account_id}/clients")
    @HasResourcePermission(permission = "'institutions::' + #id + '::accounts:link-client'")
    @Transactional
    public ResponseEntity<CreateAccountResponseDTO> linkExistingAccountToClient(@PathVariable UUID id, @PathVariable("account_id") UUID accountId, @RequestBody @Valid CreateClientRequestDTO createClientRequestDTO) {
        Account account = tenantAwareAccountsService.findById(accountId);
        Client newClient = ClientMappers.convert(createClientRequestDTO, id);
        newClient.setAccount(account);
        Client createdClient = clientService.linkClientToAccount(newClient, account.getId());
        return new ResponseEntity<>(AccountMappers.convert(account), HttpStatus.OK);
    }

    //TODO: create and link student with account
    @Operation(summary = "links a student into a client account", description = "create student and link student to an already created client account")
    @ApiResponse(responseCode = "200", description = "OK")
    @PostMapping("/institutions/{id}/accounts/{account_id}/students")
    @HasResourcePermission(permission = "'institutions::' + #id + '::accounts:link-student'")
    @Transactional
    public ResponseEntity<CreateStudentResponseDTO> linkExistingAccountToStudent(@PathVariable UUID id, @PathVariable("account_id") UUID accountId, @RequestBody @Valid CreateStudentDTO createStudentDTO) {
        Account account = tenantAwareAccountsService.findById(accountId);
        Student newStudent = StudentMappers.convert(createStudentDTO, id);
        Level level = levelsService.findById(createStudentDTO.getLevelId());
        newStudent.setLevel(level);
        Student createdStudent = studentsService.createStudentForInstitution(newStudent, id);
        tenantAwareAccountsService.linkAccountToStudent(account.getId(), createdStudent.getId());
        return new ResponseEntity<>(StudentMappers.convert(createdStudent), HttpStatus.OK);
    }


    @Operation(summary = "create an educator account", description = "create educator in a already created client account")
    @ApiResponse(responseCode = "200", description = "OK")
    @PostMapping("/institutions/{id}/accounts/{account_id}/educators")
    @HasResourcePermission(permission = "'institutions::' + #id + '::accounts:link-educator'")
    @Transactional
    public ResponseEntity<CreateEducatorResponseDTO> linkExistingAccountToEducator(@PathVariable UUID id, @PathVariable("account_id") UUID accountId, @RequestBody @Valid CreateEducatorDTO createEducatorDTO) {
        Account account = tenantAwareAccountsService.findById(accountId);
        Educator newEducator = EducatorMappers.convert(createEducatorDTO, id);
        Educator createdEducator = educatorsService.createEducatorForInstitution(newEducator, id);
        tenantAwareAccountsService.linkAccountToEducator(account.getId(), createdEducator.getId());
        return new ResponseEntity<>(EducatorMappers.convert(createdEducator), HttpStatus.OK);
    }

    @Operation(summary = "create an outlet-admin on top of base account and link", description = "link outlet-admin to base admin")
    @ApiResponse(responseCode = "200", description = "OK")
    @PatchMapping("/institutions/{id}/accounts/{account_id}/outlets/{outlet_id}/outlet-admins")
    @HasResourcePermission(permission = "'institutions::' + #id + '::accounts:create-link-outlet-admin'")
    public ResponseEntity<Void> linkExistingAccountToOutletAdmin(@PathVariable UUID id, @PathVariable(name = "account_id") UUID accountId, @PathVariable(name = "outlet_id") UUID outletId) {
        tenantAwareAccountsService.createOutletAdminAndLinkToAccount(id, outletId, accountId);
        return ResponseEntity.ok().build();
    }
}
