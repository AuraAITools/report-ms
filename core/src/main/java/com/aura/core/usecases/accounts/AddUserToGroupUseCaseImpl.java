package com.aura.core.usecases.accounts;

import com.aura.core.clients.GroupAuthorizationService;
import com.aura.core.clients.IdentityService;
import com.aura.core.clients.UserIdentity;
import java.util.List;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class AddUserToGroupUseCaseImpl implements AddUserToGroupUseCase {
    private final IdentityService identityService;
    private final GroupAuthorizationService groupAuthorizationService;

    @Override
    public void addUserToGroup(String email, String groupName) {
        List<UserIdentity> users = identityService.findByEmail(email);
        if (users.isEmpty()) {
            throw new IllegalStateException("User with email '%s' not found".formatted(email));
        }
        String userId = users.getFirst().getId();
        groupAuthorizationService.addUserToGroup(userId, groupName);
    }
}
