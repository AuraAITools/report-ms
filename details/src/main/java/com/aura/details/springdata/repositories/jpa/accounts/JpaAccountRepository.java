package com.aura.details.springdata.repositories.jpa.accounts;

import com.aura.details.springdata.models.accounts.AccountModel;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
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
}

