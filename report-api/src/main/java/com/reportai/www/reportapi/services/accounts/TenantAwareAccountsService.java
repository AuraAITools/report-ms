package com.reportai.www.reportapi.services.accounts;

import com.reportai.www.reportapi.entities.Account;
import com.reportai.www.reportapi.entities.Student;
import com.reportai.www.reportapi.exceptions.lib.ResourceAlreadyExistsException;
import com.reportai.www.reportapi.exceptions.lib.ResourceNotFoundException;
import com.reportai.www.reportapi.repositories.AccountRepository;
import com.reportai.www.reportapi.repositories.InstitutionRepository;
import com.reportai.www.reportapi.repositories.StudentRepository;
import com.reportai.www.reportapi.services.accounts.creationstrategies.ClientTenantAwareAccountCreationStrategy;
import com.reportai.www.reportapi.services.accounts.creationstrategies.EducatorTenantAwareAccountCreationStrategy;
import com.reportai.www.reportapi.services.accounts.creationstrategies.InstitutionAdminTenantAwareAccountCreationStrategy;
import com.reportai.www.reportapi.services.accounts.creationstrategies.OutletAdminTenantAwareAccountCreationStrategy;
import com.reportai.www.reportapi.services.accounts.creationstrategies.TenantAwareAccountCreationContext;
import com.reportai.www.reportapi.services.accounts.creationstrategies.TenantAwareAccountCreationStrategy;
import java.util.UUID;
import org.keycloak.admin.client.resource.ClientResource;
import org.keycloak.admin.client.resource.RealmResource;
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

    public TenantAwareAccountsService(RealmResource realmResource, InstitutionRepository institutionRepository, AccountRepository accountRepository, ClientResource clientResource, StudentRepository studentRepository) {
        this.realmResource = realmResource;
        this.institutionRepository = institutionRepository;
        this.accountRepository = accountRepository;
        this.clientResource = clientResource;
        this.studentRepository = studentRepository;
    }

    // can create Client account, educator account, Institution_admin account and outlet_admin account
    // CLIENT: access to report mobile
    // EDUCATOR: access to report mobile
    // INSTITUTION_ADMIN: access to ops website
    // This method creates a logical multitenant aware account
    // OUTLET_ADMIN: access to ops website
    public Account createTenantAwareAccount(Account tenantAwareAccount, TenantAwareAccountCreationContext.AccountType accountType, UUID tenantId) {
        // create tenantAwareAccount according to the strategy
        return tenantAwareAccountCreationContext.createTenantAwareAccount(chooseStrategy(accountType, tenantAwareAccount, tenantId));
    }


    /**
     * helper method to choose account creation strategy
     *
     * @param accountType
     * @param tenantAwareAccount
     * @param institutionId
     * @return
     */
    private TenantAwareAccountCreationStrategy chooseStrategy(TenantAwareAccountCreationContext.AccountType accountType, Account tenantAwareAccount, UUID institutionId) {
        return switch (accountType) {
            case CLIENT ->
                    new ClientTenantAwareAccountCreationStrategy(institutionId, tenantAwareAccount, clientResource, realmResource, accountRepository, institutionRepository);
            case EDUCATOR ->
                    new EducatorTenantAwareAccountCreationStrategy(institutionId, tenantAwareAccount, clientResource, realmResource, accountRepository, institutionRepository);
            case INSTITUTION_ADMIN ->
                    new InstitutionAdminTenantAwareAccountCreationStrategy(institutionId, tenantAwareAccount, clientResource, realmResource, accountRepository, institutionRepository);
            case OUTLET_ADMIN ->
                    new OutletAdminTenantAwareAccountCreationStrategy(institutionId, tenantAwareAccount, clientResource, realmResource, accountRepository, institutionRepository);
            default ->
                    throw new UnsupportedOperationException(String.format("accountType of %s is not allowed", accountType));
        };
    }

    public Account findById(UUID id) {
        return accountRepository.findById(id).orElseThrow(() -> new ResourceAlreadyExistsException("Account already exists"));
    }

    public Account linkAccountToStudent(UUID accountId, UUID studentId) {
        Account account = accountRepository.findById(accountId).orElseThrow(() -> new ResourceNotFoundException("Account does not exist"));
        Student student = studentRepository.findById(studentId).orElseThrow(() -> new ResourceNotFoundException("Student already exists"));
        account.getStudents().add(student);
        return accountRepository.save(account);
    }
}
