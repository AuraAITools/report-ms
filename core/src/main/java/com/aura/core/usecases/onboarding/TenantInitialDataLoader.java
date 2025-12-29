package com.aura.core.usecases.onboarding;

/**
 * Loads initial default data for tenant
 */
public interface TenantInitialDataLoader {
    void loadInitialDataForTenant(String tenantId);
}
