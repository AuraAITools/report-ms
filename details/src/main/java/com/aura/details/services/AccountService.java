package com.aura.details.services;

import com.aura.core.clients.IdentityService;
import com.aura.core.entities.accounts.Account;
import com.aura.core.usecases.accounts.AssignInstitutionAdminRoleUseCase;
import com.aura.core.usecases.accounts.AssignRoleUseCase;
import com.aura.core.usecases.accounts.CreateAccountUseCase;
import jakarta.transaction.Transactional;

public class AccountService {
    private final CreateAccountUseCase createAccountUseCase;
    private final IdentityService identityService;

    public AccountService(CreateAccountUseCase createAccountUseCase, IdentityService identityService) {
        this.createAccountUseCase = createAccountUseCase;
        this.identityService = identityService;
    }

    @Transactional
    public Account createAccount(Account account) {
        return createAccountUseCase.createTenantAwareAccount(account);
    }

    public void assignInstitutionAdminRole(String tenantId, String userId) {
        AssignRoleUseCase assignRoleUseCase = new AssignInstitutionAdminRoleUseCase(identityService, tenantId, userId);
        assignRoleUseCase.assignRole();
    }
}
