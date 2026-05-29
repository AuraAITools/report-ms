package com.aura.core.clients;

import java.util.List;
import org.jspecify.annotations.Nullable;

public interface IdentityService {

    List<UserIdentity> findByEmail(String email);

    UserIdentity createUserIdentityAndTagToTenant(UserIdentity userIdentity, String tenantId);

    void tagTenantToUser(String userId, String tenantId);

    void untagTenantFromUser(String userId, String tenantId);

    void deleteUser(String userId);

    void assignRolesToUser(String userId, String... roles);

    /**
     * Triggers the identity provider to email the user a one-time link to
     * complete the listed required actions (for example VERIFY_EMAIL,
     * UPDATE_PASSWORD). After completion, the provider redirects the browser
     * to {@code redirectUri} if non-null; otherwise no redirect is attached
     * (useful when no Valid Redirect URI is registered on the IDP client).
     * Implementations must NOT throw on transport or SMTP failures — log and
     * swallow so the caller's domain operation is not rolled back.
     */
    void sendActionsEmail(String userId, List<String> actions, @Nullable String redirectUri);

    /**
     * Returns whether the identity provider account for {@code userId} has
     * completed all of {@code requiredActions} (i.e. none of them remain
     * pending on the user). Keycloak's {@code enabled} flag is set at user
     * creation and is therefore not a reliable signal that the user finished
     * the verify-email / set-password flow — checking the pending action list
     * is. Implementations must NOT throw on lookup or transport failures — log
     * and return {@code false} so callers can fall back to existing state
     * rather than failing the request.
     */
    boolean hasCompletedRequiredActions(String userId, List<String> requiredActions);

}
