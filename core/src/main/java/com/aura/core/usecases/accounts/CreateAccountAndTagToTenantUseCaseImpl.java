package com.aura.core.usecases.accounts;

import com.aura.core.clients.IdentityCredential;
import com.aura.core.clients.IdentityService;
import com.aura.core.clients.UserIdentity;
import com.aura.core.entities.accounts.Account;
import com.aura.core.repositories.AccountRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Creates account & tags it to a tenant
 */
@Slf4j
@RequiredArgsConstructor
public class CreateAccountAndTagToTenantUseCaseImpl implements CreateAccountUseCase {
    private static final String CREDENTIAL_TYPE = "password";
    private static final String DEFAULT_PASSWORD_VALUE = "password";
    private static final List<String> DEFAULT_REQUIRED_ACTIONS = List.of("VERIFY_EMAIL", "UPDATE_PASSWORD");
    private final AccountRepository accountRepository;
    private final IdentityService identityService;

    /**
     * throws UserAccountNotCreatedException user cannot be created
     * throws UserIdentityAlreadyExistsException if user already exists and could not be created
     *
     * @param account
     * @return
     */
    @Override
    public Account createAccount(Account account) {
        account.setState(Account.STATE.VERIFYING);
        List<Account> existingAccounts = accountRepository.findAllByEmail(account.getEmail());
        boolean isAccountInTenant = existingAccounts.stream().anyMatch(acc -> acc.getTenantId().equals(account.getTenantId()));
        // account already exists in tenant
        if (isAccountInTenant) {
            throw new RuntimeException(String.format("user account email=%s userId=%s already exists for tenantId=%s", account.getEmail(), existingAccounts.getFirst().getUserId(), account.getTenantId()));
        }

        // account exists in separate tenant, don't need to recreate idp account
        if (!existingAccounts.isEmpty()) {
            String userId = existingAccounts.getFirst().getUserId();
            account.setUserId(userId);
            Account acc = accountRepository.saveAndFlush(account);
            identityService.tagTenantToUser(userId, account.getTenantId());
            return acc;
        }

        // account doesn't exist at all
        UserIdentity userIdentity = mapToUserIdentity(account);
        UserIdentity createdUserIdentity = identityService.createUserIdentityAndTagToTenant(userIdentity, account.getTenantId());
        account.setUserId(createdUserIdentity.getId());
        try {
            return accountRepository.saveAndFlush(account);
        } catch (Exception e) { // race condition, unique key constraint violation
            log.info("Encountered race condition while creating idp account for userEmail={} deleting idp account userId={}", account.getEmail(), createdUserIdentity.getId());
            identityService.deleteUser(createdUserIdentity.getId());
            throw new RuntimeException("Account Creation failed");
        }
    }

    private static UserIdentity mapToUserIdentity(Account account) {
        // default credentials
        IdentityCredential credentialRepresentation = IdentityCredential
                .builder()
                .type(CREDENTIAL_TYPE)
                .value(DEFAULT_PASSWORD_VALUE)
                .isTemporary(true)
                .build();
        return UserIdentity
                .builder()
                .email(account.getEmail())
                .firstName(account.getFirstName())
                .lastName(account.getLastName())
                .enabled(true)
                .emailVerified(false)
                .requiredActions(DEFAULT_REQUIRED_ACTIONS)
                .credentials(List.of(credentialRepresentation))
                .build();
    }
}
