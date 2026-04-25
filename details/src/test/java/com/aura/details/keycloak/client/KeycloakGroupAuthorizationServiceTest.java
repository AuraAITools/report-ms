package com.aura.details.keycloak.client;

import java.util.List;
import java.util.Set;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.keycloak.admin.client.resource.AuthorizationResource;
import org.keycloak.admin.client.resource.ClientResource;
import org.keycloak.admin.client.resource.GroupsResource;
import org.keycloak.admin.client.resource.PoliciesResource;
import org.keycloak.admin.client.resource.GroupPoliciesResource;
import org.keycloak.admin.client.resource.PermissionsResource;
import org.keycloak.admin.client.resource.ResourcePermissionsResource;
import org.keycloak.admin.client.resource.ResourcesResource;
import org.keycloak.admin.client.resource.UserResource;
import org.keycloak.admin.client.resource.UsersResource;
import org.keycloak.representations.idm.GroupRepresentation;
import org.keycloak.representations.idm.authorization.GroupPolicyRepresentation;
import org.keycloak.representations.idm.authorization.ResourcePermissionRepresentation;
import org.keycloak.representations.idm.authorization.ResourceRepresentation;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@DisplayName("Keycloak group authorization service")
@ExtendWith(MockitoExtension.class)
class KeycloakGroupAuthorizationServiceTest {

    @Mock private ClientResource clientResource;
    @Mock private GroupsResource groupsResource;
    @Mock private UsersResource usersResource;
    @Mock private AuthorizationResource authorizationResource;
    @Mock private PoliciesResource policiesResource;
    @Mock private GroupPoliciesResource groupPoliciesResource;
    @Mock private PermissionsResource permissionsResource;
    @Mock private ResourcePermissionsResource resourcePermissionsResource;
    @Mock private ResourcesResource resourcesResource;
    @Mock private UserResource userResource;

    private KeycloakGroupAuthorizationService service;

    @BeforeEach
    void setUp() {
        service = new KeycloakGroupAuthorizationService(clientResource, groupsResource, usersResource);
    }

    @DisplayName("Should create group with correct name")
    @Test
    void createGroup_createsGroupWithCorrectName() {
        String groupName = "institution:inst-001:admin";

        service.createGroup(groupName);

        ArgumentCaptor<GroupRepresentation> captor = ArgumentCaptor.forClass(GroupRepresentation.class);
        verify(groupsResource).add(captor.capture());
        assertThat(captor.getValue().getName()).isEqualTo(groupName);
    }

    @DisplayName("Should create group policy linked to group")
    @Test
    void createGroupPolicy_createsPolicy_linkedToGroup() {
        String policyName = "institution-inst-001-admin-policy";
        String groupName = "institution:inst-001:admin";
        String groupId = "group-uuid-123";

        GroupRepresentation group = new GroupRepresentation();
        group.setId(groupId);
        group.setName(groupName);
        when(groupsResource.groups(groupName, 0, 1)).thenReturn(List.of(group));
        when(clientResource.authorization()).thenReturn(authorizationResource);
        when(authorizationResource.policies()).thenReturn(policiesResource);
        when(policiesResource.group()).thenReturn(groupPoliciesResource);

        service.createGroupPolicy(policyName, groupName);

        ArgumentCaptor<GroupPolicyRepresentation> captor = ArgumentCaptor.forClass(GroupPolicyRepresentation.class);
        verify(groupPoliciesResource).create(captor.capture());
        assertThat(captor.getValue().getName()).isEqualTo(policyName);
    }

    @DisplayName("Should throw when group not found for policy creation")
    @Test
    void createGroupPolicy_throwsWhenGroupNotFound() {
        when(groupsResource.groups("nonexistent", 0, 1)).thenReturn(List.of());

        assertThatThrownBy(() -> service.createGroupPolicy("policy", "nonexistent"))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("nonexistent");
    }

    @DisplayName("Should create resource permission linked to resource and policies")
    @Test
    void createResourcePermission_createsPermission_linkedToResourceAndPolicies() {
        String permissionName = "outlet-outlet-001-permission";
        String resourceName = "outlet:outlet-001";
        Set<String> scopes = Set.of("view", "manage");
        Set<String> policyNames = Set.of("outlet-outlet-001-admin-policy", "institution-inst-001-admin-policy");

        ResourceRepresentation resource = new ResourceRepresentation();
        resource.setId("resource-uuid");
        resource.setName(resourceName);

        when(clientResource.authorization()).thenReturn(authorizationResource);
        when(authorizationResource.resources()).thenReturn(resourcesResource);
        when(resourcesResource.findByName(resourceName)).thenReturn(List.of(resource));
        when(authorizationResource.permissions()).thenReturn(permissionsResource);
        when(permissionsResource.resource()).thenReturn(resourcePermissionsResource);

        service.createResourcePermission(permissionName, resourceName, scopes, policyNames);

        ArgumentCaptor<ResourcePermissionRepresentation> captor =
                ArgumentCaptor.forClass(ResourcePermissionRepresentation.class);
        verify(resourcePermissionsResource).create(captor.capture());

        ResourcePermissionRepresentation captured = captor.getValue();
        assertThat(captured.getName()).isEqualTo(permissionName);
        assertThat(captured.getResources()).containsExactly("resource-uuid");
        assertThat(captured.getPolicies()).containsExactlyInAnyOrderElementsOf(policyNames);
    }

    @DisplayName("Should throw when resource not found for permission creation")
    @Test
    void createResourcePermission_throwsWhenResourceNotFound() {
        when(clientResource.authorization()).thenReturn(authorizationResource);
        when(authorizationResource.resources()).thenReturn(resourcesResource);
        when(resourcesResource.findByName("outlet:nonexistent")).thenReturn(List.of());

        assertThatThrownBy(() ->
                service.createResourcePermission("perm", "outlet:nonexistent", Set.of("view"), Set.of("policy")))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("outlet:nonexistent");
    }

    @DisplayName("Should join user to correct group")
    @Test
    void addUserToGroup_joinsUserToCorrectGroup() {
        String userId = "user-uuid";
        String groupName = "institution:inst-001:admin";
        String groupId = "group-uuid";

        GroupRepresentation group = new GroupRepresentation();
        group.setId(groupId);
        group.setName(groupName);
        when(groupsResource.groups(groupName, 0, 1)).thenReturn(List.of(group));
        when(usersResource.get(userId)).thenReturn(userResource);

        service.addUserToGroup(userId, groupName);

        verify(userResource).joinGroup(groupId);
    }

    @Test
    void addUserToGroup_throwsWhenGroupNotFound() {
        when(groupsResource.groups("nonexistent", 0, 1)).thenReturn(List.of());

        assertThatThrownBy(() -> service.addUserToGroup("user-id", "nonexistent"))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("nonexistent");
    }
}
