package com.aura.app.controllers.graphql.v1.accounts;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

import com.aura.app.codegen.types.AccountMinimalDetail;
import com.aura.core.auth.User;
import com.aura.core.entities.accounts.Account;
import com.aura.details.services.AccountService;
import com.aura.details.services.AssetUrlService;
import java.time.Duration;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

@DisplayName("AccountMinimalDetailGqlController")
class AccountMinimalDetailGqlControllerTest {

    private AccountService accountService;
    private AssetUrlService assetUrlService;
    private AccountMinimalDetailGqlController controller;

    private static final String TENANT = "tenant-1";

    @BeforeEach
    void setUp() {
        accountService = Mockito.mock(AccountService.class);
        assetUrlService = Mockito.mock(AssetUrlService.class);
        controller = new AccountMinimalDetailGqlController(accountService, assetUrlService);
    }

    private User user() {
        User u = Mockito.mock(User.class);
        when(u.getCurrentTenantAccessed()).thenReturn(TENANT);
        return u;
    }

    private AccountMinimalDetail stub(String userId) {
        return AccountMinimalDetail.newBuilder().userId(userId).build();
    }

    private Account acc(String id, String userId, String first, String last, String imageKey) {
        return Account.builder().id(id).userId(userId).firstName(first).lastName(last)
                .profileImageKey(imageKey).tenantId(TENANT).build();
    }

    @Test
    @DisplayName("Should batch-resolve accountId for each stub from the matching account in tenant")
    void accountId_resolvesFromAccountLookup() {
        AccountMinimalDetail s1 = stub("user-a");
        AccountMinimalDetail s2 = stub("user-b");
        Account a1 = acc("acc-1", "user-a", "Alice", "Anderson", null);
        Account a2 = acc("acc-2", "user-b", "Bob", "Brown", null);
        when(accountService.findAllByUserIdsAndTenantId(List.of("user-a", "user-b"), TENANT))
                .thenReturn(Map.of("user-a", a1, "user-b", a2));

        Map<AccountMinimalDetail, String> result = controller.accountId(List.of(s1, s2), user());

        assertThat(result).containsEntry(s1, "acc-1").containsEntry(s2, "acc-2");
    }

    @Test
    @DisplayName("Should fall back to the system placeholder accountId for stubs whose userId has no matching account in the tenant")
    void accountId_fallsBackToPlaceholderForUnmatchedUsers() {
        AccountMinimalDetail s1 = stub("user-a");
        AccountMinimalDetail s2 = stub("user-missing");
        Account a1 = acc("acc-1", "user-a", "Alice", "Anderson", null);
        when(accountService.findAllByUserIdsAndTenantId(List.of("user-a", "user-missing"), TENANT))
                .thenReturn(Map.of("user-a", a1));

        Map<AccountMinimalDetail, String> result = controller.accountId(List.of(s1, s2), user());

        assertThat(result)
                .containsEntry(s1, "acc-1")
                .containsEntry(s2, "00000000-0000-0000-0000-000000000000");
    }

    @Test
    @DisplayName("Should fall back to the system placeholder name for stubs whose userId has no matching account in the tenant")
    void name_fallsBackToPlaceholderForUnmatchedUsers() {
        AccountMinimalDetail s1 = stub("user-a");
        AccountMinimalDetail s2 = stub("user-missing");
        Account a1 = acc("acc-1", "user-a", "Alice", "Anderson", null);
        when(accountService.findAllByUserIdsAndTenantId(List.of("user-a", "user-missing"), TENANT))
                .thenReturn(Map.of("user-a", a1));

        Map<AccountMinimalDetail, String> result = controller.name(List.of(s1, s2), user());

        assertThat(result)
                .containsEntry(s1, "Alice Anderson")
                .containsEntry(s2, "system");
    }

    @Test
    @DisplayName("Should fall back to the empty placeholder url for stubs whose userId has no matching account in the tenant")
    void url_fallsBackToPlaceholderForUnmatchedUsers() {
        AccountMinimalDetail s1 = stub("user-missing");
        when(accountService.findAllByUserIdsAndTenantId(List.of("user-missing"), TENANT))
                .thenReturn(Map.of());

        Map<AccountMinimalDetail, String> result = controller.url(List.of(s1), user());

        assertThat(result).containsEntry(s1, "");
    }

    @Test
    @DisplayName("Should resolve name as firstName + ' ' + lastName for each matching account")
    void name_concatenatesFirstAndLast() {
        AccountMinimalDetail s1 = stub("user-a");
        Account a1 = acc("acc-1", "user-a", "Alice", "Anderson", null);
        when(accountService.findAllByUserIdsAndTenantId(List.of("user-a"), TENANT))
                .thenReturn(Map.of("user-a", a1));

        Map<AccountMinimalDetail, String> result = controller.name(List.of(s1), user());

        assertThat(result).containsEntry(s1, "Alice Anderson");
    }

    @Test
    @DisplayName("Should resolve url by presigning the profile image key for accounts that have one")
    void url_presignsProfileImageKey() {
        AccountMinimalDetail s1 = stub("user-a");
        AccountMinimalDetail s2 = stub("user-b");
        Account a1 = acc("acc-1", "user-a", "Alice", "Anderson", "institutions/T/accounts/acc-1/profile.png");
        Account a2 = acc("acc-2", "user-b", "Bob", "Brown", null);
        when(accountService.findAllByUserIdsAndTenantId(List.of("user-a", "user-b"), TENANT))
                .thenReturn(Map.of("user-a", a1, "user-b", a2));
        when(assetUrlService.presignGet(eq("institutions/T/accounts/acc-1/profile.png"), any(Duration.class)))
                .thenReturn("https://s3/signed-url-for-a");

        Map<AccountMinimalDetail, String> result = controller.url(List.of(s1, s2), user());

        assertThat(result).containsOnlyKeys(s1);
        assertThat(result).containsEntry(s1, "https://s3/signed-url-for-a");
    }

    @Test
    @DisplayName("Should make no DB or S3 calls for an empty batch")
    void emptyBatch_noLookups() {
        Map<AccountMinimalDetail, String> r1 = controller.accountId(List.of(), user());
        Map<AccountMinimalDetail, String> r2 = controller.name(List.of(), user());
        Map<AccountMinimalDetail, String> r3 = controller.url(List.of(), user());

        assertThat(r1).isEmpty();
        assertThat(r2).isEmpty();
        assertThat(r3).isEmpty();
        Mockito.verifyNoInteractions(accountService);
        Mockito.verifyNoInteractions(assetUrlService);
    }

    @Test
    @DisplayName("Should handle duplicate value-equal stubs in the batch (multiple rows by the same user)")
    void duplicateValueEqualStubs_doesNotThrow() {
        AccountMinimalDetail s1 = stub("user-a");
        AccountMinimalDetail s2 = stub("user-a"); // value-equal to s1
        Account a = acc("acc-1", "user-a", "Alice", "Anderson", null);
        when(accountService.findAllByUserIdsAndTenantId(java.util.List.of("user-a"), TENANT))
                .thenReturn(java.util.Map.of("user-a", a));

        Map<AccountMinimalDetail, String> accountIds = controller.accountId(java.util.List.of(s1, s2), user());
        Map<AccountMinimalDetail, String> names = controller.name(java.util.List.of(s1, s2), user());

        // s1 and s2 are value-equal, so the map will collapse to one entry. That's fine.
        assertThat(accountIds).containsEntry(s1, "acc-1");
        assertThat(names).containsEntry(s1, "Alice Anderson");
    }

    @Test
    @DisplayName("Should fill every stub with system placeholders when the user has no current tenant")
    void noCurrentTenant_returnsSystemPlaceholdersForEveryStub() {
        User userWithoutTenant = Mockito.mock(User.class);
        when(userWithoutTenant.getCurrentTenantAccessed()).thenReturn(null);
        AccountMinimalDetail s1 = stub("user-a");
        AccountMinimalDetail s2 = stub("user-b");

        Map<AccountMinimalDetail, String> accountIds = controller.accountId(List.of(s1, s2), userWithoutTenant);
        Map<AccountMinimalDetail, String> names = controller.name(List.of(s1, s2), userWithoutTenant);
        Map<AccountMinimalDetail, String> urls = controller.url(List.of(s1, s2), userWithoutTenant);

        assertThat(accountIds)
                .containsEntry(s1, "00000000-0000-0000-0000-000000000000")
                .containsEntry(s2, "00000000-0000-0000-0000-000000000000");
        assertThat(names).containsEntry(s1, "system").containsEntry(s2, "system");
        assertThat(urls).containsEntry(s1, "").containsEntry(s2, "");
        Mockito.verifyNoInteractions(accountService);
        Mockito.verifyNoInteractions(assetUrlService);
    }
}
