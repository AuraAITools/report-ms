package com.reportai.www.reportapi.services.accounts.creationstrategies;

import com.reportai.www.reportapi.entities.Account;

public class AccountCreationContext {
    public enum AccountType {
        CLIENT,
        EDUCATOR,
        INSTITUTION_ADMIN,
        OUTLET_ADMIN
    }

    public Account createAccount(AccountCreationStrategy strategy) {
        return strategy.createAccount();
    }
}
