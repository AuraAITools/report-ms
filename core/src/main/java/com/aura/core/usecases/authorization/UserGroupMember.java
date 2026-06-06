package com.aura.core.usecases.authorization;

import java.util.List;

public record UserGroupMember(String userId, List<String> relations) {
}
