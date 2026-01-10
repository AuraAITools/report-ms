package com.aura.details.services;

import com.aura.core.clients.IdentityService;
import com.aura.core.entities.accounts.Account;
import com.aura.core.usecases.accounts.AssignInstitutionAdminRoleUseCase;
import com.aura.core.usecases.accounts.AssignParentRoleUseCase;
import com.aura.core.usecases.accounts.AssignRoleUseCase;
import com.aura.core.usecases.accounts.CreateAccountUseCase;
import com.aura.core.usecases.accounts.CreateParentAccountUseCase;
import jakarta.transaction.Transactional;

public class AccountService {
    private final CreateAccountUseCase createAccountUseCase;
    private final CreateParentAccountUseCase createParentAccountUseCase;
    private final IdentityService identityService;

    public AccountService(CreateAccountUseCase createAccountUseCase, CreateParentAccountUseCase createParentAccountUseCase, IdentityService identityService) {
        this.createAccountUseCase = createAccountUseCase;
        this.createParentAccountUseCase = createParentAccountUseCase;
        this.identityService = identityService;
    }

    @Transactional
    public Account createAccountWithInstitutionAdminRole(Account account) {
        Account createdAccount = createAccountUseCase.createAccount(account);
        AssignRoleUseCase assignRoleUseCase = new AssignInstitutionAdminRoleUseCase(identityService, createdAccount.getTenantId(), createdAccount.getUserId());
        assignRoleUseCase.assignRole();
        return createdAccount;
    }

    @Transactional
    public Account createParentAccount(Account account) {
        Account createdAccount = createParentAccountUseCase.createParentAccount(account);
        AssignRoleUseCase assignRoleUseCase = new AssignParentRoleUseCase(identityService, createdAccount.getTenantId(), createdAccount.getUserId());
        assignRoleUseCase.assignRole();
        return createdAccount;
    }
}
