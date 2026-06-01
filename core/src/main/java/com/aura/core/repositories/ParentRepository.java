package com.aura.core.repositories;

import com.aura.core.entities.accounts.Parent;
import java.util.List;
import java.util.Map;
import org.jspecify.annotations.Nullable;

public interface ParentRepository {
    Parent save(Parent parent);

    List<Parent> saveAll(List<Parent> parents);

    @Nullable Parent findById(String id);

    /**
     * Targeted single-column update for the profile image S3 key. Avoids the
     * domain-mapper round-trip used by {@link #save(Parent)}, which would
     * re-attach the linked AccountModel on merge. Pass null to clear the key.
     */
    void updateProfileImageKeyById(String id, @Nullable String profileImageKey);

    Map<String, Parent> findAllByAccountIds(List<String> accountIds);

    List<Parent> findAllByTenantId(String tenantId);

    void deleteById(String id);
}
