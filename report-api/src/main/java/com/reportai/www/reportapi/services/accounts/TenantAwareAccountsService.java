package com.reportai.www.reportapi.services.accounts;

import com.reportai.www.reportapi.entities.Account;
import com.reportai.www.reportapi.entities.Educator;
import com.reportai.www.reportapi.entities.Institution;
import com.reportai.www.reportapi.entities.Outlet;
import com.reportai.www.reportapi.entities.OutletAdmin;
import com.reportai.www.reportapi.entities.Student;
import com.reportai.www.reportapi.exceptions.lib.ResourceNotFoundException;
import com.reportai.www.reportapi.repositories.AccountRepository;
import com.reportai.www.reportapi.repositories.EducatorRepository;
import com.reportai.www.reportapi.repositories.InstitutionRepository;
import com.reportai.www.reportapi.repositories.OutletAdminRepository;
import com.reportai.www.reportapi.repositories.OutletRepository;
import com.reportai.www.reportapi.repositories.StudentRepository;
import com.reportai.www.reportapi.services.accounts.creationstrategies.BlankTenantAwareAccountCreationStrategy;
import com.reportai.www.reportapi.services.accounts.creationstrategies.ClientTenantAwareAccountCreationStrategy;
import com.reportai.www.reportapi.services.accounts.creationstrategies.EducatorTenantAwareAccountCreationStrategy;
import com.reportai.www.reportapi.services.accounts.creationstrategies.InstitutionAdminTenantAwareAccountCreationStrategy;
import com.reportai.www.reportapi.services.accounts.creationstrategies.OutletAdminTenantAwareAccountCreationStrategy;
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
    private final OutletAdminRepository outletAdminRepository;

    @Autowired
    public TenantAwareAccountsService(RealmResource realmResource, InstitutionRepository institutionRepository, AccountRepository accountRepository, ClientResource clientResource, StudentRepository studentRepository, EducatorRepository educatorRepository, OutletRepository outletRepository, UsersResource usersResource, OutletAdminRepository outletAdminRepository) {
        this.realmResource = realmResource;
        this.institutionRepository = institutionRepository;
        this.accountRepository = accountRepository;
        this.clientResource = clientResource;
        this.studentRepository = studentRepository;
        this.educatorRepository = educatorRepository;
        this.outletRepository = outletRepository;
        this.usersResource = usersResource;
        this.outletAdminRepository = outletAdminRepository;
    }

    // can create Client account, educator account, Institution_admin account and outlet_admin account
    // CLIENT: access to report mobile
    // EDUCATOR: access to report mobile
    // INSTITUTION_ADMIN: access to ops website
    // This method creates a logical multitenant aware account
    // OUTLET_ADMIN: access to ops website
    public Account createTenantAwareAccount(Account tenantAwareAccount, TenantAwareAccountCreationContext.AccountType accountType, UUID tenantId, List<String> outletIds) {
        // create tenantAwareAccount according to the strategy
        return tenantAwareAccountCreationContext.createTenantAwareAccount(chooseStrategy(accountType, tenantAwareAccount, tenantId, outletIds));
    }


    /**
     * helper method to choose account creation strategy
     *
     * @param accountType
     * @param tenantAwareAccount
     * @param institutionId
     * @return
     */
    private TenantAwareAccountCreationStrategy chooseStrategy(TenantAwareAccountCreationContext.AccountType accountType, Account tenantAwareAccount, UUID institutionId, List<String> outletIds) {
        return switch (accountType) {
            case BLANK ->
                    new BlankTenantAwareAccountCreationStrategy(institutionId, tenantAwareAccount, clientResource, realmResource, accountRepository, institutionRepository);
            case CLIENT ->
                    new ClientTenantAwareAccountCreationStrategy(institutionId, tenantAwareAccount, clientResource, realmResource, accountRepository, institutionRepository);
            case EDUCATOR ->
                    new EducatorTenantAwareAccountCreationStrategy(institutionId, tenantAwareAccount, clientResource, realmResource, accountRepository, institutionRepository);
            case INSTITUTION_ADMIN ->
                    new InstitutionAdminTenantAwareAccountCreationStrategy(institutionId, tenantAwareAccount, clientResource, realmResource, accountRepository, institutionRepository);
            case OUTLET_ADMIN ->
                    new OutletAdminTenantAwareAccountCreationStrategy(institutionId, tenantAwareAccount, clientResource, realmResource, accountRepository, institutionRepository, outletIds);
            default ->
                    throw new UnsupportedOperationException(String.format("accountType of %s is not allowed", accountType));
        };
    }

    public Account findById(UUID id) {
        return accountRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("account does not exist"));
    }

    @Transactional
    public Account linkAccountToStudent(UUID accountId, UUID studentId) {
        Account account = accountRepository.findById(accountId).orElseThrow(() -> new ResourceNotFoundException("Account does not exist"));
        Student student = studentRepository.findById(studentId).orElseThrow(() -> new ResourceNotFoundException("Student already exists"));
        account.getStudents().add(student);
        return accountRepository.save(account);
    }

    @Transactional
    public Account linkAccountToEducator(UUID accountId, UUID educatorId) {
        Account account = accountRepository.findById(accountId).orElseThrow(() -> new ResourceNotFoundException("Account does not exist"));
        Educator educator = educatorRepository.findById(educatorId).orElseThrow(() -> new ResourceNotFoundException("Student already exists"));
        account.getEducators().add(educator);
        return accountRepository.save(account);
    }

    @Transactional
    public Account createOutletAdminAndLinkToAccount(UUID institutionId, UUID outletId, UUID accountId) {
        Account account = accountRepository.findById(accountId).orElseThrow(() -> new ResourceNotFoundException("account not found"));
        Outlet outlet = outletRepository.findById(outletId).orElseThrow(() -> new ResourceNotFoundException("outlet not found"));
        Institution institution = institutionRepository.findById(institutionId).orElseThrow(() -> new ResourceNotFoundException("institution not found"));
        UserResource userResource = usersResource.get(account.getUserId());
        // TODO: check that account is not already an outlet admin in this outlet
        OutletAdmin createdOutletAdmin = outletAdminRepository.save(OutletAdmin
                .builder()
                .tenantId(institutionId.toString())
                .account(account)
                .build());

        outlet.getOutletAdmins().add(createdOutletAdmin);
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
}
