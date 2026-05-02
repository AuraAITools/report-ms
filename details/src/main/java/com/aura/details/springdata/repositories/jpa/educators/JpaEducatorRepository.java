package com.aura.details.springdata.repositories.jpa.educators;

import com.aura.details.springdata.models.educators.EducatorModel;
import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface JpaEducatorRepository extends JpaRepository<EducatorModel, UUID>, JpaSpecificationExecutor<EducatorModel> {

    @Query("SELECT e.id, a.userId FROM EducatorModel e JOIN e.accountEducatorLinks ael JOIN ael.account a WHERE e.id IN :educatorIds")
    List<Object[]> findUserIdsByIds(@Param("educatorIds") List<UUID> educatorIds);
}

