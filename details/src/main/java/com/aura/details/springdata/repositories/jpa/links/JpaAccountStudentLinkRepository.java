package com.aura.details.springdata.repositories.jpa.links;

import com.aura.details.springdata.models.links.AccountStudentLinkModel;
import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface JpaAccountStudentLinkRepository extends JpaRepository<AccountStudentLinkModel, UUID>, JpaSpecificationExecutor<AccountStudentLinkModel> {

    @Query("SELECT l.account.id, l.student FROM AccountStudentLinkModel l WHERE l.account.id IN :accountIds")
    List<Object[]> findStudentsByAccountIds(@Param("accountIds") List<UUID> accountIds);

    void deleteAllByStudentId(UUID studentId);
}

