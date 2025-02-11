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
import com.reportai.www.reportapi.repositories.EducatorRepository;
import com.reportai.www.reportapi.repositories.InstitutionAdminPersonaRepository;
import com.reportai.www.reportapi.repositories.InstitutionRepository;
import com.reportai.www.reportapi.repositories.OutletAdminPersonaRepository;
import com.reportai.www.reportapi.repositories.OutletRepository;
import com.reportai.www.reportapi.repositories.StudentClientPersonaRepository;
import com.reportai.www.reportapi.repositories.StudentRepository;
import com.reportai.www.reportapi.services.accounts.creationstrategies.BlankTenantAwareAccountCreationStrategy;
import com.reportai.www.reportapi.services.accounts.creationstrategies.EducatorClientTenantAwareAccountCreationStrategy;
import com.reportai.www.reportapi.services.accounts.creationstrategies.InstitutionAdminTenantAwareAccountCreationStrategy;
import com.reportai.www.reportapi.services.accounts.creationstrategies.OutletAdminTenantAwareAccountCreationStrategy;
import com.reportai.www.reportapi.services.accounts.creationstrategies.StudentClientTenantAwareAccountCreationStrategy;
import com.reportai.www.reportapi.services.accounts.creationstrategies.TenantAwareAccountCreationContext;
import com.reportai.www.reportapi.services.accounts.creationstrategies.TenantAwareAccountCreationStrategy;
import jakarta.transaction.Transactional;
import java.util.List;
import java.util.UUID;
import org.keycloak.admin.client.resource.ClientResource;
import org.keycloak.admin.client.resource.RealmResource;
import org.keycloak.admin.client.resource.UserResource;
import org.keycloak.admin.client.resource.UsersResource;
import org.keycloak.representations.idm.RoleRepresentation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * TenantAwareAccountsService manages TenantAwareAccounts
 * tenant aware accounts are accounts that belongs to a tenant (in this case an institution)
 * keycloak accounts (different from tenant aware accounts) are not tenant aware accounts as
 * users have 1 keycloak account that might be used to access multiple TenantAwareAccounts
 */
@Service
public class TenantAwareAccountsService {
    private final RealmResource realmResource;
    private final InstitutionRepository institutionRepository;
    private final AccountRepository accountRepository;
    private final TenantAwareAccountCreationContext tenantAwareAccountCreationContext = new TenantAwareAccountCreationContext();
    private final ClientResource clientResource;
    private final StudentRepository studentRepository;
    private final EducatorRepository educatorRepository;
    private final OutletRepository outletRepository;
    private final UsersResource usersResource;
    private final StudentClientPersonaRepository studentClientPersonaRepository;
    private final InstitutionAdminPersonaRepository institutionAdminPersonaRepository;
    private final EducatorClientPersonaRepository educatorClientPersonaRepository;
    private final OutletAdminPersonaRepository outletAdminPersonaRepository;

    @Autowired
    public TenantAwareAccountsService(RealmResource realmResource, InstitutionRepository institutionRepository, AccountRepository accountRepository, ClientResource clientResource, StudentRepository studentRepository, EducatorRepository educatorRepository, OutletRepository outletRepository, UsersResource usersResource, StudentClientPersonaRepository studentClientPersonaRepository, InstitutionAdminPersonaRepository institutionAdminPersonaRepository, EducatorClientPersonaRepository educatorClientPersonaRepository, OutletAdminPersonaRepository outletAdminPersonaRepository) {
        this.realmResource = realmResource;
        this.institutionRepository = institutionRepository;
        this.accountRepository = accountRepository;
        this.clientResource = clientResource;
        this.studentRepository = studentRepository;
        this.educatorRepository = educatorRepository;
        this.outletRepository = outletRepository;
        this.usersResource = usersResource;
        this.studentClientPersonaRepository = studentClientPersonaRepository;
        this.institutionAdminPersonaRepository = institutionAdminPersonaRepository;
        this.educatorClientPersonaRepository = educatorClientPersonaRepository;
        this.outletAdminPersonaRepository = outletAdminPersonaRepository;
    }

    // can create Client account, educator account, Institution_admin account and outlet_admin account
    // CLIENT: access to report mobile
    // EDUCATOR: access to report mobile
    // INSTITUTION_ADMIN: access to ops website
    // This method creates a logical multitenant aware account
    // OUTLET_ADMIN: access to ops website
    public Account createTenantAwareAccount(Account tenantAwareAccount, TenantAwareAccountCreationContext.AccountType accountType, UUID tenantId, List<String> outletIds, StudentClientPersona.RELATIONSHIP relationship) {
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
    private TenantAwareAccountCreationStrategy chooseStrategy(TenantAwareAccountCreationContext.AccountType accountType, Account tenantAwareAccount, UUID institutionId, List<String> outletIds, StudentClientPersona.RELATIONSHIP relationship) {
        return switch (accountType) {
            case BLANK ->
                    new BlankTenantAwareAccountCreationStrategy(institutionId, tenantAwareAccount, clientResource, realmResource, accountRepository, institutionRepository); // doesn't create any personas
            case STUDENT_CLIENT ->
                    new StudentClientTenantAwareAccountCreationStrategy(institutionId, tenantAwareAccount, clientResource, realmResource, accountRepository, institutionRepository, studentClientPersonaRepository, relationship); // creates studentClientPersona
            case EDUCATOR_CLIENT ->
                    new EducatorClientTenantAwareAccountCreationStrategy(institutionId, tenantAwareAccount, clientResource, realmResource, accountRepository, institutionRepository, educatorClientPersonaRepository);
            case INSTITUTION_ADMIN ->
                    new InstitutionAdminTenantAwareAccountCreationStrategy(institutionId, tenantAwareAccount, clientResource, realmResource, accountRepository, institutionRepository, institutionAdminPersonaRepository);
            case OUTLET_ADMIN ->
                    new OutletAdminTenantAwareAccountCreationStrategy(institutionId, tenantAwareAccount, clientResource, realmResource, accountRepository, institutionRepository, outletIds, outletRepository, outletAdminPersonaRepository);
            default ->
                    throw new UnsupportedOperationException(String.format("accountType of %s is not allowed", accountType));
        };
    }

    public Account findById(UUID id) {
        return accountRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("account does not exist"));
    }

    @Transactional
    public Account createOutletAdminAndLinkToAccount(UUID institutionId, UUID outletId, UUID accountId) {
        Account account = accountRepository.findById(accountId).orElseThrow(() -> new ResourceNotFoundException("account not found"));
        Outlet outlet = outletRepository.findById(outletId).orElseThrow(() -> new ResourceNotFoundException("outlet not found"));
        Institution institution = institutionRepository.findById(institutionId).orElseThrow(() -> new ResourceNotFoundException("institution not found"));
        UserResource userResource = usersResource.get(account.getUserId());
        // TODO: check that account is not already an outlet admin in this outlet
        OutletAdminPersona createdOutletAdminPersona = outletAdminPersonaRepository.save(OutletAdminPersona
                .builder()
                .tenantId(institutionId.toString())
                .account(account)
                .build());

        outlet.getOutletAdminPersonas().add(createdOutletAdminPersona);
        outletRepository.save(outlet);
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
        Account account = accountRepository.findById(accountId).orElseThrow(() -> new ResourceNotFoundException("no account found"));
        List<Persona> accountPersonas = account.getPersonas();
        List<Persona> studentClientPersonas = accountPersonas.stream().filter(persona -> persona instanceof StudentClientPersona).toList();
        if (studentClientPersonas.isEmpty()) {
            throw new ResourceNotFoundException("No student client persona found in account");
        }
        student.setInstitution(account.getInstitution());
        student.setStudentClientPersona((StudentClientPersona) accountPersonas.getFirst());
        return studentRepository.save(student);
    }

    @Transactional
    public Educator createEducatorInAccountUnderOutlet(UUID accountId, UUID outletId, Educator educator) {
        Account account = accountRepository.findById(accountId).orElseThrow(() -> new ResourceNotFoundException("no account found"));
        Outlet outlet = outletRepository.findById(outletId).orElseThrow(() -> new ResourceNotFoundException("no outlet found"));
        List<Persona> accountPersonas = account.getPersonas();
        List<Persona> educatorClientPersonas = accountPersonas.stream().filter(persona -> persona instanceof EducatorClientPersona).toList();
        if (educatorClientPersonas.isEmpty()) {
            throw new ResourceNotFoundException("No educator client persona found in account");
        }
        educator.setInstitution(account.getInstitution());
        educator.setEducatorClientPersona((EducatorClientPersona) accountPersonas.getFirst());
        educator.setOutlets(List.of(outlet));
        return educatorRepository.save(educator);
    }
}
