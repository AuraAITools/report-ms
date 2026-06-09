package com.aura.details.springdata.repositories.jpa.links;

import com.aura.details.springdata.models.links.SubjectTestGroupLinkModel;
import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface JpaSubjectTestGroupLinkRepository extends JpaRepository<SubjectTestGroupLinkModel, UUID>, JpaSpecificationExecutor<SubjectTestGroupLinkModel> {

    void deleteAllBySubjectId(UUID subjectId);

    @Query("SELECT l.testGroup.id FROM SubjectTestGroupLinkModel l WHERE l.subject.id = :subjectId")
    List<UUID> findTestGroupIdsBySubjectId(@Param("subjectId") UUID subjectId);
}

