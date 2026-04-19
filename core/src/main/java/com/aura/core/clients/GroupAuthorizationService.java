package com.aura.core.clients;

import java.util.Set;

public interface GroupAuthorizationService {

    void createGroup(String groupName);

    void createGroupPolicy(String policyName, String groupName);

    void createResourcePermission(String permissionName, String resourceName, Set<String> scopes, Set<String> policyNames);

    void addUserToGroup(String userId, String groupName);
}
