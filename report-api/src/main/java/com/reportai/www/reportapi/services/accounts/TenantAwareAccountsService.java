package com.reportai.www.reportapi.services.accounts;

import com.reportai.www.reportapi.entities.Account;
import com.reportai.www.reportapi.entities.Educator;
import com.reportai.www.reportapi.entities.Institution;
import com.reportai.www.reportapi.entities.Outlet;
import com.reportai.www.reportapi.entities.Student;
import com.reportai.www.reportapi.entities.personas.EducatorClientPersona;
import com.reportai.www.reportapi.entities.personas.OutletAdminPersona;
import com.reportai.www.reportapi.entities.personas.Persona;
import com.reportai.www.reportapi.entities.personas.StudentClientPersona;
import com.reportai.www.reportapi.exceptions.lib.ResourceNotFoundException;
import com.reportai.www.reportapi.repositories.AccountRepository;
import com.reportai.www.reportapi.repositories.EducatorClientPersonaRepository;
import com.reportai.www.reportapi.repositories.InstitutionAdminPersonaRepository;
import com.reportai.www.reportapi.repositories.OutletAdminPersonaRepository;
import com.reportai.www.reportapi.repositories.StudentClientPersonaRepository;
import com.reportai.www.reportapi.services.accounts.creationstrategies.BlankTenantAwareAccountCreationStrategy;
import com.reportai.www.reportapi.services.accounts.creationstrategies.EducatorClientTenantAwareAccountCreationStrategy;
import com.reportai.www.reportapi.services.accounts.creationstrategies.InstitutionAdminTenantAwareAccountCreationStrategy;
import com.reportai.www.reportapi.services.accounts.creationstrategies.OutletAdminTenantAwareAccountCreationStrategy;
import com.reportai.www.reportapi.services.accounts.creationstrategies.StudentClientTenantAwareAccountCreationStrategy;
import com.reportai.www.reportapi.services.accounts.creationstrategies.TenantAwareAccountCreationContext;
import com.reportai.www.reportapi.services.accounts.creationstrategies.TenantAwareAccountCreationStrategy;
import com.reportai.www.reportapi.services.common.BaseServiceTemplate;
import com.reportai.www.reportapi.services.educators.EducatorsService;
import com.reportai.www.reportapi.services.institutions.InstitutionsService;
import com.reportai.www.reportapi.services.outlets.OutletsService;
import com.reportai.www.reportapi.services.students.StudentsService;
import jakarta.transaction.Transactional;
import java.util.List;
import java.util.UUID;
import org.keycloak.admin.client.resource.ClientResource;
import org.keycloak.admin.client.resource.RealmResource;
import org.keycloak.admin.client.resource.UserResource;
import org.keycloak.admin.client.resource.UsersResource;
import org.keycloak.representations.idm.RoleRepresentation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

/**
 * TenantAwareAccountsService manages TenantAwareAccounts
 * tenant aware accounts are accounts that belongs to a tenant (in this case an institution)
 * keycloak accounts (different from tenant aware accounts) are not tenant aware accounts as
 * users have 1 keycloak account that might be used to access multiple TenantAwareAccounts
 */
@Service
public class TenantAwareAccountsService implements BaseServiceTemplate<Account, UUID> {
    private final RealmResource realmResource;
    private final InstitutionsService institutionsService;
    private final AccountRepository accountRepository;
    private final TenantAwareAccountCreationContext tenantAwareAccountCreationContext = new TenantAwareAccountCreationContext();
    private final ClientResource clientResource;
    private final StudentsService studentsService;
    private final EducatorsService educatorsService;
    private final OutletsService outletsService;
    private final UsersResource usersResource;
    private final StudentClientPersonaRepository studentClientPersonaRepository;
    private final InstitutionAdminPersonaRepository institutionAdminPersonaRepository;
    private final EducatorClientPersonaRepository educatorClientPersonaRepository;
    private final OutletAdminPersonaRepository outletAdminPersonaRepository;

    @Autowired
    public TenantAwareAccountsService(RealmResource realmResource, InstitutionsService institutionsService, AccountRepository accountRepository, ClientResource clientResource, StudentsService studentsService, EducatorsService educatorsService, OutletsService outletsService, UsersResource usersResource, StudentClientPersonaRepository studentClientPersonaRepository, InstitutionAdminPersonaRepository institutionAdminPersonaRepository, EducatorClientPersonaRepository educatorClientPersonaRepository, OutletAdminPersonaRepository outletAdminPersonaRepository) {
        this.realmResource = realmResource;
        this.institutionsService = institutionsService;
        this.accountRepository = accountRepository;
        this.clientResource = clientResource;
        this.studentsService = studentsService;
        this.educatorsService = educatorsService;
        this.outletsService = outletsService;
        this.usersResource = usersResource;
        this.studentClientPersonaRepository = studentClientPersonaRepository;
        this.institutionAdminPersonaRepository = institutionAdminPersonaRepository;
        this.educatorClientPersonaRepository = educatorClientPersonaRepository;
        this.outletAdminPersonaRepository = outletAdminPersonaRepository;
    }

    @Override
    public JpaRepository<Account, UUID> getRepository() {
        return this.accountRepository;
    }

    // can create Client account, educator account, Institution_admin account and outlet_admin account
    // CLIENT: access to report mobile
    // EDUCATOR: access to report mobile
    // INSTITUTION_ADMIN: access to ops website
    // This method creates a logical multitenant aware account
    // OUTLET_ADMIN: access to ops website
    public Account createTenantAwareAccount(Account tenantAwareAccount, TenantAwareAccountCreationContext.AccountType accountType, UUID tenantId, List<UUID> outletIds, StudentClientPersona.RELATIONSHIP relationship) {
        // create tenantAwareAccount according to the strategy
        // TODO: strategy to create keycloak user
        return tenantAwareAccountCreationContext.createTenantAwareAccount(chooseStrategy(accountType, tenantAwareAccount, tenantId, outletIds, relationship));
    }


    /**
     * helper method to choose account creation strategy
     *
     * @param accountType
     * @param tenantAwareAccount
     * @param institutionId
     * @return
     */
    private TenantAwareAccountCreationStrategy chooseStrategy(TenantAwareAccountCreationContext.AccountType accountType, Account tenantAwareAccount, UUID institutionId, List<UUID> outletIds, StudentClientPersona.RELATIONSHIP relationship) {
        return switch (accountType) {
            case BLANK -> new BlankTenantAwareAccountCreationStrategy(
                    BlankTenantAwareAccountCreationStrategy.CreateBlankTenantAwareAccountParams
                            .builder()
                            .institutionId(institutionId)
                            .requestedAccount(tenantAwareAccount)
                            .clientResource(clientResource)
                            .realmResource(realmResource)
                            .accountRepository(accountRepository)
                            .institutionsService(institutionsService)
                            .build()); // doesn't create any personas
            case STUDENT_CLIENT -> new StudentClientTenantAwareAccountCreationStrategy(
                    StudentClientTenantAwareAccountCreationStrategy.CreateStudentClientTenantAwareAccountParams
                            .builder()
                            .institutionId(institutionId)
                            .requestedAccount(tenantAwareAccount)
                            .clientResource(clientResource)
                            .realmResource(realmResource)
                            .accountRepository(accountRepository)
                            .institutionsService(institutionsService)
                            .studentClientPersonaRepository(studentClientPersonaRepository)
                            .relationship(relationship)
                            .build()); // creates studentClientPersona
            case EDUCATOR_CLIENT -> new EducatorClientTenantAwareAccountCreationStrategy(
                    EducatorClientTenantAwareAccountCreationStrategy.CreateEducatorClientTenantAwareAccountParams
                            .builder()
                            .institutionId(institutionId)
                            .requestedAccount(tenantAwareAccount)
                            .clientResource(clientResource)
                            .realmResource(realmResource)
                            .accountRepository(accountRepository)
                            .institutionsService(institutionsService)
                            .educatorClientPersonaRepository(educatorClientPersonaRepository)
                            .build());
            case INSTITUTION_ADMIN ->
                    new InstitutionAdminTenantAwareAccountCreationStrategy(InstitutionAdminTenantAwareAccountCreationStrategy.CreateInstitutionAdminTenantAwareAccountParams.builder()
                            .institutionId(institutionId)
                            .requestedAccount(tenantAwareAccount)
                            .clientResource(clientResource)
                            .realmResource(realmResource)
                            .accountRepository(accountRepository)
                            .institutionsService(institutionsService)
                            .institutionsService(institutionsService)
                            .institutionAdminPersonaRepository(institutionAdminPersonaRepository)
                            .build());
            case OUTLET_ADMIN -> new OutletAdminTenantAwareAccountCreationStrategy(
                    OutletAdminTenantAwareAccountCreationStrategy.CreateOutletAdminTenantAwareAccountParams
                            .builder()
                            .institutionId(institutionId)
                            .requestedAccount(tenantAwareAccount)
                            .clientResource(clientResource)
                            .realmResource(realmResource)
                            .accountRepository(accountRepository)
                            .institutionsService(institutionsService)
                            .outletIds(outletIds)
                            .outletsService(outletsService)
                            .outletAdminPersonaRepository(outletAdminPersonaRepository)
                            .build()
            );
            default ->
                    throw new UnsupportedOperationException(String.format("accountType of %s is not allowed", accountType));
        };
    }

    @Transactional
    public Account createOutletAdminAndLinkToAccount(UUID institutionId, UUID outletId, UUID accountId) {
        Account account = findById(accountId);
        Outlet outlet = outletsService.findById(outletId);
        Institution institution = institutionsService.findById(institutionId);
        UserResource userResource = usersResource.get(account.getUserId());
        // TODO: check that account is not already an outlet admin in this outlet
        OutletAdminPersona createdOutletAdminPersona = outletAdminPersonaRepository.save(OutletAdminPersona
                .builder()
                .tenantId(institutionId.toString())
                .account(account)
                .build());

        outlet.addOutletAdminPersona(createdOutletAdminPersona);
        RoleRepresentation tenantAwareOutletAdminRole = clientResource
                .roles()
                .get(String.format("%s_%s_outlet-admin", institution.getId(), outlet.getId()))
                .toRepresentation();
        userResource
                .roles()
                .clientLevel(clientResource.toRepresentation().getId())
                .add(List.of(tenantAwareOutletAdminRole));
        return account;
    }

    @Transactional
    public Student createStudentInAccount(UUID accountId, Student student) {
        Account account = findById(accountId);
        List<Persona> accountPersonas = account.getPersonas();
        List<Persona> studentClientPersonas = accountPersonas.stream().filter(persona -> persona instanceof StudentClientPersona).toList();
        if (studentClientPersonas.isEmpty()) {
            throw new ResourceNotFoundException("No student client persona found in account");
        }
        Student newStudent = studentsService.create(student);
        studentsService
                .addInstitution(newStudent.getId(), account.getInstitution().getId());
        return studentsService.addStudentClientPersona(newStudent.getId(), accountPersonas.getFirst().getId());
    }

    @Transactional
    public Educator createEducatorInAccountUnderOutlet(UUID accountId, UUID outletId, Educator educator) {
        Account account = findById(accountId);
        Outlet outlet = outletsService.findById(outletId);
        List<Persona> accountPersonas = account.getPersonas();
        List<Persona> educatorClientPersonas = accountPersonas.stream().filter(persona -> persona instanceof EducatorClientPersona).toList();
        if (educatorClientPersonas.isEmpty()) {
            throw new ResourceNotFoundException("No educator client persona found in account");
        }

        Educator newEducator = educatorsService.create(educator);
        educatorsService.addInstitution(newEducator.getId(), account.getInstitution().getId());
        educatorsService.addEducatorClientPersona(newEducator.getId(), accountPersonas.getFirst().getId());
        return educatorsService.addOulets(newEducator.getId(), List.of(outlet.getId()));
    }
}
