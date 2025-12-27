package com.aura.details.repositories.jpa.links;

import com.aura.details.tables.links.AccountEducatorLinkTable;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface JpaAccountEducatorLinkRepository extends JpaRepository<AccountEducatorLinkTable, UUID>, JpaSpecificationExecutor<AccountEducatorLinkTable> {
}

