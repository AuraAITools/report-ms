package com.aura.details.services;

import com.aura.core.entities.accounts.Account;
import com.aura.core.entities.outlets.Outlet;
import com.aura.core.events.OutletAdminAssignedEvent;
import com.aura.core.repositories.AccountRepository;
import com.aura.core.repositories.OutletRepository;
import com.aura.core.usecases.authorization.GrantUserGroupRoleUseCase;
import com.aura.core.usecases.authorization.LookupPermissionsUseCase;
import com.aura.core.usecases.authorization.ResourcePermission;
import com.aura.core.usecases.authorization.RevokeUserGroupRoleUseCase;
import java.util.List;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@RequiredArgsConstructor
public class AuthorizationService {

    private static final String OUTLET_USER_GROUP = "outlet";
    private static final Set<String> OUTLET_ADMIN_ROLES = Set.of("admin", "group_admin");

    private final LookupPermissionsUseCase lookupPermissionsUseCase;
    private final GrantUserGroupRoleUseCase grantUserGroupRoleUseCase;
    private final RevokeUserGroupRoleUseCase revokeUserGroupRoleUseCase;
    private final OutletRepository outletRepository;
    private final AccountRepository accountRepository;
    private final ApplicationEventPublisher applicationEventPublisher;

    public List<ResourcePermission> lookupPermissions(String userId) {
        return lookupPermissionsUseCase.lookupPermissions(userId);
    }

    @Transactional
    public void grantUserGroupRole(String userGroupType, String userGroupId, String role, String targetUserId) {
        grantUserGroupRoleUseCase.grant(userGroupType, userGroupId, role, targetUserId);
        if (OUTLET_USER_GROUP.equals(userGroupType) && OUTLET_ADMIN_ROLES.contains(role)) {
            publishOutletAdminAssigned(userGroupId, targetUserId);
        }
    }

    @Transactional
    public void revokeUserGroupRole(String userGroupType, String userGroupId, String role, String targetUserId) {
        revokeUserGroupRoleUseCase.revoke(userGroupType, userGroupId, role, targetUserId);
    }

    private void publishOutletAdminAssigned(String outletId, String targetUserId) {
        try {
            Outlet outlet = outletRepository.findById(outletId);
            if (outlet == null) {
                log.warn("Skipping OutletAdminAssignedEvent — outlet {} not found", outletId);
                return;
            }
            Account account = accountRepository.findByUserIdAndTenantId(targetUserId, outlet.getTenantId());
            if (account == null) {
                log.warn("Skipping OutletAdminAssignedEvent — no account for userId={} in tenant={}",
                        targetUserId, outlet.getTenantId());
                return;
            }
            applicationEventPublisher.publishEvent(OutletAdminAssignedEvent.builder()
                    .outletId(outlet.getId())
                    .outletName(outlet.getName())
                    .institutionName(outlet.getTenantId())
                    .userId(account.getUserId())
                    .userEmail(account.getEmail())
                    .userFirstName(account.getFirstName())
                    .tenantId(outlet.getTenantId())
                    .build());
        } catch (RuntimeException e) {
            log.error("Failed to publish OutletAdminAssignedEvent for outlet={} userId={}",
                    outletId, targetUserId, e);
        }
    }
}
