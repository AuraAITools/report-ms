package com.aura.core.usecases.accounts;

import com.aura.core.clients.IdentityCredential;
import com.aura.core.clients.IdentityService;
import com.aura.core.clients.UserIdentity;
import com.aura.core.entities.accounts.Account;
import com.aura.core.repositories.AccountRepository;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CreateAccountUseCaseImpl implements CreateAccountUseCase {
    private final AccountRepository accountRepository;
    private final IdentityService identityService;

    public CreateAccountUseCaseImpl(AccountRepository accountRepository, IdentityService identityService) {
        this.accountRepository = accountRepository;
        this.identityService = identityService;
    }

    public static UserIdentity convert(Account account, Map<String, List<String>> attributes) {
        // default credentials
        IdentityCredential credentialRepresentation = IdentityCredential
                .builder()
                .type("password")
                .value("password")
                .isTemporary(true)
                .build();
        return UserIdentity
                .builder()
                .email(account.getEmail())
                .firstName(account.getFirstName())
                .lastName(account.getLastName())
                .attributes(attributes)
                .enabled(true)
                .emailVerified(false)
                .requiredActions(List.of("VERIFY_EMAIL", "UPDATE_PASSWORD"))
                .credentials(List.of(credentialRepresentation))
                .build();
    }

    @Override
    public Account createTenantAwareAccount(Account account) {
        List<UserIdentity> matchedUsers = identityService.findByEmail(account.getEmail());

        if (matchedUsers.size() > 1) {
            throw new RuntimeException("There are more than one user with the same email. cannot create another");
        }

        if (matchedUsers.isEmpty()) {
            Map<String, List<String>> attributes = new HashMap<>();
            attributes.put("tenant-id", List.of(account.getTenantId()));
            UserIdentity userIdentityToBeCreated = convert(account, attributes);
            UserIdentity createdUserIdentity = identityService.createUserIdentity(userIdentityToBeCreated, account.getTenantId());
            account.setUserId(createdUserIdentity.getId());
            return accountRepository.save(account);
        }

        UserIdentity userIdentity = matchedUsers.getFirst();
        identityService.addTenantToUser(userIdentity.getId(), account.getTenantId());
        account.setUserId(userIdentity.getId());
        return accountRepository.save(account);
    }
}
