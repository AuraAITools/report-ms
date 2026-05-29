package com.aura.details.springdata.repositories.jpa.accounts;

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

    List<AccountModel> findByTenantId(String tenantId);

    @Modifying
    @Query("update AccountModel a set a.state = :state where a.id = :id")
    int updateStateById(@Param("id") UUID id, @Param("state") AccountModel.STATE state);

    @Modifying
    @Query("update AccountModel a set a.parentFeatureEnabled = true where a.id = :id")
    int enableParentFeatureById(@Param("id") UUID id);

    @Modifying
    @Query("update AccountModel a set a.educatorFeatureEnabled = true where a.id = :id")
    int enableEducatorFeatureById(@Param("id") UUID id);

    @Modifying
    @Query("update AccountModel a set a.staffFeatureEnabled = true where a.id = :id")
    int enableStaffFeatureById(@Param("id") UUID id);
}

