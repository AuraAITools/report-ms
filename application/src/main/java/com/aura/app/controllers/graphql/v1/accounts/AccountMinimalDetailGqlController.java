package com.aura.app.controllers.graphql.v1.accounts;

import com.aura.app.codegen.types.AccountMinimalDetail;
import com.aura.core.auth.User;
import com.aura.core.entities.accounts.Account;
import com.aura.details.services.AccountService;
import com.aura.details.services.AssetUrlService;
import java.time.Duration;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.graphql.data.method.annotation.BatchMapping;
import org.springframework.graphql.data.method.annotation.ContextValue;
import org.springframework.stereotype.Controller;

/**
 * Resolves the deferred fields of {@link AccountMinimalDetail}. Source mappers produce
 * stubs with only {@code userId} populated; this controller fills in {@code accountId},
 * {@code name}, and {@code url} by batch-loading the matching {@link Account}s for the
 * current tenant.
 *
 * <p>System-scoped queries (e.g. getUserInstitutions) run before any tenant is selected,
 * so {@code user.getCurrentTenantAccessed()} is null and no Account lookup is possible.
 * In that case the three fields are populated with system placeholders
 * ({@link #SYSTEM_ACCOUNT_ID} / {@link #SYSTEM_NAME} / empty url) so clients always
 * receive a non-null AccountMinimalDetail object.
 *
 * <p>No {@code @PreAuthorize} on these methods: AccountMinimalDetail is only ever a
 * subfield of an already-authorized parent response (Auditable.createdBy /
 * Auditable.updatedBy). Permission checks run at the parent query.
 */
@Slf4j
@Controller
@RequiredArgsConstructor
public class AccountMinimalDetailGqlController {

    private static final Duration PROFILE_IMAGE_URL_TTL = Duration.ofMinutes(15);
    private static final String SYSTEM_ACCOUNT_ID = "00000000-0000-0000-0000-000000000000";
    private static final String SYSTEM_NAME = "system";
    private static final String SYSTEM_URL = "";

    private final AccountService accountService;
    private final AssetUrlService assetUrlService;

    @BatchMapping(typeName = "AccountMinimalDetail", field = "accountId")
    public Map<AccountMinimalDetail, String> accountId(
            List<AccountMinimalDetail> stubs,
            @ContextValue(name = "user") User user) {
        if (stubs.isEmpty()) {
            return Map.of();
        }
        if (user.getCurrentTenantAccessed() == null) {
            return constantForEveryStub(stubs, SYSTEM_ACCOUNT_ID);
        }
        Map<String, Account> byUserId = loadAccountsByUserId(stubs, user);
        return stubs.stream()
                .filter(s -> byUserId.containsKey(s.getUserId()))
                .collect(Collectors.toMap(
                        s -> s,
                        s -> byUserId.get(s.getUserId()).getId(),
                        (existing, replacement) -> existing,
                        LinkedHashMap::new));
    }

    @BatchMapping(typeName = "AccountMinimalDetail", field = "name")
    public Map<AccountMinimalDetail, String> name(
            List<AccountMinimalDetail> stubs,
            @ContextValue(name = "user") User user) {
        if (stubs.isEmpty()) {
            return Map.of();
        }
        if (user.getCurrentTenantAccessed() == null) {
            return constantForEveryStub(stubs, SYSTEM_NAME);
        }
        Map<String, Account> byUserId = loadAccountsByUserId(stubs, user);
        return stubs.stream()
                .filter(s -> byUserId.containsKey(s.getUserId()))
                .collect(Collectors.toMap(
                        s -> s,
                        s -> {
                            Account a = byUserId.get(s.getUserId());
                            return a.getFirstName() + " " + a.getLastName();
                        },
                        (existing, replacement) -> existing,
                        LinkedHashMap::new));
    }

    @BatchMapping(typeName = "AccountMinimalDetail", field = "url")
    public Map<AccountMinimalDetail, String> url(
            List<AccountMinimalDetail> stubs,
            @ContextValue(name = "user") User user) {
        if (stubs.isEmpty()) {
            return Map.of();
        }
        if (user.getCurrentTenantAccessed() == null) {
            return constantForEveryStub(stubs, SYSTEM_URL);
        }
        Map<String, Account> byUserId = loadAccountsByUserId(stubs, user);
        return stubs.stream()
                .filter(s -> {
                    Account a = byUserId.get(s.getUserId());
                    return a != null && a.getProfileImageKey() != null;
                })
                .collect(Collectors.toMap(
                        s -> s,
                        s -> assetUrlService.presignGet(
                                byUserId.get(s.getUserId()).getProfileImageKey(),
                                PROFILE_IMAGE_URL_TTL),
                        (existing, replacement) -> existing,
                        LinkedHashMap::new));
    }

    private Map<String, Account> loadAccountsByUserId(List<AccountMinimalDetail> stubs, User user) {
        List<String> userIds = stubs.stream()
                .map(AccountMinimalDetail::getUserId)
                .filter(Objects::nonNull)
                .distinct()
                .toList();
        return accountService.findAllByUserIdsAndTenantId(userIds, user.getCurrentTenantAccessed());
    }

    private static Map<AccountMinimalDetail, String> constantForEveryStub(
            List<AccountMinimalDetail> stubs, String value) {
        return stubs.stream().collect(Collectors.toMap(
                s -> s,
                s -> value,
                (existing, replacement) -> existing,
                LinkedHashMap::new));
    }
}
