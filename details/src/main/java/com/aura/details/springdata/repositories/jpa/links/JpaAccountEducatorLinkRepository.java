package com.aura.details.springdata.repositories.jpa.links;

import com.aura.details.springdata.models.links.AccountEducatorLinkModel;
import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface JpaAccountEducatorLinkRepository extends JpaRepository<AccountEducatorLinkModel, UUID>, JpaSpecificationExecutor<AccountEducatorLinkModel> {

    @Query("SELECT l.account.id, l.educator FROM AccountEducatorLinkModel l WHERE l.account.id IN :accountIds")
    List<Object[]> findEducatorsByAccountIds(@Param("accountIds") List<UUID> accountIds);

    @Query("SELECT l.educator.id, l.account FROM AccountEducatorLinkModel l WHERE l.educator.id IN :educatorIds")
    List<Object[]> findAccountsByEducatorIds(@Param("educatorIds") List<UUID> educatorIds);

    void deleteAllByEducatorId(UUID educatorId);
}

