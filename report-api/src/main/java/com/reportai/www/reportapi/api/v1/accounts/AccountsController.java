package com.reportai.www.reportapi.api.v1.accounts;

import com.reportai.www.reportapi.api.v1.accounts.requests.CreateInstitutionAdminAccountDTO;
import com.reportai.www.reportapi.api.v1.accounts.requests.CreateUserDTO;
import com.reportai.www.reportapi.api.v1.accounts.responses.CreateAccountResponseDTO;
import com.reportai.www.reportapi.api.v1.students.requests.CreateStudentDTO;
import com.reportai.www.reportapi.api.v1.students.responses.CreateStudentResponseDTO;
import com.reportai.www.reportapi.entities.Account;
import com.reportai.www.reportapi.entities.Level;
import com.reportai.www.reportapi.entities.Student;
import com.reportai.www.reportapi.mappers.AccountMappers;
import com.reportai.www.reportapi.mappers.StudentMappers;
import com.reportai.www.reportapi.services.accounts.TenantAwareAccountsService;
import com.reportai.www.reportapi.services.accounts.creationstrategies.TenantAwareAccountCreationContext;
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
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
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

    @Autowired
    public AccountsController(TenantAwareAccountsService tenantAwareAccountsService, StudentsService studentsService, LevelsService levelsService) {
        this.tenantAwareAccountsService = tenantAwareAccountsService;
        this.studentsService = studentsService;
        this.levelsService = levelsService;
    }

    @Operation(summary = "create an account for the clients of a institution", description = "create an account for the clients of a institution. A client of a institution is defined as the person who is registering with the institution, it could be either a student or a parent. The client will be able to log in to the Aura Report mobile app after email verification")
    @ApiResponse(responseCode = "201", description = "OK")
    @PostMapping("/institutions/{id}/accounts")
    @PreAuthorize("hasRole(#id + '_institution-admin')")
    public ResponseEntity<CreateAccountResponseDTO> createClientAccountForInstitution(@PathVariable UUID id, @RequestBody @Valid CreateUserDTO createUserDTO) {
        Account account = AccountMappers.convert(createUserDTO, id.toString());
        Account createdAccount = tenantAwareAccountsService.createTenantAwareAccount(account, TenantAwareAccountCreationContext.AccountType.CLIENT, id);
        return new ResponseEntity<>(AccountMappers.convert(createdAccount), HttpStatus.CREATED);
    }

    @Operation(summary = "Creates an admin account for an institution ", description = "Creates an admin account for the institution. This admin user can be used to add other accounts")
    @ApiResponses({@ApiResponse(responseCode = "201", description = "created"), @ApiResponse(responseCode = "409", description = "existing account for institution account already exists"), @ApiResponse(responseCode = "500", description = "unexpected internal server error has occurred")})
    @PostMapping("/institutions/{id}/admin-accounts")
    @PreAuthorize("hasRole('aura-admin')")
    public ResponseEntity<CreateAccountResponseDTO> createAdminAccountForInstitution(@PathVariable UUID id, @RequestBody @Valid CreateInstitutionAdminAccountDTO createInstitutionAdminAccountDTO) {
        Account account = AccountMappers.convert(createInstitutionAdminAccountDTO);
        account.setTenantId(id.toString());
        Account createdAccount = tenantAwareAccountsService.createTenantAwareAccount(account, TenantAwareAccountCreationContext.AccountType.INSTITUTION_ADMIN, id);
        return new ResponseEntity<>(AccountMappers.convert(createdAccount), HttpStatus.OK);
    }

    //TODO: create student with account
    @Operation(summary = "create an student in a client account", description = "create student in a already created client account")
    @ApiResponse(responseCode = "201", description = "OK")
    @PostMapping("/institutions/{id}/accounts/{account_id}/students")
    @PreAuthorize("hasRole(#id + '_institution-admin')")
    @Transactional
    public ResponseEntity<CreateStudentResponseDTO> createClientAccountForInstitution(@PathVariable UUID id, @PathVariable("account_id") UUID accountId, @RequestBody @Valid CreateStudentDTO createStudentDTO) {
        Account account = tenantAwareAccountsService.findById(accountId);
        Student newStudent = StudentMappers.convert(createStudentDTO, id);
        Level level = levelsService.findById(createStudentDTO.getLevelId());
        newStudent.setLevel(level);
        Student createdStudent = studentsService.createStudentForInstitution(newStudent, id);
        tenantAwareAccountsService.linkAccountToStudent(account.getId(), createdStudent.getId());
        return new ResponseEntity<>(StudentMappers.convert(createdStudent), HttpStatus.OK);
    }
    // TODO: create educator on account

    // TODO: create admin with account
}
