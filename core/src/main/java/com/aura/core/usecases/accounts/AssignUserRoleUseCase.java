package com.aura.core.usecases.accounts;

public interface AssignUserRoleUseCase {
    void assignRole(String email, String resourceType, String resourceId, String role);
}
