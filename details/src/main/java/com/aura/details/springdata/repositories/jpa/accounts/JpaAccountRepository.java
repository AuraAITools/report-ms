package com.aura.details.springdata.repositories.jpa.accounts;

import com.aura.core.entities.accounts.AccountFeatureState;
import com.aura.details.springdata.models.accounts.AccountModel;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.jspecify.annotations.Nullable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface JpaAccountRepository extends JpaRepository<AccountModel, UUID>, JpaSpecificationExecutor<AccountModel> {
    @EntityGraph(attributePaths = {
            "accountStudentLinks",
            "accountStudentLinks.student",
            "accountEducatorLinks",
            "accountEducatorLinks.educator"   // Assuming AccountEducatorLinkModel has 'educator' field
    })
    Optional<AccountModel> findWithAllDetailsAndLinksByTenantIdAndEmail(String tenantId, String email);

    @Nullable AccountModel findByEmailAndTenantId(String email, String tenantId);

    List<AccountModel> findAllByEmail(String email);

    @Nullable AccountModel findByUserIdAndTenantId(String userId, String tenantId);

    List<AccountModel> findAllByUserIdInAndTenantId(List<String> userIds, String tenantId);

    List<AccountModel> findByTenantId(String tenantId);

    @Modifying
    @Query("update AccountModel a set a.state = :state where a.id = :id")
    int updateStateById(@Param("id") UUID id, @Param("state") AccountModel.STATE state);

    @Modifying
    @Query("update AccountModel a set a.parentFeatureState = :state where a.id = :id")
    int updateParentFeatureStateById(@Param("id") UUID id, @Param("state") AccountFeatureState state);

    @Modifying
    @Query("update AccountModel a set a.educatorFeatureState = :state where a.id = :id")
    int updateEducatorFeatureStateById(@Param("id") UUID id, @Param("state") AccountFeatureState state);

    @Modifying
    @Query("update AccountModel a set a.staffFeatureState = :state where a.id = :id")
    int updateStaffFeatureStateById(@Param("id") UUID id, @Param("state") AccountFeatureState state);

    @Modifying
    @Query("update AccountModel a set a.profileImageKey = :key where a.id = :id")
    int updateProfileImageKeyById(@Param("id") UUID id, @Param("key") @Nullable String key);
}

