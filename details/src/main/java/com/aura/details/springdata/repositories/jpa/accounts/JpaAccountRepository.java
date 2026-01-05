package com.aura.details.springdata.repositories.jpa.accounts;

import com.aura.details.springdata.tables.accounts.AccountTable;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface JpaAccountRepository extends JpaRepository<AccountTable, UUID>, JpaSpecificationExecutor<AccountTable> {
    @EntityGraph(attributePaths = {
            "accountStudentLinks",
            "accountStudentLinks.student",
            "accountEducatorLinks",
            "accountEducatorLinks.educator"   // Assuming AccountEducatorLinkTable has 'educator' field
    })
    Optional<AccountTable> findWithAllDetailsAndLinksByTenantIdAndEmail(String tenantId, String email);
}

