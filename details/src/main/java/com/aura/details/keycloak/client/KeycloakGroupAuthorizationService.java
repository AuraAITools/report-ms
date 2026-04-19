package com.aura.details.keycloak.client;

import com.aura.core.clients.GroupAuthorizationService;
import java.util.List;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.keycloak.admin.client.resource.ClientResource;
import org.keycloak.admin.client.resource.GroupsResource;
import org.keycloak.admin.client.resource.UsersResource;
import org.keycloak.representations.idm.GroupRepresentation;
import org.keycloak.representations.idm.authorization.GroupPolicyRepresentation;
import org.keycloak.representations.idm.authorization.ResourcePermissionRepresentation;
import org.keycloak.representations.idm.authorization.ResourceRepresentation;
import org.keycloak.representations.idm.authorization.DecisionStrategy;

@Slf4j
@RequiredArgsConstructor
public class KeycloakGroupAuthorizationService implements GroupAuthorizationService {
    private final ClientResource clientResource;
    private final GroupsResource groupsResource;
    private final UsersResource usersResource;

    @Override
    public void createGroup(String groupName) {
        GroupRepresentation group = new GroupRepresentation();
        group.setName(groupName);
        groupsResource.add(group);
        log.info("created Keycloak group '{}'", groupName);
    }

    @Override
    public void createGroupPolicy(String policyName, String groupName) {
        String groupId = findGroupIdByName(groupName);

        GroupPolicyRepresentation policy = new GroupPolicyRepresentation();
        policy.setName(policyName);
        policy.setGroupsClaim("groups");
        policy.addGroup(groupId);
        clientResource.authorization().policies().group().create(policy);
        log.info("created group policy '{}' for group '{}'", policyName, groupName);
    }

    @Override
    public void createResourcePermission(String permissionName, String resourceName,
                                          Set<String> scopes, Set<String> policyNames) {
        List<ResourceRepresentation> resources = clientResource.authorization()
                .resources().findByName(resourceName);
        if (resources.isEmpty()) {
            throw new IllegalStateException("UMA resource '%s' not found in Keycloak".formatted(resourceName));
        }
        String resourceId = resources.getFirst().getId();

        ResourcePermissionRepresentation permission = new ResourcePermissionRepresentation();
        permission.setName(permissionName);
        permission.setResources(Set.of(resourceId));
        permission.setPolicies(policyNames);
        permission.setDecisionStrategy(DecisionStrategy.AFFIRMATIVE);
        clientResource.authorization().permissions().resource().create(permission);
        log.info("created resource permission '{}' on resource '{}' with policies {}",
                permissionName, resourceName, policyNames);
    }

    @Override
    public void addUserToGroup(String userId, String groupName) {
        String groupId = findGroupIdByName(groupName);
        usersResource.get(userId).joinGroup(groupId);
        log.info("added user '{}' to group '{}'", userId, groupName);
    }

    private String findGroupIdByName(String groupName) {
        List<GroupRepresentation> groups = groupsResource.groups(groupName, 0, 1);
        List<GroupRepresentation> exactMatches = groups.stream()
                .filter(g -> groupName.equals(g.getName()))
                .toList();
        if (exactMatches.isEmpty()) {
            throw new IllegalStateException("group '%s' not found in Keycloak".formatted(groupName));
        }
        return exactMatches.getFirst().getId();
    }
}
