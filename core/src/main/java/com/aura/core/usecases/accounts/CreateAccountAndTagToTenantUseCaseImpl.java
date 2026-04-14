package com.aura.core.usecases.accounts;

import com.aura.core.clients.IdentityCredential;
import com.aura.core.clients.IdentityService;
import com.aura.core.clients.UserIdentity;
import com.aura.core.entities.accounts.Account;
import com.aura.core.repositories.AccountRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;

/**
 * Creates account & tags it to a tenant
 */
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
        UserIdentity userIdentity = mapToUserIdentity(account);
        UserIdentity createdUserIdentity = identityService.createUserIdentityAndTagToTenant(userIdentity, account.getTenantId());
        account.setUserId(createdUserIdentity.getId());
        return accountRepository.save(account);
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
