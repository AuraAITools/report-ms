package com.reportai.www.reportapi.services.accounts.creationstrategies;

import com.reportai.www.reportapi.entities.Account;

public class TenantAwareAccountCreationContext {
    public enum AccountType {
        BLANK,
        STUDENT_CLIENT,
        EDUCATOR_CLIENT,
        INSTITUTION_ADMIN,
        OUTLET_ADMIN
    }

    public Account createTenantAwareAccount(TenantAwareAccountCreationStrategy strategy) {
        return strategy.createTenantAwareAccount();
    }
}
