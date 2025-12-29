package com.aura.core.services.institutions.onboarding;

/**
 * Loads initial default data for tenant
 */
public interface TenantInitialDataLoader {
    void loadInitialDataForTenant(String tenantId);
}
