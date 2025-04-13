package com.reportai.www.reportapi.api.v1.accounts;

import com.reportai.www.reportapi.annotations.authorisation.HasResourcePermission;
import com.reportai.www.reportapi.api.v1.accounts.requests.CreateBlankAccountRequestDTO;
import com.reportai.www.reportapi.api.v1.accounts.requests.CreateInstitutionAdminAccountRequestDTO;
import com.reportai.www.reportapi.api.v1.accounts.requests.CreateStudentClientRequestDTO;
import com.reportai.www.reportapi.api.v1.accounts.responses.AccountResponseDTO;
import com.reportai.www.reportapi.api.v1.accounts.responses.EducatorClientResponseDTO;
import com.reportai.www.reportapi.api.v1.accounts.responses.ExpandedAccountResponse;
import com.reportai.www.reportapi.api.v1.accounts.responses.StudentClientResponseDTO;
import com.reportai.www.reportapi.entities.Account;
import com.reportai.www.reportapi.entities.Institution;
import com.reportai.www.reportapi.entities.personas.EducatorClientPersona;
import com.reportai.www.reportapi.entities.personas.StudentClientPersona;
import com.reportai.www.reportapi.exceptions.lib.ResourceNotFoundException;
import com.reportai.www.reportapi.mappers.AccountMappers;
import com.reportai.www.reportapi.mappers.EducatorMappers;
import com.reportai.www.reportapi.mappers.StudentMappers;
import com.reportai.www.reportapi.repositories.EducatorClientPersonaRepository;
import com.reportai.www.reportapi.repositories.InstitutionRepository;
import com.reportai.www.reportapi.repositories.StudentClientPersonaRepository;
import com.reportai.www.reportapi.repositories.specifications.TenantSpecification;
import com.reportai.www.reportapi.services.accounts.TenantAwareAccountsService;
import com.reportai.www.reportapi.services.accounts.creationstrategies.TenantAwareAccountCreationContext;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import java.util.UUID;
import lombok.extern.slf4j.Slf4j;
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
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Accounts APIs", description = "APIs for creating accounts in an institutions")
@RestController
@RequestMapping("/api/v1")
@Validated
@Slf4j
public class AccountsController {
    private final TenantAwareAccountsService tenantAwareAccountsService;
    private final InstitutionRepository institutionRepository;
    private final StudentClientPersonaRepository studentClientPersonaRepository;
    private final EducatorClientPersonaRepository educatorClientPersonaRepository;

    @Autowired
    public AccountsController(TenantAwareAccountsService tenantAwareAccountsService, InstitutionRepository institutionRepository, StudentClientPersonaRepository studentClientPersonaRepository, EducatorClientPersonaRepository educatorClientPersonaRepository) {
        this.tenantAwareAccountsService = tenantAwareAccountsService;
        this.institutionRepository = institutionRepository;
        this.studentClientPersonaRepository = studentClientPersonaRepository;
        this.educatorClientPersonaRepository = educatorClientPersonaRepository;
    }

    //TODO: allow institution admin authority to create admin accounts for their institution too
    @Operation(summary = "Creates an blank account and links institution-admin permissions for an institution", description = "Creates an admin account for the institution. This admin user can be used to add other accounts")
    @ApiResponses({@ApiResponse(responseCode = "201", description = "created"), @ApiResponse(responseCode = "409", description = "existing account for institution account already exists"), @ApiResponse(responseCode = "500", description = "unexpected internal server error has occurred")})
    @PostMapping("/institutions/{id}/accounts/institution-admins")
//    @HasRole("'aura-admin'")
    public ResponseEntity<AccountResponseDTO> createInstitutionAdminAccountForInstitution(@PathVariable UUID id, @RequestBody @Valid CreateInstitutionAdminAccountRequestDTO createInstitutionAdminAccountRequestDTO) {
        Account account = AccountMappers.convert(createInstitutionAdminAccountRequestDTO);
        account.setTenantId(id.toString());
        Account createdAccount = tenantAwareAccountsService.createTenantAwareAccount(account, TenantAwareAccountCreationContext.AccountType.INSTITUTION_ADMIN, id, null, null);
        return new ResponseEntity<>(AccountMappers.convert(createdAccount), HttpStatus.OK);
    }

    @Operation(summary = "Creates an blank account and links outlet-admin permissions for an institution", description = "link outlet-admin to base admin")
    @ApiResponse(responseCode = "200", description = "OK")
    @PostMapping("/institutions/{id}/outlets/{outlet_id}/accounts/outlet-admins")
    @HasResourcePermission(permission = "'institutions::' + #id + '::accounts:create-link-outlet-admin'")
    public ResponseEntity<AccountResponseDTO> createAccountAndAddOutletAdminRole(@PathVariable UUID id, @PathVariable(name = "outlet_id") UUID outletId, @RequestBody CreateBlankAccountRequestDTO createBlankAccountRequestDTO) {
        Account account = AccountMappers.convert(createBlankAccountRequestDTO, id.toString());
        Account createdAccount = tenantAwareAccountsService.createTenantAwareAccount(account, TenantAwareAccountCreationContext.AccountType.OUTLET_ADMIN, id, List.of(outletId), null);
        return new ResponseEntity<>(AccountMappers.convert(createdAccount), HttpStatus.OK);
    }

    @Operation(summary = "Creates an student client account and links permission to access report-mobile student dashboard feature", description = "Creates an student client account and links permission to access report-mobile student dashboard feature")
    @ApiResponse(responseCode = "200", description = "OK")
    @PostMapping("/institutions/{id}/accounts/student-clients")
    @HasResourcePermission(permission = "'institutions::' + #id + '::accounts::student-client-account:create'")
    public ResponseEntity<StudentClientResponseDTO> createStudentClientAccountInInstitution(@PathVariable UUID id, @RequestBody @Valid CreateStudentClientRequestDTO createStudentClientRequestDTO) {
        Account account = AccountMappers.convert(createStudentClientRequestDTO, id.toString());
        Account createdAccount = tenantAwareAccountsService.createTenantAwareAccount(account, TenantAwareAccountCreationContext.AccountType.STUDENT_CLIENT, id, null, createStudentClientRequestDTO.getRelationship());
        return new ResponseEntity<>(AccountMappers.convert(createdAccount, createStudentClientRequestDTO.getRelationship()), HttpStatus.OK);
    }

    @Operation(summary = "get student client details", description = "create an account for the clients of a institution.")
    @ApiResponse(responseCode = "200", description = "OK")
    @GetMapping("/institutions/{id}/accounts/student-clients")
    @HasResourcePermission(permission = "'institutions::' + #id + '::accounts::student-clients:read'")
    public ResponseEntity<List<StudentClientResponseDTO>> getStudentClientsAccountOfInstitution(@PathVariable UUID id) {
        Institution institution = institutionRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("institution not found"));
        List<StudentClientPersona> studentClientPersona = studentClientPersonaRepository.findAll(TenantSpecification.forTenant(id.toString()));
        return new ResponseEntity<>(studentClientPersona.stream().map(StudentMappers::convert).toList(), HttpStatus.OK);
    }

    // TODO: get all student client accounts
    @Operation(summary = "get institution details", description = "create an account for the clients of a institution.")
    @ApiResponse(responseCode = "200", description = "OK")
    @GetMapping("/institutions/{id}/accounts/educator-clients")
    @HasResourcePermission(permission = "'institutions::' + #id + '::accounts::educator-clients:read'")
    public ResponseEntity<List<EducatorClientResponseDTO>> getEducatorClientsAccountOfInstitution(@PathVariable UUID id) {
        Institution institution = institutionRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("institution not found"));
        List<EducatorClientPersona> educatorClientPersona = educatorClientPersonaRepository.findAll(TenantSpecification.forTenant(id.toString()));
        return new ResponseEntity<>(educatorClientPersona.stream().map(EducatorMappers::convert).toList(), HttpStatus.OK);
    }


    @Operation(summary = "Creates an educator client account and links permission to access report-mobile educator dashboard feature", description = "Creates an educator client account and links permission to access report-mobile educator dashboard feature")
    @ApiResponse(responseCode = "200", description = "OK")
    @PostMapping("/institutions/{id}/accounts/educator-clients")
    @HasResourcePermission(permission = "'institutions::' + #id + '::accounts::educator-client-account:create'")
    public ResponseEntity<AccountResponseDTO> createEducatorClientAccountInInstitution(@PathVariable UUID id, @RequestBody @Valid CreateBlankAccountRequestDTO createEducatorClientRequestDTO) {
        Account account = AccountMappers.convert(createEducatorClientRequestDTO, id.toString());
        Account createdAccount = tenantAwareAccountsService.createTenantAwareAccount(account, TenantAwareAccountCreationContext.AccountType.EDUCATOR_CLIENT, id, null, null);
        return new ResponseEntity<>(AccountMappers.convert(createdAccount), HttpStatus.OK);
    }

    @Operation(summary = "create an outlet-admin on top of base account and link", description = "link outlet-admin to base admin")
    @ApiResponse(responseCode = "200", description = "OK")
    @PatchMapping("/institutions/{id}/accounts/{account_id}/outlets/{outlet_id}/outlet-admins")
    @HasResourcePermission(permission = "'institutions::' + #id + '::accounts:create-link-outlet-admin'")
    public ResponseEntity<Void> linkExistingAccountToOutletAdmin(@PathVariable UUID id, @PathVariable(name = "account_id") UUID accountId, @PathVariable(name = "outlet_id") UUID outletId) {
        tenantAwareAccountsService.grantOutletAdminInTenantAwareAccount(outletId, accountId);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "create an blank account for a institution", description = "create an blank account for a institution. This blank account can later be linked to a client, student, parent, educator, outlet and institution admin")
    @ApiResponse(responseCode = "201", description = "OK")
    @PostMapping("/institutions/{id}/accounts")
    @HasResourcePermission(permission = "'institutions::' + #id + '::accounts:create'")
    public ResponseEntity<AccountResponseDTO> createBlankAccountForInstitution(@PathVariable UUID id, @RequestBody @Valid CreateBlankAccountRequestDTO createBlankAccountRequestDTO) {
        Account account = AccountMappers.convert(createBlankAccountRequestDTO, id.toString());
        Account createdAccount = tenantAwareAccountsService.createTenantAwareAccount(account, TenantAwareAccountCreationContext.AccountType.BLANK, id, null, null);
        return new ResponseEntity<>(AccountMappers.convert(createdAccount), HttpStatus.CREATED);
    }

    @Operation(summary = "get all account for a institution", description = "get all accounts for a institution. This blank account can later be linked to a client, student, parent, educator, outlet and institution admin")
    @ApiResponse(responseCode = "201", description = "OK")
    @GetMapping("/institutions/{id}/accounts")
    @HasResourcePermission(permission = "'institutions::' + #id + '::accounts:read'")
    public ResponseEntity<List<ExpandedAccountResponse>> getAllAccountsForInstitution(@PathVariable UUID id) {
        List<ExpandedAccountResponse> response = tenantAwareAccountsService
                .getAllTenantAwareAccounts()
                .stream()
                .map(account -> {
                    ExpandedAccountResponse expandedAccountResponse = AccountMappers.convertExpanded(account);
                    expandedAccountResponse.setPendingAccountActions(tenantAwareAccountsService.getPendingActionsOfTenantAwareAccount(account.getId()));
                    return expandedAccountResponse;
                })
                .toList();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
