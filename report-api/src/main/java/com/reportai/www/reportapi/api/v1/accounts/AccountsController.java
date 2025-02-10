package com.reportai.www.reportapi.api.v1.accounts;

import com.reportai.www.reportapi.annotations.authorisation.HasResourcePermission;
import com.reportai.www.reportapi.api.v1.accounts.requests.CreateBlankAccountRequestDTO;
import com.reportai.www.reportapi.api.v1.accounts.requests.CreateEducatorDTO;
import com.reportai.www.reportapi.api.v1.accounts.requests.CreateInstitutionAdminAccountDTO;
import com.reportai.www.reportapi.api.v1.accounts.requests.CreateStudentClientRequestDTO;
import com.reportai.www.reportapi.api.v1.accounts.requests.CreateStudentDTO;
import com.reportai.www.reportapi.api.v1.accounts.responses.CreateAccountResponseDTO;
import com.reportai.www.reportapi.api.v1.accounts.responses.CreateEducatorResponseDTO;
import com.reportai.www.reportapi.api.v1.accounts.responses.CreateStudentClientResponse;
import com.reportai.www.reportapi.api.v1.accounts.responses.CreateStudentResponseDTO;
import com.reportai.www.reportapi.api.v1.accounts.responses.EducatorClientResponse;
import com.reportai.www.reportapi.api.v1.accounts.responses.StudentClientResponse;
import com.reportai.www.reportapi.entities.Account;
import com.reportai.www.reportapi.entities.Educator;
import com.reportai.www.reportapi.entities.Institution;
import com.reportai.www.reportapi.entities.Level;
import com.reportai.www.reportapi.entities.Student;
import com.reportai.www.reportapi.entities.Subject;
import com.reportai.www.reportapi.entities.personas.EducatorClientPersona;
import com.reportai.www.reportapi.entities.personas.StudentClientPersona;
import com.reportai.www.reportapi.exceptions.lib.ResourceNotFoundException;
import com.reportai.www.reportapi.mappers.AccountMappers;
import com.reportai.www.reportapi.mappers.EducatorMappers;
import com.reportai.www.reportapi.mappers.StudentMappers;
import com.reportai.www.reportapi.repositories.EducatorClientPersonaRepository;
import com.reportai.www.reportapi.repositories.InstitutionRepository;
import com.reportai.www.reportapi.repositories.StudentClientPersonaRepository;
import com.reportai.www.reportapi.repositories.SubjectRepository;
import com.reportai.www.reportapi.repositories.specifications.TenantSpecification;
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
    private final StudentsService studentsService;
    private final LevelsService levelsService;
    private final EducatorsService educatorsService;
    private final ClientService clientService;
    private final InstitutionRepository institutionRepository;
    private final StudentClientPersonaRepository studentClientPersonaRepository;
    private final SubjectRepository subjectRepository;
    private final EducatorClientPersonaRepository educatorClientPersonaRepository;

    @Autowired
    public AccountsController(TenantAwareAccountsService tenantAwareAccountsService, StudentsService studentsService, LevelsService levelsService, EducatorsService educatorsService, ClientService clientService, InstitutionRepository institutionRepository, StudentClientPersonaRepository studentClientPersonaRepository, SubjectRepository subjectRepository, EducatorClientPersonaRepository educatorClientPersonaRepository) {
        this.tenantAwareAccountsService = tenantAwareAccountsService;
        this.studentsService = studentsService;
        this.levelsService = levelsService;
        this.educatorsService = educatorsService;
        this.clientService = clientService;
        this.institutionRepository = institutionRepository;
        this.studentClientPersonaRepository = studentClientPersonaRepository;
        this.subjectRepository = subjectRepository;
        this.educatorClientPersonaRepository = educatorClientPersonaRepository;
    }


    //TODO: allow institution admin authority to create admin accounts for their institution too
    @Operation(summary = "Creates an blank account and links institution-admin permissions for an institution", description = "Creates an admin account for the institution. This admin user can be used to add other accounts")
    @ApiResponses({@ApiResponse(responseCode = "201", description = "created"), @ApiResponse(responseCode = "409", description = "existing account for institution account already exists"), @ApiResponse(responseCode = "500", description = "unexpected internal server error has occurred")})
    @PostMapping("/institutions/{id}/accounts/institution-admins")
//    @HasRole("'aura-admin'")
    public ResponseEntity<CreateAccountResponseDTO> createInstitutionAdminAccountForInstitution(@PathVariable UUID id, @RequestBody @Valid CreateInstitutionAdminAccountDTO createInstitutionAdminAccountDTO) {
        Account account = AccountMappers.convert(createInstitutionAdminAccountDTO);
        account.setTenantId(id.toString());
        Account createdAccount = tenantAwareAccountsService.createTenantAwareAccount(account, TenantAwareAccountCreationContext.AccountType.INSTITUTION_ADMIN, id, null, null);
        return new ResponseEntity<>(AccountMappers.convert(createdAccount), HttpStatus.OK);
    }

    @Operation(summary = "Creates an blank account and links outlet-admin permissions for an institution", description = "link outlet-admin to base admin")
    @ApiResponse(responseCode = "200", description = "OK")
    @PostMapping("/institutions/{id}/outlets/{outlet_id}/accounts/outlet-admins")
    @HasResourcePermission(permission = "'institutions::' + #id + '::accounts:create-link-outlet-admin'")
    public ResponseEntity<CreateAccountResponseDTO> createAccountAndAddOutletAdminRole(@PathVariable UUID id, @PathVariable(name = "outlet_id") UUID outletId, @RequestBody CreateBlankAccountRequestDTO createBlankAccountRequestDTO) {
        Account account = AccountMappers.convert(createBlankAccountRequestDTO, id.toString());
        Account createdAccount = tenantAwareAccountsService.createTenantAwareAccount(account, TenantAwareAccountCreationContext.AccountType.OUTLET_ADMIN, id, List.of(outletId.toString()), null);
        return new ResponseEntity<>(AccountMappers.convert(createdAccount), HttpStatus.OK);
    }

    @Operation(summary = "Creates an student client account and links permission to access report-mobile student dashboard feature", description = "Creates an student client account and links permission to access report-mobile student dashboard feature")
    @ApiResponse(responseCode = "200", description = "OK")
    @PostMapping("/institutions/{id}/accounts/student-clients")
    @HasResourcePermission(permission = "'institutions::' + #id + '::accounts::student-client-account:create'")
    public ResponseEntity<CreateStudentClientResponse> createStudentClientAccountInInstitution(@PathVariable UUID id, @RequestBody @Valid CreateStudentClientRequestDTO createStudentClientRequestDTO) {
        Account account = AccountMappers.convert(createStudentClientRequestDTO, id.toString());
        Account createdAccount = tenantAwareAccountsService.createTenantAwareAccount(account, TenantAwareAccountCreationContext.AccountType.STUDENT_CLIENT, id, null, createStudentClientRequestDTO.getRelationship());
        return new ResponseEntity<>(AccountMappers.convert(createdAccount, createStudentClientRequestDTO.getRelationship()), HttpStatus.OK);
    }

    @Operation(summary = "creates a student in a client account", description = "creates student in an already created client account")
    @ApiResponse(responseCode = "200", description = "OK")
    @PostMapping("/institutions/{id}/accounts/{account_id}/students")
    @HasResourcePermission(permission = "'institutions::' + #id + '::accounts:create-student'")
    @Transactional
    public ResponseEntity<CreateStudentResponseDTO> createStudentInClientAccount(@PathVariable UUID id, @PathVariable("account_id") UUID accountId, @RequestBody @Valid CreateStudentDTO createStudentDTO) {
        Account account = tenantAwareAccountsService.findById(accountId);
        Student newStudent = StudentMappers.convert(createStudentDTO, id);
        Level level = levelsService.findById(createStudentDTO.getLevelId());
        newStudent.setLevel(level);
        Student createdStudent = tenantAwareAccountsService.createStudentInAccount(accountId, newStudent);
        return new ResponseEntity<>(StudentMappers.convert(createdStudent), HttpStatus.OK);
    }

    @Operation(summary = "get student client details", description = "create an account for the clients of a institution.")
    @ApiResponse(responseCode = "200", description = "OK")
    @GetMapping("/institutions/{id}/accounts/student-clients")
    @HasResourcePermission(permission = "'institutions::' + #id + '::accounts::student-clients:read'")
    public ResponseEntity<List<StudentClientResponse>> getStudentClientsAccountOfInstitution(@PathVariable UUID id) {
        Institution institution = institutionRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("institution not found"));
        List<StudentClientPersona> studentClientPersona = studentClientPersonaRepository.findAll(TenantSpecification.forTenant(id.toString()));
        return new ResponseEntity<>(studentClientPersona.stream().map(StudentMappers::convert).toList(), HttpStatus.OK);
    }

    // TODO: get all student client accounts
    @Operation(summary = "get institution details", description = "create an account for the clients of a institution.")
    @ApiResponse(responseCode = "200", description = "OK")
    @GetMapping("/institutions/{id}/accounts/educator-clients")
    @HasResourcePermission(permission = "'institutions::' + #id + '::accounts::educator-clients:read'")
    public ResponseEntity<List<EducatorClientResponse>> getEducatorClientsAccountOfInstitution(@PathVariable UUID id) {
        Institution institution = institutionRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("institution not found"));
        List<EducatorClientPersona> educatorClientPersona = educatorClientPersonaRepository.findAll(TenantSpecification.forTenant(id.toString()));
        return new ResponseEntity<>(educatorClientPersona.stream().map(EducatorMappers::convert).toList(), HttpStatus.OK);
    }


    @Operation(summary = "Creates an educator client account and links permission to access report-mobile educator dashboard feature", description = "Creates an educator client account and links permission to access report-mobile educator dashboard feature")
    @ApiResponse(responseCode = "200", description = "OK")
    @PostMapping("/institutions/{id}/accounts/educator-clients")
    @HasResourcePermission(permission = "'institutions::' + #id + '::accounts::educator-client-account:create'")
    public ResponseEntity<CreateAccountResponseDTO> createEducatorClientAccountInInstitution(@PathVariable UUID id, @RequestBody @Valid CreateBlankAccountRequestDTO createEducatorClientRequestDTO) {
        Account account = AccountMappers.convert(createEducatorClientRequestDTO, id.toString());
        Account createdAccount = tenantAwareAccountsService.createTenantAwareAccount(account, TenantAwareAccountCreationContext.AccountType.EDUCATOR_CLIENT, id, null, null);
        return new ResponseEntity<>(AccountMappers.convert(createdAccount), HttpStatus.OK);
    }

    @Operation(summary = "creates a educator in a client account", description = "creates educator in an already created client account")
    @ApiResponse(responseCode = "200", description = "OK")
    @PostMapping("/institutions/{id}/outlets/{outlet_id}/accounts/{account_id}/educators")
    @HasResourcePermission(permission = "'institutions::' + #id + '::outlets::' + #outletId + '::accounts::educators:create'")
    @Transactional
    public ResponseEntity<CreateEducatorResponseDTO> createEducatorInClientAccount(@PathVariable UUID id, @PathVariable("outlet_id") UUID outletId, @PathVariable("account_id") UUID accountId, @RequestBody @Valid CreateEducatorDTO createEducatorDTO) {
        Account account = tenantAwareAccountsService.findById(accountId);
        Educator newEducator = EducatorMappers.convert(createEducatorDTO, id);
        List<Level> levels = levelsService.findByIds(createEducatorDTO.getLevelIds());
        List<Subject> subjects = subjectRepository.findAllById(createEducatorDTO.getSubjectIds());
        newEducator.setLevels(levels);
        newEducator.setSubjects(subjects);
        Educator createdEducator = tenantAwareAccountsService.createEducatorInAccountUnderOutlet(accountId, outletId, newEducator);
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

    @Operation(summary = "create an blank account for a institution", description = "create an blank account for a institution. This blank account can later be linked to a client, student, parent, educator, outlet and institution admin")
    @ApiResponse(responseCode = "201", description = "OK")
    @PostMapping("/institutions/{id}/accounts")
    @HasResourcePermission(permission = "'institutions::' + #id + '::accounts:create'")
    public ResponseEntity<CreateAccountResponseDTO> createBlankAccountForInstitution(@PathVariable UUID id, @RequestBody @Valid CreateBlankAccountRequestDTO createBlankAccountRequestDTO) {
        Account account = AccountMappers.convert(createBlankAccountRequestDTO, id.toString());
        Account createdAccount = tenantAwareAccountsService.createTenantAwareAccount(account, TenantAwareAccountCreationContext.AccountType.BLANK, id, null, null);
        return new ResponseEntity<>(AccountMappers.convert(createdAccount), HttpStatus.CREATED);
    }
}
