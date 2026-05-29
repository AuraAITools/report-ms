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
@DisplayName("GrantUserGroupRoleUseCaseImpl")
class GrantUserGroupRoleUseCaseImplTest {

    @Mock
    RelationshipAuthorizationService relationshipService;

    @InjectMocks
    GrantUserGroupRoleUseCaseImpl useCase;

    @Test
    @DisplayName("Should write the relationship with the given user_group type, id, role, and target user")
    void writes_relationship() {
        useCase.grant("institution_user_group", "inst-1", "admin", "target");

        verify(relationshipService).writeRelationship(
                "institution_user_group", "inst-1", "admin", "user", "target");
        verifyNoMoreInteractions(relationshipService);
    }

    @Test
    @DisplayName("Should pass through user_group type and role verbatim with no string mutation")
    void passes_through_arguments_verbatim() {
        useCase.grant("course_user_group", "course-1", "group_viewer", "target");

        verify(relationshipService).writeRelationship(
                "course_user_group", "course-1", "group_viewer", "user", "target");
    }
}
