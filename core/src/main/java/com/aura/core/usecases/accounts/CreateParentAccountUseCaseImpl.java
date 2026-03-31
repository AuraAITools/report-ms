package com.aura.core.usecases.accounts;

import com.aura.core.clients.IdentityService;
import com.aura.core.clients.UserIdentity;
import com.aura.core.entities.accounts.Account;
import com.aura.core.exceptions.UnexpectedTechnicalException;
import com.aura.core.exceptions.UserIdentityAlreadyExistsException;
import com.aura.core.repositories.AccountRepository;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import lombok.RequiredArgsConstructor;


import static com.aura.core.clients.UserIdentityAttributeKeys.TENANT_ID_ATTR_KEY;

@RequiredArgsConstructor
public class CreateParentAccountUseCaseImpl implements CreateParentAccountUseCase {
    private final CreateAccountAndTagToTenantUseCaseImpl createAccountAndTagToTenantUseCase;
    private final AccountRepository accountRepository;
    private final IdentityService identityService;

    public Account createParentAccount(Account account) {
        try {
            Account createdAccount = createAccountAndTagToTenantUseCase.createAccount(account);
            AssignRoleUseCase assignRoleUseCase = new AssignParentRoleUseCase(identityService, createdAccount.getTenantId(), createdAccount.getUserId());
            assignRoleUseCase.assignRole();
            return createdAccount;
            // TODO: send welcome email for parent
        } catch (UserIdentityAlreadyExistsException _) {
            List<UserIdentity> foundUserIdentities = identityService.findByEmail(account.getEmail());
            if (foundUserIdentities.size() > 1)
                throw new UnexpectedTechnicalException(String.format("user with email %s has more than 1 userIdentities", account.getEmail()));
            if (foundUserIdentities.isEmpty())
                throw new UnexpectedTechnicalException(String.format("user with email %s has no userIdentities, it might have been recently deleted, try again", account.getEmail()));

            UserIdentity userIdentity = foundUserIdentities.getFirst();
            Map<String, List<String>> attributes = userIdentity.getAttributes();
            List<String> tenantIdsTaggedToUserIdentity = retrieveTenantIdsIfExistElseThrow(attributes);

            // userIdentity is not tagged to tenant
            if (!tenantIdsTaggedToUserIdentity.contains(account.getTenantId())) {
                Account savedAccount = accountRepository.save(account);
                identityService.tagTenantToUser(userIdentity.getId(), account.getTenantId());
                new AssignParentRoleUseCase(identityService, account.getTenantId(), userIdentity.getId()).assignRole();
                // TODO: send welcome email for parent
                return savedAccount;
            }


            // if user already has a parent account
            // TODO: fix
//            if (hasParentRole(userIdentity)) {
//                String msg = String.format("user with email %s already tagged to tenant with id %s", account.getEmail(), account.getTenantId());
//                throw new ParentAccountAlreadyExists(msg);
//            }

            Account savedAccount = accountRepository.save(account);
            new AssignParentRoleUseCase(identityService, account.getTenantId(), userIdentity.getId()).assignRole();
            // TODO: send welcome email
            return savedAccount;


        }
    }

    private static List<String> retrieveTenantIdsIfExistElseThrow(Map<String, List<String>> attributes) {
        return Optional
                .ofNullable(attributes.get(TENANT_ID_ATTR_KEY))
                .orElseThrow(() -> new UnexpectedTechnicalException("tenant-id attribute key is not set on user identity"));
    }
}
