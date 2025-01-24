package com.reportai.www.reportapi.services.accounts;

import com.reportai.www.reportapi.entities.Account;
import com.reportai.www.reportapi.repositories.AccountRepository;
import com.reportai.www.reportapi.repositories.InstitutionRepository;
import com.reportai.www.reportapi.services.accounts.creationstrategies.AccountCreationContext;
import com.reportai.www.reportapi.services.accounts.creationstrategies.AccountCreationStrategy;
import com.reportai.www.reportapi.services.accounts.creationstrategies.ClientAccountCreationStrategy;
import com.reportai.www.reportapi.services.accounts.creationstrategies.EducatorAccountCreationStrategy;
import com.reportai.www.reportapi.services.accounts.creationstrategies.InstitutionAdminAccountCreationStrategy;
import com.reportai.www.reportapi.services.accounts.creationstrategies.OutletAdminAccountCreationStrategy;
import java.util.UUID;
import org.keycloak.admin.client.resource.ClientResource;
import org.keycloak.admin.client.resource.RealmResource;
import org.springframework.stereotype.Service;

@Service
public class AccountsService {
    private final RealmResource realmResource;
    private final InstitutionRepository institutionRepository;
    private final AccountRepository accountRepository;
    private final AccountCreationContext accountCreationContext = new AccountCreationContext();
    private final ClientResource clientResource;

    public AccountsService(InstitutionRepository institutionRepository, AccountRepository accountRepository, RealmResource realmResource, ClientResource clientResource) {
        this.realmResource = realmResource;
        this.clientResource = clientResource;
        this.institutionRepository = institutionRepository;
        this.accountRepository = accountRepository;
    }

    // can create Client account, educator account, Institution_admin account and outlet_admin account
    // CLIENT: access to report mobile
    // EDUCATOR: access to report mobile
    // INSTITUTION_ADMIN: access to ops website
    // This method creates a logical multitenant aware account
    // OUTLET_ADMIN: access to ops website
    public Account createUserAccountInInstitution(Account account, AccountCreationContext.AccountType accountType, UUID institutionId) {
        // create account according to the strategy
        return accountCreationContext.createAccount(chooseStrategy(accountType, account, institutionId));
    }


    /**
     * helper method to choose account creation strategy
     *
     * @param accountType
     * @param account
     * @param institutionId
     * @return
     */
    private AccountCreationStrategy chooseStrategy(AccountCreationContext.AccountType accountType, Account account, UUID institutionId) {
        return switch (accountType) {
            case CLIENT ->
                    new ClientAccountCreationStrategy(institutionId, account, clientResource, realmResource, accountRepository, institutionRepository);
            case EDUCATOR ->
                    new EducatorAccountCreationStrategy(institutionId, account, clientResource, realmResource, accountRepository, institutionRepository);
            case INSTITUTION_ADMIN ->
                    new InstitutionAdminAccountCreationStrategy(institutionId, account, clientResource, realmResource, accountRepository, institutionRepository);
            case OUTLET_ADMIN ->
                    new OutletAdminAccountCreationStrategy(institutionId, account, clientResource, realmResource, accountRepository, institutionRepository);
            default ->
                    throw new UnsupportedOperationException(String.format("accountType of %s is not allowed", accountType));
        };
    }

}
