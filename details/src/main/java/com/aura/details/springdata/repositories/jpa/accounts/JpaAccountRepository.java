package com.aura.details.springdata.repositories.jpa.accounts;

import com.aura.details.springdata.tables.accounts.AccountTable;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface JpaAccountRepository extends JpaRepository<AccountTable, UUID>, JpaSpecificationExecutor<AccountTable> {
}

