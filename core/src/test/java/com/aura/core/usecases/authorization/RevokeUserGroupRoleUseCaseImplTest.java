package com.aura.core.usecases.authorization;

import com.aura.core.clients.RelationshipAuthorizationService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;


import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

@ExtendWith(MockitoExtension.class)
@DisplayName("RevokeUserGroupRoleUseCaseImpl")
class RevokeUserGroupRoleUseCaseImplTest {

    @Mock
    RelationshipAuthorizationService relationshipService;

    @InjectMocks
    RevokeUserGroupRoleUseCaseImpl useCase;

    @Test
    @DisplayName("Should delete the relationship with the given user_group type, id, role, and target user")
    void deletes_relationship() {
        useCase.revoke("outlet_user_group", "outlet-1", "modifier", "target");

        verify(relationshipService).deleteRelationship(
                "outlet_user_group", "outlet-1", "modifier", "user", "target");
        verifyNoMoreInteractions(relationshipService);
    }

    @Test
    @DisplayName("Should pass through user_group type and role verbatim for system/group_admin")
    void passes_through_arguments_verbatim() {
        useCase.revoke("system_user_group", "aura", "group_admin", "target");

        verify(relationshipService).deleteRelationship(
                "system_user_group", "aura", "group_admin", "user", "target");
    }
}
