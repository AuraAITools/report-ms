package com.aura.core.clients;

import java.util.Set;
import org.jspecify.annotations.Nullable;

public interface ResourceAuthorizationService {

    void registerResource(String name, String type, @Nullable String ownerId, Set<String> scopes);

    void deleteResource(String name);
}
