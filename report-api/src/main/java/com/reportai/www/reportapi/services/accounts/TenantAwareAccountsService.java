package com.reportai.www.reportapi.services.accounts;

import com.reportai.www.reportapi.entities.Account;
import com.reportai.www.reportapi.entities.Outlet;
import com.reportai.www.reportapi.entities.Student;
import com.reportai.www.reportapi.entities.educators.Educator;
import com.reportai.www.reportapi.entities.personas.EducatorClientPersona;
import com.reportai.www.reportapi.entities.personas.OutletAdminPersona;
import com.reportai.www.reportapi.entities.personas.Persona;
import com.reportai.www.reportapi.entities.personas.StudentClientPersona;
import com.reportai.www.reportapi.exceptions.lib.ResourceNotFoundException;
import com.reportai.www.reportapi.repositories.AccountRepository;
import com.reportai.www.reportapi.repositories.EducatorClientPersonaRepository;
import com.reportai.www.reportapi.repositories.EducatorRepository;
import com.reportai.www.reportapi.repositories.InstitutionAdminPersonaRepository;
import com.reportai.www.reportapi.repositories.OutletAdminPersonaRepository;
import com.reportai.www.reportapi.repositories.StudentClientPersonaRepository;
import com.reportai.www.reportapi.repositories.StudentRepository;
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
import java.util.Collection;
import java.util.List;
import java.util.UUID;
import org.keycloak.admin.client.resource.ClientResource;
import org.keycloak.admin.client.resource.RealmResource;
import org.keycloak.admin.client.resource.UserResource;
import org.keycloak.admin.client.resource.UsersResource;
import org.keycloak.representations.idm.RoleRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

/**
 * IdpUserAccount (keycloak) and TenantAwareAccounts(Accounts in DB) have a one-to-many relationship
 * TenantAwareAccountsService manages TenantAwareAccounts
 * tenant aware accounts are accounts that belongs to a tenant (an institution)
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
    private final StudentRepository studentRepository;
    private final EducatorRepository educatorRepository;

    @Autowired
    public TenantAwareAccountsService(RealmResource realmResource, InstitutionsService institutionsService, AccountRepository accountRepository, ClientResource clientResource, StudentsService studentsService, EducatorsService educatorsService, OutletsService outletsService, UsersResource usersResource, StudentClientPersonaRepository studentClientPersonaRepository, InstitutionAdminPersonaRepository institutionAdminPersonaRepository, EducatorClientPersonaRepository educatorClientPersonaRepository, OutletAdminPersonaRepository outletAdminPersonaRepository,
                                      StudentRepository studentRepository,
                                      EducatorRepository educatorRepository) {
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
        this.studentRepository = studentRepository;
        this.educatorRepository = educatorRepository;
    }

    @Override
    public JpaRepository<Account, UUID> getRepository() {
        return this.accountRepository;
    }

    public Account createTenantAwareAccount(Account tenantAwareAccount, TenantAwareAccountCreationContext.AccountType accountType, UUID tenantId, List<UUID> outletIds, StudentClientPersona.RELATIONSHIP relationship) {
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

    /**
     * Grants an account with outlet admin rights to a specified outlet
     *
     * @param outletId  outlet's outlet admin permission
     * @param accountId account
     * @return
     */
    @Transactional
    public Account grantOutletAdminInTenantAwareAccount(UUID outletId, UUID accountId) {
        Account account = findById(accountId);
        Outlet outlet = outletsService.findById(outletId);
        UserResource userResource = usersResource.get(account.getUserId());
        // TODO: check that account is not already an outlet admin in this outlet

        // check if existing outletAdminPersona exists
        List<Persona> existingOutletAdminPersona = account.getPersonas().stream().filter(persona -> persona instanceof OutletAdminPersona).toList();

        OutletAdminPersona outletAdminPersona = existingOutletAdminPersona.isEmpty() ? createOutletAdminPersona(account) : (OutletAdminPersona) existingOutletAdminPersona.getFirst();

        outlet.addOutletAdminPersona(outletAdminPersona);
        RoleRepresentation tenantAwareOutletAdminRole = clientResource
                .roles()
                .get(String.format("%s_%s_outlet-admin", outlet.getTenantId(), outlet.getId()))
                .toRepresentation();
        userResource
                .roles()
                .clientLevel(clientResource.toRepresentation().getId())
                .add(List.of(tenantAwareOutletAdminRole));
        return account;
    }

    @Transactional
    public OutletAdminPersona createOutletAdminPersona(Account account) {
        return outletAdminPersonaRepository.save(
                OutletAdminPersona
                        .builder()
                        .account(account)
                        .build());
    }


    @Transactional
    public Student createStudentInTenantAwareAccount(UUID accountId, Student student) {
        Account account = findById(accountId);
        Student linkedTransientStudent = linkStudentToAccount(student, account);
        return studentRepository.save(linkedTransientStudent);
    }

    private Student linkStudentToAccount(Student transientStudent, Account createdAccount) {
        List<Persona> existingStudentClientPersonas = createdAccount.getPersonas().stream().filter(persona -> persona instanceof StudentClientPersona).toList();
        if (existingStudentClientPersonas.isEmpty()) {
            throw new ResourceNotFoundException("No student client persona found in account, please register as a student");
        }
        transientStudent.addStudentClientPersona((StudentClientPersona) existingStudentClientPersonas.getFirst());
        return transientStudent;
    }


    /**
     * creates educator in an existing tenantAwareAccount under an outlet
     * tenantAwareAccount must already have the Educator Client persona
     *
     * @param accountId
     * @param outletId
     * @param levelIds
     * @param subjectIds
     * @param educator
     * @return
     */
    @Transactional
    public Educator createEducatorInTenantAwareAccountInOutlet(UUID accountId, UUID outletId, List<UUID> levelIds, List<UUID> subjectIds, Educator educator) {
        Account tenantAwareAccount = findById(accountId);
        Educator createdEducator = educatorsService.create(educator, levelIds, subjectIds);
        Outlet outlet = outletsService.findById(outletId);
        linkEducatorToTenantAwareAccount(createdEducator, tenantAwareAccount);
        return educatorsService.attachToOutlets(createdEducator.getId(), List.of(outlet.getId()));
    }

    private Educator linkEducatorToTenantAwareAccount(Educator createdEducator, Account createdAccount) {
        List<Persona> existingEducatorClientPersonas = createdAccount.getPersonas().stream().filter(persona -> persona instanceof EducatorClientPersona).toList();
        if (existingEducatorClientPersonas.isEmpty()) {
            throw new ResourceNotFoundException("No educator client persona found in account, please register as a educator");
        }
        createdEducator.addEducatorClientPersona((EducatorClientPersona) existingEducatorClientPersonas.getFirst());
        return educatorRepository.save(createdEducator);
    }

    /**
     * get all accounts in a tenant
     *
     * @return
     */
    public Collection<Account> getAllTenantAwareAccounts() {
        return accountRepository.findAll();
    }

    /**
     * get required actions for an account in a tenant
     *
     * @param accountId
     * @return
     */
    public List<String> getPendingActionsOfTenantAwareAccount(UUID accountId) {
        Account account = findById(accountId);
        UserResource userResource = usersResource.get(account.getUserId());
        UserRepresentation userAccountInIdp = userResource.toRepresentation();
        return userAccountInIdp.getRequiredActions();
    }
}
