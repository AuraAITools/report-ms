package com.aura.details.springdata.repositories.jpa.links;

import com.aura.details.springdata.models.links.SubjectEducatorLinkModel;
import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface JpaSubjectEducatorLinkRepository extends JpaRepository<SubjectEducatorLinkModel, UUID>, JpaSpecificationExecutor<SubjectEducatorLinkModel> {

    @Query("SELECT l.educator.id, l.subject FROM SubjectEducatorLinkModel l WHERE l.educator.id IN :educatorIds")
    List<Object[]> findSubjectsByEducatorIds(@Param("educatorIds") List<UUID> educatorIds);

    void deleteAllByEducatorId(UUID educatorId);
}
