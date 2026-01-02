package com.aura.app.controllers.graphql.v1.accounts;

import com.aura.app.codegen.types.AccountResponse;
import com.aura.app.codegen.types.CreateAccountInput;
import com.aura.app.mappers.AccountMapper;
import com.aura.core.auth.User;
import com.aura.core.entities.accounts.Account;
import com.aura.details.services.AccountService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.ContextValue;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.stereotype.Controller;


import static com.aura.app.codegen.DgsConstants.MUTATION.CREATEACCOUNT_INPUT_ARGUMENT.CreateAccountInput;
import static com.aura.app.codegen.DgsConstants.MUTATION.CreateAccount;
import static com.aura.app.codegen.DgsConstants.MUTATION.CreateInstitutionAdminAccount;
import static com.aura.core.auth.RoleTypes.AURA_ADMIN;
import static com.aura.core.auth.RoleTypes.INSTITUTION_ADMIN;
import static com.aura.core.utils.AuthUtils.hasAnyRoles;
import static com.aura.core.utils.AuthUtils.hasAnyTechnicalUserRole;
import static com.aura.core.utils.AuthUtils.isAggregateQuery;

@Slf4j
@Controller
public class AccountsGqlController {

    private final AccountService accountService;

    public AccountsGqlController(AccountService accountService) {
        this.accountService = accountService;
    }

    @MutationMapping(name = CreateAccount)
    public AccountResponse createAccount(@Argument(value = CreateAccountInput) CreateAccountInput createAccountInput, @ContextValue(name = "user") User user) {
        if (isAggregateQuery(user))
            throw new UnsupportedOperationException("aggregate operation unsupported for this API");
        if (!hasAnyRoles(user, INSTITUTION_ADMIN))
            throw new RuntimeException("unauthorized to create account");
        Account account = AccountMapper.INSTANCE.toDomainEntity(createAccountInput, user.getCurrentTenantAccessed());
        Account createdAccount = accountService.createAccount(account);
        return AccountMapper.INSTANCE.toDto(createdAccount);
    }

    @MutationMapping(name = CreateInstitutionAdminAccount)
    public AccountResponse createAccountWithInstitutionAdminRole(@Argument(value = CreateAccountInput) CreateAccountInput createAccountInput, @ContextValue(name = "user") User user) {
        if (isAggregateQuery(user))
            throw new RuntimeException("aggregate operation unsupported for this API");
        if (!hasAnyTechnicalUserRole(user, AURA_ADMIN) && !hasAnyRoles(user, INSTITUTION_ADMIN))
            throw new RuntimeException("unauthorized access admin apis");
        Account account = AccountMapper.INSTANCE.toDomainEntity(createAccountInput, user.getCurrentTenantAccessed());
        Account createdAccount = accountService.createAccount(account);
        accountService.assignInstitutionAdminRole(user.getCurrentTenantAccessed(), createdAccount.getUserId());
        return AccountMapper.INSTANCE.toDto(createdAccount);
    }

}
