package com.aura.details.springdata.repositories.jpa.accounts;

import com.aura.details.springdata.models.accounts.ParentModel;
import java.util.List;
import java.util.UUID;
import org.jspecify.annotations.Nullable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface JpaParentRepository extends JpaRepository<ParentModel, UUID> {

    @Query("SELECT p.account.id, p FROM ParentModel p WHERE p.account.id IN :accountIds")
    List<Object[]> findByAccountIds(@Param("accountIds") List<UUID> accountIds);

    List<ParentModel> findByTenantId(String tenantId);

    @Modifying
    @Query("update ParentModel p set p.profileImageKey = :key where p.id = :id")
    int updateProfileImageKeyById(@Param("id") UUID id, @Param("key") @Nullable String key);
}
